package interface_adapter.recommendations;

import java.util.ArrayList;
import java.util.List;

public class RecommendationsViewModel {

    public static final String TITLE_LABEL = "Movie Diary - Recommendations";
    public static final String VIEW_NAME = "recommendations";

    private final List<RecommendationsViewModelListener> listeners = new ArrayList<>();

    private RecommendationsState state = new RecommendationsState();

    public RecommendationsState getState() {
        return this.state;
    }

    public void setState(RecommendationsState state) {
        this.state = state;
    }

    public void addListener(RecommendationsViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (RecommendationsViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
