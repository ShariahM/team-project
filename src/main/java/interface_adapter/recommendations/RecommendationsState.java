package interface_adapter.recommendations;

import java.util.ArrayList;
import java.util.List;

/**
 * What the recommendations view shows: ready-to-display lines, or an error message.
 */
public class RecommendationsState {
    private List<String> recommendations = new ArrayList<>();
    private String errorMessage;

    public List<String> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<String> recommendations) {
        this.recommendations = recommendations;
    }

    /** Null when the last request succeeded. */
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
