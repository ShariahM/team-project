package data_access;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import entity.Movie;
import io.github.cdimascio.dotenv.Dotenv;
import use_case.explore.ExploreDataAccessInterface;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

/**
 * Sends requests to TMDB's version 3 API.
 */
public class MovieService implements ExploreDataAccessInterface {
    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(20);

    private final HttpClient httpClient;
    private final String apiReadAccessToken;
    private final String baseUrl;

    /**
     * Creates a service using TMDB_TOKEN from a .env file in the working directory.
     *
     * @throws IllegalStateException if TMDB_TOKEN is missing or blank
     */
    public MovieService() {
        this(Dotenv.configure().ignoreIfMissing().load().get("TMDB_TOKEN"));
    }

    /**
     * Creates a service with the supplied TMDB Read Access Token.
     *
     * @param apiReadAccessToken the token used for Bearer authentication
     * @throws IllegalStateException if the token is missing or blank
     */
    private MovieService(String apiReadAccessToken) {
        if (apiReadAccessToken == null || apiReadAccessToken.isBlank()) {
            throw new IllegalStateException(
                    "TMDB_TOKEN is not set. Set it to your TMDB API Read Access Token.");
        }
        this.apiReadAccessToken = apiReadAccessToken.trim();
        this.baseUrl = "https://api.themoviedb.org/3";
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(CONNECT_TIMEOUT)
                .build();
    }

    /**
     * Searches TMDB for movies matching a title.
     *
     * @param query the movie title or search terms
     * @return the matching movies
     * @throws IllegalArgumentException if the query is null or blank
     * @throws IOException if the request fails or TMDB returns an error response
     * @throws InterruptedException if the request is interrupted
     */
    public List<Movie> searchMovies(String query) throws IOException, InterruptedException {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Search query must not be blank.");
        }
        String encodedQuery = URLEncoder.encode(query.trim(), StandardCharsets.UTF_8);
        JsonObject response = getJsonObject("/search/movie?query=" + encodedQuery);
        return MovieFactory.createMovies(getResults(response));
    }

    /**
     * Retrieves a movie's TMDB details. The director is provided separately by
     * {@link #getMovieCredits(int)} because TMDB returns it from the credits endpoint.
     *
     * @param movieId the TMDB movie ID
     * @return the movie details
     * @throws IllegalArgumentException if the movie ID is not positive
     * @throws IOException if the request fails or TMDB returns an error response
     * @throws InterruptedException if the request is interrupted
     */
    public Movie getMovieDetails(int movieId) throws IOException, InterruptedException {
        validateMovieId(movieId);
        JsonObject response = getJsonObject("/movie/" + movieId);
        try {
            return MovieFactory.createMovie(response);
        } catch (IllegalArgumentException exception) {
            throw new IOException("TMDB returned invalid movie details for movie ID " + movieId + ".",
                    exception);
        }
    }

    /**
     * Retrieves the director's name from TMDB credits, or an empty string if no director is listed.
     *
     * @param movieId the TMDB movie ID
     * @return the director's name, or an empty string if no director is listed
     * @throws IllegalArgumentException if the movie ID is not positive
     * @throws IOException if the request fails or TMDB returns an error response
     * @throws InterruptedException if the request is interrupted
     */
    public String getMovieCredits(int movieId) throws IOException, InterruptedException {
        validateMovieId(movieId);
        JsonObject response = getJsonObject("/movie/" + movieId + "/credits");
        JsonElement crewElement = response.get("crew");
        if (crewElement == null || crewElement.isJsonNull() || !crewElement.isJsonArray()) {
            return "";
        }

        JsonArray crew = crewElement.getAsJsonArray();
        for (JsonElement person : crew) {
            if (person == null || !person.isJsonObject()) {
                continue;
            }
            JsonObject personObject = person.getAsJsonObject();
            if ("Director".equalsIgnoreCase(getString(personObject, "job"))) {
                String name = getString(personObject, "name");
                if (!name.isEmpty()) {
                    return name;
                }
            }
        }
        return "";
    }

    /**
     * Retrieves TMDB's currently popular movies.
     *
     * @return the popular movies
     * @throws IOException if the request fails or TMDB returns an error response
     * @throws InterruptedException if the request is interrupted
     */
    public List<Movie> getPopularMovies() throws IOException, InterruptedException {
        JsonObject response = getJsonObject("/movie/popular");
        return MovieFactory.createMovies(getResults(response));
    }

    /**
     * Sends a GET request to TMDB and parses its JSON object response.
     *
     * @param path the endpoint path, including any query string
     * @return the parsed response object
     * @throws IOException if the request fails, TMDB returns an error, or the response is invalid
     * @throws InterruptedException if the request is interrupted
     */
    private JsonObject getJsonObject(String path) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .timeout(REQUEST_TIMEOUT)
                .header("Authorization", "Bearer " + apiReadAccessToken)
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();
        if (statusCode < 200 || statusCode >= 300) {
            throw new IOException("TMDB request failed with HTTP status " + statusCode
                    + errorMessage(response.body()));
        }

        try {
            JsonElement json = JsonParser.parseString(response.body());
            if (json.isJsonObject()) {
                return json.getAsJsonObject();
            }
        } catch (JsonParseException exception) {
            throw new IOException("TMDB returned invalid JSON.", exception);
        }
        throw new IOException("TMDB returned a JSON response that was not an object.");
    }

    /**
     * Retrieves the results array from a TMDB search or popular-movies response.
     *
     * @param response the parsed TMDB response object
     * @return the response's results array
     * @throws IOException if the response does not contain a results array
     */
    private JsonArray getResults(JsonObject response) throws IOException {
        JsonElement results = response.get("results");
        if (results == null || results.isJsonNull() || !results.isJsonArray()) {
            throw new IOException("TMDB response did not contain a results array.");
        }
        return results.getAsJsonArray();
    }

    /**
     * Gets TMDB's status message from an error response body, when available.
     *
     * @param responseBody the body returned with the HTTP error
     * @return the formatted status message, or a period if no message is available
     */
    private String errorMessage(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            return ".";
        }
        try {
            JsonElement json = JsonParser.parseString(responseBody);
            if (json.isJsonObject()) {
                String message = getString(json.getAsJsonObject(), "status_message");
                if (!message.isEmpty()) {
                    return ": " + message;
                }
            }
        } catch (JsonParseException exception) {
            // The HTTP status is still useful when the error body is not JSON.
        }
        return ".";
    }

    /**
     * Reads and trims a string field from a JSON object.
     *
     * @param json the object containing the field
     * @param key the field name
     * @return the trimmed field value, or an empty string if it is absent or not a string
     */
    private String getString(JsonObject json, String key) {
        JsonElement value = json.get(key);
        if (value == null || value.isJsonNull() || !value.isJsonPrimitive()
                || !value.getAsJsonPrimitive().isString()) {
            return "";
        }
        return value.getAsString().trim();
    }

    /**
     * Checks that a movie ID can be used in a TMDB endpoint path.
     *
     * @param movieId the movie ID to validate
     * @throws IllegalArgumentException if the movie ID is not positive
     */
    private void validateMovieId(int movieId) {
        if (movieId <= 0) {
            throw new IllegalArgumentException("Movie ID must be positive.");
        }
    }
}
