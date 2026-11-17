package entity;

import java.util.ArrayList;
import java.util.List;

public class Watchlist {
    private final List<Review> reviews;

    public Watchlist(){
        reviews = new ArrayList<>();
    }

    public void addReview(Review review) {
        reviews.add(review);
    }

    public void removeReview(Review review){
        reviews.remove(review);
    }

    public Review getReview(Movie movie){
        for(Review review : reviews){
            if (review.getMovie() == movie){
                return review;
            }
        }
        return null;
    }
}
