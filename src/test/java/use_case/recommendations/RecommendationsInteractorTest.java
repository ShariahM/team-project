package use_case.recommendations;

import entity.Movie;
import entity.Review;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecommendationsInteractorTest {

    /** Fake TMDB: returns a fixed list per seed movie ID and remembers which IDs were asked for. */
    private static class FakeRecommendations implements MovieRecommendationsDataAccessInterface {
        private final Map<Integer, List<Movie>> bySeed = new HashMap<>();
        private final List<Integer> requested = new ArrayList<>();

        FakeRecommendations add(int seedID, Movie... movies) {
            bySeed.put(seedID, List.of(movies));
            return this;
        }

        @Override
        public List<Movie> getRecommendationsFor(int movieID) {
            requested.add(movieID);
            return bySeed.getOrDefault(movieID, List.of());
        }
    }

    /** Fake presenter: records whichever view the interactor prepared. */
    private static class RecordingPresenter implements RecommendationsOutputBoundary {
        private RecommendationsOutputData success;
        private String error;

        @Override
        public void prepareSuccessView(RecommendationsOutputData outputData) {
            success = outputData;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            error = errorMessage;
        }
    }

    private static Movie movie(int id, String title) {
        return new Movie(id, title, 2000 + id % 20, Set.of(), "", "", "");
    }

    private static Review review(int id, String title, double rating) {
        return new Review(movie(id, title), rating, "");
    }

    private static RecordingPresenter run(List<Review> reviews, MovieRecommendationsDataAccessInterface tmdb) {
        final RecordingPresenter presenter = new RecordingPresenter();
        new RecommendationsInteractor(() -> reviews, tmdb, presenter).execute();
        return presenter;
    }

    private static List<String> titles(RecordingPresenter presenter) {
        final List<String> titles = new ArrayList<>();
        for (RecommendedMovie movie : presenter.success.getRecommendations()) {
            titles.add(movie.getTitle());
        }
        return titles;
    }

    @Test
    void noReviewsFails() {
        final RecordingPresenter presenter = run(List.of(), new FakeRecommendations());
        assertEquals(RecommendationsInteractor.NO_HIGH_RATINGS_MESSAGE, presenter.error);
        assertNull(presenter.success);
    }

    @Test
    void onlyLowRatingsFailsWithoutCallingTmdb() {
        final FakeRecommendations tmdb = new FakeRecommendations();
        final RecordingPresenter presenter = run(List.of(review(1, "Meh", 3.5)), tmdb);
        assertEquals(RecommendationsInteractor.NO_HIGH_RATINGS_MESSAGE, presenter.error);
        assertTrue(tmdb.requested.isEmpty());
    }

    @Test
    void ranksBySumOfSeedRatingsThenTitle() {
        final FakeRecommendations tmdb = new FakeRecommendations()
                .add(1, movie(10, "Shared"), movie(11, "Zeta"))
                .add(2, movie(10, "Shared"), movie(12, "Alpha"));
        final RecordingPresenter presenter = run(List.of(review(1, "Seed A", 5.0), review(2, "Seed B", 4.0)), tmdb);

        // Shared = 5 + 4 = 9; Zeta = 5; Alpha = 4.
        assertEquals(List.of("Shared", "Zeta", "Alpha"), titles(presenter));
        assertNull(presenter.error);
    }

    @Test
    void tiesKeepTmdbOrder() {
        final FakeRecommendations tmdb = new FakeRecommendations()
                .add(1, movie(10, "Zeta"), movie(11, "Alpha"), movie(12, "Mu"));
        final RecordingPresenter presenter = run(List.of(review(1, "Seed", 4.0)), tmdb);
        assertEquals(List.of("Zeta", "Alpha", "Mu"), titles(presenter));
    }

    @Test
    void tiesUseBestPositionAcrossSeeds() {
        // Zulu and Alpha both score 4 + 4 = 8. Zulu is 4th for seed 1 but 1st for seed 2 (best = 0);
        // Alpha is 2nd for both (best = 1). So Zulu wins even though Alpha comes first alphabetically.
        final FakeRecommendations tmdb = new FakeRecommendations()
                .add(1, movie(20, "Other A"), movie(11, "Alpha"), movie(21, "Other B"), movie(10, "Zulu"))
                .add(2, movie(10, "Zulu"), movie(11, "Alpha"));
        final RecordingPresenter presenter = run(List.of(review(1, "One", 4.0), review(2, "Two", 4.0)), tmdb);
        assertEquals(List.of("Zulu", "Alpha", "Other A", "Other B"), titles(presenter));
    }

    @Test
    void sameScoreAndPositionFallsBackToTitle() {
        final FakeRecommendations tmdb = new FakeRecommendations()
                .add(1, movie(10, "beta"))
                .add(2, movie(11, "Alpha"));
        final RecordingPresenter presenter = run(List.of(review(1, "One", 4.0), review(2, "Two", 4.0)), tmdb);
        assertEquals(List.of("Alpha", "beta"), titles(presenter));
    }

    @Test
    void excludesAlreadyRatedMoviesAndDuplicates() {
        final FakeRecommendations tmdb = new FakeRecommendations()
                .add(1, movie(2, "Low Rated"), movie(3, "Seed Two"), movie(10, "New"), movie(10, "New"));
        final RecordingPresenter presenter = run(List.of(
                review(1, "Seed One", 5.0), review(2, "Low Rated", 2.0), review(3, "Seed Two", 4.0)), tmdb);

        assertEquals(List.of("New"), titles(presenter));
        // Listed twice by the same seed, but only counted once.
        assertEquals(1, presenter.success.getRecommendations().size());
    }

    @Test
    void becauseYouLikedIsTheHighestRatedSeed() {
        final FakeRecommendations tmdb = new FakeRecommendations()
                .add(1, movie(10, "Shared"))
                .add(2, movie(10, "Shared"));
        final RecordingPresenter presenter = run(List.of(review(2, "Good", 4.0), review(1, "Favourite", 5.0)), tmdb);

        final RecommendedMovie result = presenter.success.getRecommendations().get(0);
        assertEquals("Favourite", result.getBecauseYouLiked());
        assertEquals(10, result.getMovieID());
        assertEquals(2010, result.getReleaseYear());
        assertEquals("", result.getPosterURL());
    }

    @Test
    void usesAtMostFiveSeedsBestRatedFirst() {
        final List<Review> reviews = new ArrayList<>();
        for (int id = 1; id <= 7; id++) {
            reviews.add(review(id, "Seed " + id, 4.0 + id * 0.1));
        }
        final FakeRecommendations tmdb = new FakeRecommendations();
        run(reviews, tmdb);
        assertEquals(List.of(7, 6, 5, 4, 3), tmdb.requested);
    }

    @Test
    void returnsAtMostTenResults() {
        final Movie[] many = new Movie[15];
        for (int i = 0; i < many.length; i++) {
            many[i] = movie(100 + i, String.format("Movie %02d", i));
        }
        final RecordingPresenter presenter = run(List.of(review(1, "Seed", 5.0)),
                new FakeRecommendations().add(1, many));
        assertEquals(RecommendationsInteractor.MAX_RESULTS, presenter.success.getRecommendations().size());
        assertEquals("Movie 00", titles(presenter).get(0));
    }

    @Test
    void failsWhenEveryRecommendationIsAlreadyRated() {
        final FakeRecommendations tmdb = new FakeRecommendations().add(1, movie(1, "Seed"));
        final RecordingPresenter presenter = run(List.of(review(1, "Seed", 5.0)), tmdb);
        assertEquals(RecommendationsInteractor.NO_NEW_MOVIES_MESSAGE, presenter.error);
        assertNull(presenter.success);
    }

    @Test
    void failsWhenTmdbThrows() {
        final MovieRecommendationsDataAccessInterface brokenTmdb = movieID -> {
            throw new IOException("HTTP 401");
        };
        final RecordingPresenter presenter = run(List.of(review(1, "Seed", 5.0)), brokenTmdb);
        assertEquals(RecommendationsInteractor.LOAD_FAILED_MESSAGE + "HTTP 401", presenter.error);
        assertNull(presenter.success);
    }
}
