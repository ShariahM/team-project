package interface_adapter.MovieDetailState;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailViewModel {
    public static final String VIEW_NAME = "movie details";

    private MovieDetailState state = new MovieDetailState();
    private final List<MovieDetailViewModelListener> listeners = new ArrayList<>();

    public MovieDetailState getState() {
        return state;
    }

    public void setState(MovieDetailState state) {
        this.state = state;
    }

    public void addListener(MovieDetailViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (MovieDetailViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
