package interface_adapter.watchlist;

import entity.Review;
import interface_adapter.ViewManagerModel;
import use_case.watchlist.WatchlistOutputBoundary;
import use_case.watchlist.WatchlistOutputData;

import java.util.ArrayList;
import java.util.List;

public class WatchlistPresenter implements WatchlistOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final WatchlistViewModel watchlistViewModel;

    public WatchlistPresenter(ViewManagerModel viewManagerModel, WatchlistViewModel watchlistViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.watchlistViewModel = watchlistViewModel;
    }

    @Override
    public void prepareSuccessView(WatchlistOutputData outputData) {
        final WatchlistState state = watchlistViewModel.getState();
        final List<WatchlistEntryState> entries = new ArrayList<>();
        for (Review review : outputData.getReviews()) {
            entries.add(new WatchlistEntryState(review));
        }
        state.setEntries(entries);
        state.setSuccessMessage(outputData.getMessage());
        state.setErrorMessage("");
        watchlistViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final WatchlistState state = watchlistViewModel.getState();
        state.setErrorMessage(error);
        state.setSuccessMessage("");
        watchlistViewModel.notifyStateChanged();
    }
}
