package use_case.stats;

import java.time.YearMonth;
import java.util.Map;

public class StatsOutputData {
    private final int totalMoviesWatched;
    private final double averageRating;
    private final String highestRatedGenre;
    private final int totalMinutesWatched;
    private final Map<YearMonth, Integer> minutesPerMonth;

    public StatsOutputData(int totalMoviesWatched, double averageRating, String highestRatedGenre,
                           int totalMinutesWatched, Map<YearMonth, Integer> minutesPerMonth) {
        this.totalMoviesWatched = totalMoviesWatched;
        this.averageRating = averageRating;
        this.highestRatedGenre = highestRatedGenre;
        this.totalMinutesWatched = totalMinutesWatched;
        this.minutesPerMonth = minutesPerMonth;
    }

    public int getTotalMoviesWatched() {
        return totalMoviesWatched;
    }

    public double getAverageRating() {
        return averageRating;
    }

    /** Null when there are no reviews with genres. */
    public String getHighestRatedGenre() {
        return highestRatedGenre;
    }

    public int getTotalMinutesWatched() {
        return totalMinutesWatched;
    }

    /** Sorted by month, oldest first. */
    public Map<YearMonth, Integer> getMinutesPerMonth() {
        return minutesPerMonth;
    }
}
