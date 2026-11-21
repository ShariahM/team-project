package use_case.recommendations;

import java.util.List;

/**
 * The ranked recommendations, best first.
 */
public class RecommendationsOutputData {
    private final List<RecommendedMovie> recommendations;

    public RecommendationsOutputData(List<RecommendedMovie> recommendations) {
        this.recommendations = recommendations;
    }

    public List<RecommendedMovie> getRecommendations() {
        return recommendations;
    }
}
