package use_case.recommendations;

import entity.Review;

import java.util.List;

/**
 * What the recommendations use case needs from the user's saved data: every movie they rated.
 * Same method as StatsDataAccessInterface, so the persistence DAO can implement both.
 */
public interface RatedMoviesDataAccessInterface {
    List<Review> getAllReviews();
}
