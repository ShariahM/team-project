package interface_adapter.explore;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores explore-screen state and notifies interested views about updates.
 */
public class ExploreViewModel {
    /** Heading shown above the movie search form. */
    public static final String TITLE_LABEL = "Search Movies";
    /** Text displayed on the movie search button. */
    public static final String SEARCH_BUTTON_LABEL = "Search";
    /** Card-layout identifier for the explore screen. */
    public static final String VIEW_NAME = "explore";

    private final List<ExploreViewModelListener> listeners = new ArrayList<>();
    private ExploreState state = new ExploreState();

    /**
     * Returns the current explore-screen state.
     *
     * @return current state
     */
    public ExploreState getState() {
        return state;
    }

    /**
     * Replaces the current explore-screen state.
     *
     * @param state new state
     * @throws IllegalArgumentException if {@code state} is {@code null}
     */
    public void setState(ExploreState state) {
        if (state == null) {
            throw new IllegalArgumentException("Explore state must not be null.");
        }
        this.state = state;
    }

    /**
     * Registers a view to receive state-change notifications.
     *
     * @param listener listener to register
     * @throws IllegalArgumentException if {@code listener} is {@code null}
     */
    public void addListener(ExploreViewModelListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("Listener must not be null.");
        }
        listeners.add(listener);
    }

    /**
     * Notifies all registered views that the state has changed.
     */
    public void notifyStateChanged() {
        for (ExploreViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
