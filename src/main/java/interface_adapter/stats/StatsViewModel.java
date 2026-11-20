package interface_adapter.stats;

import java.util.ArrayList;
import java.util.List;

public class StatsViewModel {

    public static final String TITLE_LABEL = "Movie Diary - Stats";
    public static final String VIEW_NAME = "stats";

    private final List<StatsViewModelListener> listeners = new ArrayList<>();

    private StatsState state = new StatsState();

    public StatsState getState() {
        return this.state;
    }

    public void setState(StatsState state) {
        this.state = state;
    }

    public void addListener(StatsViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (StatsViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
