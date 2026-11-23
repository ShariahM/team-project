package interface_adapter.explore;

import use_case.explore.ExploreInputBoundary;
import use_case.explore.ExploreInputData;

/**
 * Converts movie-search input from the view into use-case input.
 */
public class ExploreController {
    private final ExploreInputBoundary exploreUseCase;

    /**
     * Creates a controller for the explore search use case.
     *
     * @param exploreUseCase boundary invoked to execute movie searches
     * @throws IllegalArgumentException if the use case is {@code null}
     */
    public ExploreController(ExploreInputBoundary exploreUseCase) {
        if (exploreUseCase == null) {
            throw new IllegalArgumentException("Explore use case must not be null.");
        }
        this.exploreUseCase = exploreUseCase;
    }

    /**
     * Normalizes a title and starts a movie search.
     *
     * @param query raw title entered by the user
     */
    public void execute(String query) {
        final String normalizedQuery = query == null ? "" : query.trim();
        exploreUseCase.execute(new ExploreInputData(normalizedQuery));
    }
}
