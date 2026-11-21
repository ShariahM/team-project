package use_case.watchlist;

public interface WatchlistInputBoundary {
    void loadWatchlist();
    void updateReview(int movieID, double newRating, String newReviewText, String newDateWatched);
    void deleteReview(int movieID);
}
