package interface_adapter.explore;

import java.util.ArrayList;
import java.util.List;

public class ExploreViewModel {
    public static final String TITLE_LABEL = "Search Movies";
    public static final String SEARCH_BUTTON_LABEL = "Search";
    public static final String VIEW_NAME = "explore";

    private final List<ExploreViewModelListener> listeners = new ArrayList<>();
    private ExploreState state = new ExploreState();

    public ExploreState getState() {
        return state;
    }

    public void setState(ExploreState state) {
        if (state == null) {
            throw new IllegalArgumentException("Explore state must not be null.");
        }
        this.state = state;
    }

    public void addListener(ExploreViewModelListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null.");
        }
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (ExploreViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
