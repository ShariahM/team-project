package use_case.explore;

import entity.Movie;

import java.io.IOException;
import java.util.List;

/**
 * Data-access operations required by the movie explore use case.
 */
public interface ExploreDataAccessInterface {
    List<Movie> searchMovies(String query) throws IOException, InterruptedException;
}
