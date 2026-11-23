package use_case.explore;

import entity.Movie;

import java.io.IOException;
import java.util.List;

/**
 * Data-access operations required by the movie explore use case.
 */
public interface ExploreDataAccessInterface {
    /**
     * Searches the movie data source for titles matching the query.
     *
     * @param query non-blank movie title or search terms
     * @return matching movies
     * @throws IOException if the data source request fails
     * @throws InterruptedException if the request is interrupted
     */
    List<Movie> searchMovies(String query) throws IOException, InterruptedException;
}
