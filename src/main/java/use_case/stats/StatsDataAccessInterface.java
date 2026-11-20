package use_case.stats;

import entity.Review;

import java.util.List;

/**
 * What the stats use case needs from data storage: every rated/watched movie the user has saved.
 */
public interface StatsDataAccessInterface {
    List<Review> getAllReviews();
}
