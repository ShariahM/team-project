package interface_adapter.recommendations;

import use_case.recommendations.RecommendationsInputBoundary;

public class RecommendationsController {
    private final RecommendationsInputBoundary recommendationsInteractor;

    public RecommendationsController(RecommendationsInputBoundary recommendationsInteractor) {
        this.recommendationsInteractor = recommendationsInteractor;
    }

    public void execute() {
        recommendationsInteractor.execute();
    }
}
