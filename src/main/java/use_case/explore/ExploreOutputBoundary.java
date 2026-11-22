package use_case.explore;

/**
 * Reports the result of a movie search to the presentation layer.
 */
public interface ExploreOutputBoundary {
    void prepareSuccessView(ExploreOutputData outputData);

    void prepareFailView(String errorMessage);
}
