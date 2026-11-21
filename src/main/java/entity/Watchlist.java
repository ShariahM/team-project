package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Watchlist {
    private final List<Review> reviews;

    public Watchlist() {
        reviews = new ArrayList<>();
    }

    public void addReview(Review review) {
        for (int i = 0; i < reviews.size(); i++) {
            if (reviews.get(i).getMovie().getMovieID() == review.getMovie().getMovieID()) {
                reviews.set(i, review);
                return;
            }
        }
        reviews.add(review);
    }

    public void removeReview(Review review) {
        if (review != null) {
            removeReview(review.getMovie().getMovieID());
        }
    }

    public void removeReview(int movieID) {
        reviews.removeIf(r -> r.getMovie().getMovieID() == movieID);
    }

    public Review getReview(Movie movie) {
        if (movie == null) {
            return null;
        }
        return getReview(movie.getMovieID());
    }

    public Review getReview(int movieID) {
        for (Review review : reviews) {
            if (review.getMovie().getMovieID() == movieID) {
                return review;
            }
        }
        return null;
    }

    public List<Review> getReviews() {
        return Collections.unmodifiableList(reviews);
    }
}
