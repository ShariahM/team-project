package interface_adapter.explore;

import interface_adapter.ViewManagerModel;
import use_case.explore.ExploreOutputBoundary;
import use_case.explore.ExploreOutputData;

import javax.swing.SwingUtilities;
import java.util.ArrayList;
import java.util.List;

public class ExplorePresenter implements ExploreOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final ExploreViewModel exploreViewModel;

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

    @Override
    public void prepareFailView(String errorMessage) {
        final ExploreState state = new ExploreState();
        state.setQuery(exploreViewModel.getState().getQuery());
        state.setErrorMessage(errorMessage);
        exploreViewModel.setState(state);
        showExploreView();
    }

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
