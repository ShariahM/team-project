package use_case.watchlist;

import entity.Review;
import java.util.List;

public class WatchlistOutputData {
    private final List<Review> reviews;
    private final String message;

    public WatchlistOutputData(List<Review> reviews, String message) {
        this.reviews = reviews;
        this.message = message;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public String getMessage() {
        return message;
    }
}
