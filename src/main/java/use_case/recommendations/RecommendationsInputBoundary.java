package use_case.recommendations;

/**
 * Entry point of the recommendations use case. It takes no input: the user's
 * saved ratings decide what gets recommended.
 */
public interface RecommendationsInputBoundary {
    void execute();
}
