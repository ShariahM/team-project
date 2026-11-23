package interface_adapter.explore;

import interface_adapter.ViewManagerModel;
import use_case.explore.ExploreOutputBoundary;
import use_case.explore.ExploreOutputData;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

/**
 * Converts explore use-case output into view-model state.
 */
public class ExplorePresenter implements ExploreOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final ExploreViewModel exploreViewModel;

    /**
     * Creates a presenter for the explore screen.
     *
     * @param viewManagerModel model used to display the explore screen
     * @param exploreViewModel model containing search results and status
     * @throws IllegalArgumentException if either model is {@code null}
     */
    public ExplorePresenter(ViewManagerModel viewManagerModel, ExploreViewModel exploreViewModel) {
        if (viewManagerModel == null) {
            throw new IllegalArgumentException("View manager model must not be null.");
        }
        if (exploreViewModel == null) {
            throw new IllegalArgumentException("Explore view model must not be null.");
        }
        this.viewManagerModel = viewManagerModel;
        this.exploreViewModel = exploreViewModel;
    }

    /**
     * Converts successful search output into view-model state.
     *
     * @param outputData query and matching movie details
     * @throws IllegalArgumentException if output data is {@code null}
     */
    @Override
    public void prepareSuccessView(ExploreOutputData outputData) {
        if (outputData == null) {
            throw new IllegalArgumentException("Explore output data must not be null.");
        }
        final ExploreState state = new ExploreState();
        state.setQuery(outputData.getQuery());
        final List<ExploreState.MovieResult> movies = new ArrayList<>();
        for (ExploreOutputData.MovieData movie : outputData.getMovies()) {
            movies.add(new ExploreState.MovieResult(
                    movie.getMovieID(),
                    movie.getTitle(),
                    movie.getReleaseYear(),
                    movie.getGenres(),
                    movie.getDirector(),
                    movie.getOverview(),
                    movie.getPosterURL()));
        }
        state.setMovies(movies);
        exploreViewModel.setState(state);
        showExploreView();
    }

    /**
     * Prepares view-model state containing a search error.
     *
     * @param errorMessage error message to show
     */
    @Override
    public void prepareFailView(String errorMessage) {
        final ExploreState state = new ExploreState();
        state.setQuery(exploreViewModel.getState().getQuery());
        state.setErrorMessage(errorMessage);
        exploreViewModel.setState(state);
        showExploreView();
    }

    /**
     * Notifies the view model and selects the explore card on the Swing event thread.
     */
    private void showExploreView() {
        final Runnable update = () -> {
            exploreViewModel.notifyStateChanged();
            viewManagerModel.setState(ExploreViewModel.VIEW_NAME);
            viewManagerModel.notifyViewChanged();
        };
        if (SwingUtilities.isEventDispatchThread()) {
            update.run();
        }
        else {
            SwingUtilities.invokeLater(update);
        }
    }
}
