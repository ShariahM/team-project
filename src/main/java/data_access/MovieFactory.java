package data_access;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import entity.Movie;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Converts TMDB movie JSON objects into the application's Movie model.
 */
public final class MovieFactory {
    private static final String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";

    /**
     * Prevents instances of this utility class from being created.
     */
    private MovieFactory() {
        // Utility class; use the static factory methods.
    }

    /**
     * Creates a Movie from one TMDB movie object.
     *
     * @param json the TMDB movie object
     * @return the corresponding application movie
     * @throws IllegalArgumentException if the JSON is missing a valid movie ID or title
     */
    public static Movie createMovie(JsonObject json) {
        if (json == null) {
            throw new IllegalArgumentException("Movie JSON must not be null.");
        }

        int movieId = getPositiveInt(json, "id");
        String title = getString(json, "title");
        if (movieId <= 0 || title.isEmpty()) {
            throw new IllegalArgumentException("Movie JSON must contain a positive ID and a title.");
        }

        int releaseYear = getReleaseYear(getString(json, "release_date"));
        String overview = getString(json, "overview");
        String posterURL = getPosterURL(getString(json, "poster_path"));
        Set<String> genres = getGenres(json.get("genres"));

        // Search and popular results do not include the director; credits are fetched separately.
        return new Movie(movieId, title, releaseYear, genres, "", overview, posterURL);
    }

    /**
     * Converts valid movie objects in a TMDB results array. Malformed entries are skipped.
     *
     * @param results the TMDB results array, or {@code null}
     * @return the movies created from valid entries; an empty list if results is null
     */
    public static List<Movie> createMovies(JsonArray results) {
        List<Movie> movies = new ArrayList<>();
        if (results == null) {
            return movies;
        }

        for (JsonElement result : results) {
            if (result == null || !result.isJsonObject()) {
                continue;
            }
            try {
                movies.add(createMovie(result.getAsJsonObject()));
            } catch (IllegalArgumentException exception) {
                // One malformed result should not prevent the other results from being used.
            }
        }
        return movies;
    }

    /**
     * Reads a positive integer field from a JSON object.
     *
     * @param json the object containing the field
     * @param key the field name
     * @return the positive integer value, or {@code 0} if the field is absent or invalid
     */
    private static int getPositiveInt(JsonObject json, String key) {
        JsonElement value = json.get(key);
        if (value == null || value.isJsonNull() || !value.isJsonPrimitive()) {
            return 0;
        }
        try {
            int parsed = value.getAsInt();
            return parsed > 0 ? parsed : 0;
        } catch (NumberFormatException | UnsupportedOperationException exception) {
            return 0;
        }
    }

    /**
     * Reads a string field from a JSON object and trims surrounding whitespace.
     *
     * @param json the object containing the field
     * @param key the field name
     * @return the trimmed string, or an empty string if the field is absent or not a string
     */
    private static String getString(JsonObject json, String key) {
        JsonElement value = json.get(key);
        if (value == null || value.isJsonNull() || !value.isJsonPrimitive()
                || !value.getAsJsonPrimitive().isString()) {
            return "";
        }
        return value.getAsString().trim();
    }

    /**
     * Extracts the four-digit year at the start of a TMDB release date.
     *
     * @param releaseDate the release date string
     * @return the release year, or {@code 0} if it cannot be read
     */
    private static int getReleaseYear(String releaseDate) {
        if (releaseDate.length() < 4) {
            return 0;
        }
        try {
            return Integer.parseInt(releaseDate.substring(0, 4));
        } catch (NumberFormatException exception) {
            return 0;
        }
    }

    /**
     * Converts a TMDB poster path into a full-size image URL.
     *
     * @param posterPath the path returned by TMDB
     * @return the full poster URL, or an empty string if no path is provided
     */
    private static String getPosterURL(String posterPath) {
        if (posterPath.isEmpty()) {
            return "";
        }
        return POSTER_BASE_URL + (posterPath.startsWith("/") ? posterPath : "/" + posterPath);
    }

    /**
     * Extracts genre names from a TMDB genres array.
     *
     * @param genresElement the JSON element containing the genres array
     * @return the genre names, or an empty set if the element is absent or invalid
     */
    private static Set<String> getGenres(JsonElement genresElement) {
        Set<String> genres = new LinkedHashSet<>();
        if (genresElement == null || genresElement.isJsonNull() || !genresElement.isJsonArray()) {
            return genres;
        }

        for (JsonElement genreElement : genresElement.getAsJsonArray()) {
            if (genreElement != null && genreElement.isJsonObject()) {
                String name = getString(genreElement.getAsJsonObject(), "name");
                if (!name.isEmpty()) {
                    genres.add(name);
                }
            }
        }
        return genres;
    }
}
