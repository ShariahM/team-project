package use_case.recommendations;

import entity.Movie;

import java.io.IOException;
import java.util.List;

/**
 * What the recommendations use case needs from a movie database: movies similar to a given one.
 */
public interface MovieRecommendationsDataAccessInterface {
    /**
     * Returns movies recommended for the given movie.
     *
     * @param movieID the movie to get recommendations for
     * @return the recommended movies, possibly empty
     * @throws IOException if the recommendations could not be loaded
     */
    List<Movie> getRecommendationsFor(int movieID) throws IOException;
}
