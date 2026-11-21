package interface_adapter.watchlist;

import entity.Review;

public class WatchlistEntryState {
    private final int movieId;
    private final String title;
    private final int releaseYear;
    private final String director;
    private final String posterURL;
    private double rating;
    private String reviewText;
    private String dateWatched;

    public WatchlistEntryState(Review review) {
        this.movieId = review.getMovie().getMovieID();
        this.title = review.getMovie().getTitle();
        this.releaseYear = review.getMovie().getReleaseYear();
        this.director = review.getMovie().getDirector();
        this.posterURL = review.getMovie().getPosterURL();
        this.rating = review.getRating();
        this.reviewText = review.getReview() == null ? "" : review.getReview();
        this.dateWatched = review.getDateWatched() == null ? "" : review.getDateWatched().toString();
    }

    public int getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getDirector() {
        return director;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getDateWatched() {
        return dateWatched;
    }

    public void setDateWatched(String dateWatched) {
        this.dateWatched = dateWatched;
    }
}
