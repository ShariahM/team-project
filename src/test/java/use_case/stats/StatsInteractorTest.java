package use_case.stats;

import entity.Movie;
import entity.Review;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class StatsInteractorTest {

    private StatsOutputData run(List<Review> reviews) {
        final StatsOutputData[] result = new StatsOutputData[1];
        new StatsInteractor(() -> reviews, data -> result[0] = data).execute();
        return result[0];
    }

    private Review review(int id, Set<String> genres, int runtime, double rating, LocalDate date) {
        return new Review(new Movie(id, "Movie " + id, 2020, genres, "Dir", "", "", runtime), rating, "", date);
    }

    @Test
    void emptyDiary() {
        final StatsOutputData data = run(new ArrayList<>());
        assertEquals(0, data.getTotalMoviesWatched());
        assertEquals(0.0, data.getAverageRating());
        assertNull(data.getHighestRatedGenre());
        assertEquals(0, data.getTotalMinutesWatched());
        assertEquals(0, data.getMinutesPerMonth().size());
    }

    @Test
    void totalsAndAverage() {
        final StatsOutputData data = run(List.of(
                review(1, Set.of("Drama"), 100, 4.0, LocalDate.of(2026, 1, 5)),
                review(2, Set.of("Drama"), 120, 5.0, LocalDate.of(2026, 1, 20))));
        assertEquals(2, data.getTotalMoviesWatched());
        assertEquals(4.5, data.getAverageRating(), 1e-9);
        assertEquals(220, data.getTotalMinutesWatched());
    }

    @Test
    void minutesPerMonthIsGroupedAndSorted() {
        final StatsOutputData data = run(List.of(
                review(1, Set.of("Drama"), 100, 4.0, LocalDate.of(2026, 3, 5)),
                review(2, Set.of("Drama"), 90, 4.0, LocalDate.of(2026, 1, 20)),
                review(3, Set.of("Drama"), 80, 4.0, LocalDate.of(2026, 3, 25)),
                review(4, Set.of("Drama"), 70, 4.0, null)));
        assertEquals(List.of(YearMonth.of(2026, 1), YearMonth.of(2026, 3)),
                new ArrayList<>(data.getMinutesPerMonth().keySet()));
        assertEquals(90, data.getMinutesPerMonth().get(YearMonth.of(2026, 1)));
        assertEquals(180, data.getMinutesPerMonth().get(YearMonth.of(2026, 3)));
        assertEquals(340, data.getTotalMinutesWatched());
    }

    @Test
    void highestRatedGenreUsesAverageNotCount() {
        final StatsOutputData data = run(List.of(
                review(1, Set.of("Action"), 100, 3.0, null),
                review(2, Set.of("Action"), 100, 3.0, null),
                review(3, Set.of("Action"), 100, 3.0, null),
                review(4, Set.of("Drama"), 100, 5.0, null)));
        assertEquals("Drama", data.getHighestRatedGenre());
    }

    @Test
    void genreTieGoesToMoreMoviesThenAlphabetical() {
        final StatsOutputData moreMovies = run(List.of(
                review(1, Set.of("Comedy"), 100, 4.0, null),
                review(2, Set.of("Drama"), 100, 4.0, null),
                review(3, Set.of("Drama"), 100, 4.0, null)));
        assertEquals("Drama", moreMovies.getHighestRatedGenre());

        final StatsOutputData alphabetical = run(List.of(
                review(1, Set.of("Drama"), 100, 4.0, null),
                review(2, Set.of("Comedy"), 100, 4.0, null)));
        assertEquals("Comedy", alphabetical.getHighestRatedGenre());
    }

    @Test
    void multiGenreMovieCountsForEveryGenre() {
        final StatsOutputData data = run(List.of(
                review(1, Set.of("Sci-Fi", "Action"), 100, 5.0, null),
                review(2, Set.of("Action"), 100, 1.0, null)));
        assertEquals("Sci-Fi", data.getHighestRatedGenre());
    }
}
