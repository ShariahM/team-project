package use_case.stats;

import entity.Movie;
import entity.Review;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class StatsInteractor implements StatsInputBoundary {
    private final StatsDataAccessInterface dataAccess;
    private final StatsOutputBoundary presenter;

    public StatsInteractor(StatsDataAccessInterface dataAccess, StatsOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        final List<Review> reviews = dataAccess.getAllReviews();

        double ratingSum = 0;
        int totalMinutes = 0;
        final Map<YearMonth, Integer> minutesPerMonth = new TreeMap<>();
        final Map<String, double[]> genreTotals = new HashMap<>();

        for (Review review : reviews) {
            final Movie movie = review.getMovie();
            ratingSum += review.getRating();
            totalMinutes += movie.getRuntimeMinutes();

            if (review.getDateWatched() != null) {
                minutesPerMonth.merge(YearMonth.from(review.getDateWatched()), movie.getRuntimeMinutes(), Integer::sum);
            }

            if (movie.getGenres() != null) {
                for (String genre : movie.getGenres()) {
                    final double[] totals = genreTotals.computeIfAbsent(genre, g -> new double[2]);
                    totals[0] += review.getRating();
                    totals[1] += 1;
                }
            }
        }

        final double averageRating = reviews.isEmpty() ? 0 : ratingSum / reviews.size();
        presenter.prepareSuccessView(new StatsOutputData(
                reviews.size(), averageRating, highestRatedGenre(genreTotals), totalMinutes, minutesPerMonth));
    }

    /** Highest average rating wins; ties go to the genre with more movies, then alphabetical. */
    private static String highestRatedGenre(Map<String, double[]> genreTotals) {
        String best = null;
        double bestAvg = 0;
        double bestCount = 0;
        for (Map.Entry<String, double[]> entry : genreTotals.entrySet()) {
            final double avg = entry.getValue()[0] / entry.getValue()[1];
            final double count = entry.getValue()[1];
            final boolean better = best == null
                    || avg > bestAvg
                    || (avg == bestAvg && count > bestCount)
                    || (avg == bestAvg && count == bestCount && entry.getKey().compareTo(best) < 0);
            if (better) {
                best = entry.getKey();
                bestAvg = avg;
                bestCount = count;
            }
        }
        return best;
    }
}
