package data_access;

import entity.Movie;
import entity.Review;
import use_case.recommendations.RatedMoviesDataAccessInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Temporary stand-in so recommendations run before the real persistence layer lands.
 * Replace with the persistence DAO once it implements RatedMoviesDataAccessInterface.
 */
public class InMemoryRatedMoviesDataAccessObject implements RatedMoviesDataAccessInterface {
    private final List<Review> reviews = new ArrayList<>();

    public void addReview(Review review) {
        reviews.add(review);
    }

    @Override
    public List<Review> getAllReviews() {
        return new ArrayList<>(reviews);
    }

    /** Sample ratings of real TMDB movies (the IDs matter: they are sent to TMDB). */
    public static InMemoryRatedMoviesDataAccessObject withSampleRatings() {
        final InMemoryRatedMoviesDataAccessObject dao = new InMemoryRatedMoviesDataAccessObject();
        dao.addReview(new Review(new Movie(27205, "Inception", 2010, Set.of("Action", "Science Fiction"),
                "Christopher Nolan", "", ""), 5.0, "Mind-bending."));
        dao.addReview(new Review(new Movie(157336, "Interstellar", 2014, Set.of("Adventure", "Drama"),
                "Christopher Nolan", "", ""), 4.5, "Loved the score."));
        dao.addReview(new Review(new Movie(496243, "Parasite", 2019, Set.of("Thriller", "Drama"),
                "Bong Joon-ho", "", ""), 4.0, "Brilliant twist."));
        // Below the 4.0 threshold: not used as a seed, but still excluded from results.
        dao.addReview(new Review(new Movie(17473, "The Room", 2003, Set.of("Drama"),
                "Tommy Wiseau", "", ""), 1.5, "So bad it's good."));
        return dao;
    }
}
