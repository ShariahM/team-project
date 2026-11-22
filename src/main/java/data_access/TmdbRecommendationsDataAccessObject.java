package data_access;

import entity.Movie;
import use_case.recommendations.MovieRecommendationsDataAccessInterface;

import java.io.IOException;
import java.util.List;

/**
 * Gets movie recommendations from TMDB through MovieService.
 */
public class TmdbRecommendationsDataAccessObject implements MovieRecommendationsDataAccessInterface {
    private MovieService movieService;

    @Override
    public List<Movie> getRecommendationsFor(int movieID) throws IOException {
        try {
            return getMovieService().getRecommendations(movieID);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IOException("The TMDB request was interrupted.", exception);
        } catch (IllegalStateException | IllegalArgumentException exception) {
            // e.g. TMDB_TOKEN missing from .env: report it in the view instead of crashing the app.
            throw new IOException(exception.getMessage(), exception);
        }
    }

    /** Created on first use, so a missing token shows up as an error message, not a crash at startup. */
    private MovieService getMovieService() {
        if (movieService == null) {
            movieService = new MovieService();
        }
        return movieService;
    }
}
