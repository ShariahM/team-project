package interface_adapter.stats;

import java.time.YearMonth;
import java.util.Map;
import java.util.TreeMap;

public class StatsState {
    private int totalMoviesWatched;
    private double averageRating;
    private String highestRatedGenre;
    private int totalMinutesWatched;
    private Map<YearMonth, Integer> minutesPerMonth = new TreeMap<>();

    public int getTotalMoviesWatched() {
        return totalMoviesWatched;
    }

    public void setTotalMoviesWatched(int totalMoviesWatched) {
        this.totalMoviesWatched = totalMoviesWatched;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public String getHighestRatedGenre() {
        return highestRatedGenre;
    }

    public void setHighestRatedGenre(String highestRatedGenre) {
        this.highestRatedGenre = highestRatedGenre;
    }

    public int getTotalMinutesWatched() {
        return totalMinutesWatched;
    }

    public void setTotalMinutesWatched(int totalMinutesWatched) {
        this.totalMinutesWatched = totalMinutesWatched;
    }

    public Map<YearMonth, Integer> getMinutesPerMonth() {
        return minutesPerMonth;
    }

    public void setMinutesPerMonth(Map<YearMonth, Integer> minutesPerMonth) {
        this.minutesPerMonth = minutesPerMonth;
    }
}
