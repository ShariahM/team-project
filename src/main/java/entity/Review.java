package entity;

import java.time.LocalDate;

public class Review {
    private final Movie movie;
    private double rating;
    private String review;
    private LocalDate dateWatched;

    public Review(Movie movie, double rating, String review) {
        this(movie, rating, review, null);
    }

    public Review(Movie movie, double rating, String review, LocalDate dateWatched) {
        this.movie = movie;
        this.rating = rating;
        this.review = review;
        this.dateWatched = dateWatched;
    }

    /** Date the movie was watched, or null if not recorded. */
    public LocalDate getDateWatched() {
        return dateWatched;
    }

    public void setDateWatched(LocalDate dateWatched) {
        this.dateWatched = dateWatched;
    }

    public double getRating() {
        return rating;
    }

    public void updateRating(double rating){
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public Movie getMovie() {
        return movie;
    }
}
