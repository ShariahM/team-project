package interface_adapter.recommendations;

import use_case.recommendations.RecommendationsOutputBoundary;
import use_case.recommendations.RecommendationsOutputData;
import use_case.recommendations.RecommendedMovie;

import java.util.ArrayList;
import java.util.List;

/**
 * Updates the recommendations view model. It does not switch screens: the user is already on
 * the Recommendations view when they ask, and this runs on the view's background thread.
 */
public class RecommendationsPresenter implements RecommendationsOutputBoundary {
    private final RecommendationsViewModel recommendationsViewModel;

    public RecommendationsPresenter(RecommendationsViewModel recommendationsViewModel) {
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
        recommendationsViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final RecommendationsState state = recommendationsViewModel.getState();
        state.setRecommendations(new ArrayList<>());
        state.setErrorMessage(errorMessage);
        recommendationsViewModel.notifyStateChanged();
    }

    /** e.g. "Tenet (2020) - because you liked Inception". */
    static String format(RecommendedMovie movie) {
        final String year = movie.getReleaseYear() > 0 ? " (" + movie.getReleaseYear() + ")" : "";
        return movie.getTitle() + year + " - because you liked " + movie.getBecauseYouLiked();
    }
}
