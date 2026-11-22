package use_case.explore;

import entity.Movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements movie-title search for the explore screen.
 */
public class ExploreInteractor implements ExploreInputBoundary {
    public static final String BLANK_QUERY_MESSAGE = "Search query must not be blank.";
    public static final String SEARCH_FAILED_MESSAGE = "Could not search movies: ";

    private final ExploreDataAccessInterface dataAccess;
    private final ExploreOutputBoundary presenter;

    public ExploreInteractor(ExploreDataAccessInterface dataAccess,
                             ExploreOutputBoundary presenter) {
        if (dataAccess == null) {
            throw new IllegalArgumentException("Data access must not be null.");
        }
        if (presenter == null) {
            throw new IllegalArgumentException("Presenter must not be null.");
        }
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(ExploreInputData inputData) {
        if (inputData == null) {
            presenter.prepareFailView(BLANK_QUERY_MESSAGE);
            return;
        }

        final String query = inputData.getQuery();
        if (query == null || query.trim().isEmpty()) {
            presenter.prepareFailView(BLANK_QUERY_MESSAGE);
            return;
        }

        final String trimmedQuery = query.trim();
        try {
            final List<Movie> movies = dataAccess.searchMovies(trimmedQuery);
            final List<ExploreOutputData.MovieData> movieData = new ArrayList<>();
            for (Movie movie : movies) {
                movieData.add(new ExploreOutputData.MovieData(
                        movie.getMovieID(),
                        movie.getTitle(),
                        movie.getReleaseYear(),
                        movie.getGenres(),
                        movie.getDirector(),
                        movie.getOverview(),
                        movie.getPosterURL()));
            }
            presenter.prepareSuccessView(new ExploreOutputData(trimmedQuery, movieData));
        }
        catch (IOException exception) {
            presenter.prepareFailView(SEARCH_FAILED_MESSAGE
                    + (exception.getMessage() == null ? "request failed" : exception.getMessage()));
        }
        catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            presenter.prepareFailView(SEARCH_FAILED_MESSAGE + "request was interrupted");
        }
    }

}
