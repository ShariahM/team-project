package use_case.explore;

/**
 * Entry point for searching movies in the explore use case.
 */
public interface ExploreInputBoundary {
    /**
     * Starts a movie search with the supplied input.
     *
     * @param inputData title and other input required for the search
     */
    void execute(ExploreInputData inputData);
}
