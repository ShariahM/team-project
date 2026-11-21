package interface_adapter.recommendations;

import interface_adapter.ViewManagerModel;
import use_case.recommendations.RecommendationsOutputBoundary;
import use_case.recommendations.RecommendationsOutputData;
import use_case.recommendations.RecommendedMovie;

import java.util.ArrayList;
import java.util.List;

public class RecommendationsPresenter implements RecommendationsOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final RecommendationsViewModel recommendationsViewModel;

    public RecommendationsPresenter(ViewManagerModel viewManagerModel,
                                    RecommendationsViewModel recommendationsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.recommendationsViewModel = recommendationsViewModel;
    }

    @Override
    public void prepareSuccessView(RecommendationsOutputData outputData) {
        final List<String> lines = new ArrayList<>();
        for (RecommendedMovie movie : outputData.getRecommendations()) {
            lines.add(format(movie));
        }

        final RecommendationsState state = recommendationsViewModel.getState();
        state.setRecommendations(lines);
        state.setErrorMessage(null);
        showView();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final RecommendationsState state = recommendationsViewModel.getState();
        state.setRecommendations(new ArrayList<>());
        state.setErrorMessage(errorMessage);
        showView();
    }

    /** e.g. "Tenet (2020) - because you liked Inception". */
    static String format(RecommendedMovie movie) {
        final String year = movie.getReleaseYear() > 0 ? " (" + movie.getReleaseYear() + ")" : "";
        return movie.getTitle() + year + " - because you liked " + movie.getBecauseYouLiked();
    }

    private void showView() {
        recommendationsViewModel.notifyStateChanged();
        viewManagerModel.setState(RecommendationsViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }
}
