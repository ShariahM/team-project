package use_case.stats;

import entity.Review;
import java.util.List;

public interface StatsDataAccessInterface {
    List<Review> getAllReviews();
}
