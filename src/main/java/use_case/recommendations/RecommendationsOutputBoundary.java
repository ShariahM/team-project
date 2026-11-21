package use_case.recommendations;

/**
 * How the recommendations interactor reports its result to the outer layers.
 */
public interface RecommendationsOutputBoundary {
    void prepareSuccessView(RecommendationsOutputData outputData);

    void prepareFailView(String errorMessage);
}
