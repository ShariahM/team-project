package use_case.watchlist;

import entity.Review;
import java.util.List;

public interface WatchlistDataAccessInterface {
    void saveReview(Review review);
    void removeReview(int movieID);
    Review getReview(int movieID);
    List<Review> getAllReviews();
    boolean existsById(int movieID);
}
