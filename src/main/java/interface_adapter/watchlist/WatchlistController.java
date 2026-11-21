package interface_adapter.watchlist;

import use_case.watchlist.WatchlistInputBoundary;

public class WatchlistController {
    private final WatchlistInputBoundary watchlistInteractor;

    public WatchlistController(WatchlistInputBoundary watchlistInteractor) {
        this.watchlistInteractor = watchlistInteractor;
    }

    public void loadWatchlist() {
        watchlistInteractor.loadWatchlist();
    }

    public void updateReview(int movieID, double newRating, String newReviewText, String newDateWatched) {
        watchlistInteractor.updateReview(movieID, newRating, newReviewText, newDateWatched);
    }

    public void deleteReview(int movieID) {
        watchlistInteractor.deleteReview(movieID);
    }
}
