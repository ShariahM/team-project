package use_case.recommendations;

import entity.Movie;
import entity.Review;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Recommends movies based on the ones the user rated highly.
 *
 * <p>Each highly rated movie is a "seed". A candidate's score is the sum of the ratings of the
 * seeds that recommended it, so a movie recommended by several favourites ranks higher.
 */
public class RecommendationsInteractor implements RecommendationsInputBoundary {
    public static final double HIGH_RATING_THRESHOLD = 4.0;
    public static final int MAX_SEED_MOVIES = 5;
    public static final int MAX_RESULTS = 10;

    public static final String NO_HIGH_RATINGS_MESSAGE =
            "Rate some movies 4 stars or higher to get recommendations.";
    public static final String NO_NEW_MOVIES_MESSAGE =
            "No new recommendations found. Try rating more movies.";
    public static final String LOAD_FAILED_MESSAGE = "Could not load recommendations: ";

    private final RatedMoviesDataAccessInterface ratedMoviesDataAccess;
    private final MovieRecommendationsDataAccessInterface recommendationsDataAccess;
    private final RecommendationsOutputBoundary presenter;

    public RecommendationsInteractor(RatedMoviesDataAccessInterface ratedMoviesDataAccess,
                                     MovieRecommendationsDataAccessInterface recommendationsDataAccess,
                                     RecommendationsOutputBoundary presenter) {
        this.ratedMoviesDataAccess = ratedMoviesDataAccess;
        this.recommendationsDataAccess = recommendationsDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        final List<Review> reviews = ratedMoviesDataAccess.getAllReviews();

        final List<Review> seeds = pickSeeds(reviews);
        if (seeds.isEmpty()) {
            presenter.prepareFailView(NO_HIGH_RATINGS_MESSAGE);
            return;
        }

        final Set<Integer> alreadyRated = new HashSet<>();
        for (Review review : reviews) {
            alreadyRated.add(review.getMovie().getMovieID());
        }

        // Keyed by movie ID, so a movie recommended by several seeds is one candidate.
        final Map<Integer, Candidate> candidates = new LinkedHashMap<>();
        for (Review seed : seeds) {
            final List<Movie> recommended;
            try {
                recommended = recommendationsDataAccess.getRecommendationsFor(seed.getMovie().getMovieID());
            } catch (IOException exception) {
                presenter.prepareFailView(LOAD_FAILED_MESSAGE + exception.getMessage());
                return;
            }

            for (Movie movie : recommended) {
                if (alreadyRated.contains(movie.getMovieID())) {
                    continue;
                }
                candidates.computeIfAbsent(movie.getMovieID(), id -> new Candidate(movie)).addSeed(seed);
            }
        }

        final List<Candidate> ranked = new ArrayList<>(candidates.values());
        ranked.sort(Comparator.comparingDouble(Candidate::getScore).reversed()
                .thenComparing(candidate -> candidate.movie.getTitle(), String.CASE_INSENSITIVE_ORDER));

        final List<RecommendedMovie> results = new ArrayList<>();
        for (Candidate candidate : ranked.subList(0, Math.min(MAX_RESULTS, ranked.size()))) {
            final Movie movie = candidate.movie;
            results.add(new RecommendedMovie(movie.getMovieID(), movie.getTitle(), movie.getReleaseYear(),
                    movie.getPosterURL(), candidate.becauseYouLiked));
        }

        if (results.isEmpty()) {
            presenter.prepareFailView(NO_NEW_MOVIES_MESSAGE);
        } else {
            presenter.prepareSuccessView(new RecommendationsOutputData(results));
        }
    }

    /** The user's highly rated movies, best rated first, at most MAX_SEED_MOVIES of them. */
    private static List<Review> pickSeeds(List<Review> reviews) {
        final List<Review> seeds = new ArrayList<>();
        for (Review review : reviews) {
            if (review.getRating() >= HIGH_RATING_THRESHOLD) {
                seeds.add(review);
            }
        }
        seeds.sort(Comparator.comparingDouble(Review::getRating).reversed()
                .thenComparing(review -> review.getMovie().getTitle(), String.CASE_INSENSITIVE_ORDER));
        return seeds.subList(0, Math.min(MAX_SEED_MOVIES, seeds.size()));
    }

    /** A movie being considered, with its running score and the seeds that recommended it. */
    private static class Candidate {
        private final Movie movie;
        private final Set<Integer> seedIDs = new HashSet<>();
        private double score;
        private String becauseYouLiked;

        Candidate(Movie movie) {
            this.movie = movie;
        }

        void addSeed(Review seed) {
            // Ignore a seed listing the same movie twice.
            if (!seedIDs.add(seed.getMovie().getMovieID())) {
                return;
            }
            score += seed.getRating();
            // Seeds arrive best-rated first, so the first one is the best explanation.
            if (becauseYouLiked == null) {
                becauseYouLiked = seed.getMovie().getTitle();
            }
        }

        double getScore() {
            return score;
        }
    }
}
