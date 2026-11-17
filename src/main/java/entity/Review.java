package entity;

public class Review {
    private final Movie movie;
    private double rating;
    private String review;
    public Review(Movie movie, double rating, String review) {
        this.movie = movie;
        this.rating = rating;
        this.review = review;
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
