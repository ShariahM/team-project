package use_case.explore;

/**
 * Reports the result of a movie search to the presentation layer.
 */
public interface ExploreOutputBoundary {
    /**
     * Presents the successful search result.
     *
     * @param outputData query and matching movie details
     */
    void prepareSuccessView(ExploreOutputData outputData);

    /**
     * Presents a search failure.
     *
     * @param errorMessage message to display to the user
     */
    void prepareFailView(String errorMessage);
}
