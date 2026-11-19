package interface_adapter.watchlist;

import java.util.ArrayList;
import java.util.List;

public class WatchlistViewModel {

    public static final String TITLE_LABEL = "Movie Diary - Watchlist";
    public static final String VIEW_NAME = "watchlist";

    private final List<WatchlistViewModelListener> listeners = new ArrayList<>();

    private WatchlistState state = new WatchlistState();

    public WatchlistState getState() {
        return this.state;
    }

    public void setState(WatchlistState state) {
        this.state = state;
    }

    public void addListener(WatchlistViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (WatchlistViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
