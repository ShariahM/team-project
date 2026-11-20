package data_access;

import entity.Review;
import use_case.stats.StatsDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Temporary stand-in so the stats feature runs before the real persistence layer lands.
 * Replace with the persistence DAO once it implements StatsDataAccessInterface.
 */
public class InMemoryStatsDataAccessObject implements StatsDataAccessInterface {
    private final List<Review> reviews = new ArrayList<>();

    public void addReview(Review review) {
        reviews.add(review);
    }

    @Override
    public List<Review> getAllReviews() {
        return new ArrayList<>(reviews);
    }
}
