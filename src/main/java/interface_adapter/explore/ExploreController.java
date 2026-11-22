package interface_adapter.explore;

import use_case.explore.ExploreInputBoundary;
import use_case.explore.ExploreInputData;

public class ExploreController {
    private final ExploreInputBoundary exploreUseCase;

    public ExploreController(ExploreInputBoundary exploreUseCase) {
        if (exploreUseCase == null) {
            throw new IllegalArgumentException("Explore use case must not be null.");
        }
        this.exploreUseCase = exploreUseCase;
    }

    public void execute(String query) {
        final String normalizedQuery = query == null ? "" : query.trim();
        exploreUseCase.execute(new ExploreInputData(normalizedQuery));
    }
}
