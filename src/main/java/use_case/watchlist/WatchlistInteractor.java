package use_case.watchlist;

import entity.Review;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class WatchlistInteractor implements WatchlistInputBoundary {
    private final WatchlistDataAccessInterface dataAccess;
    private final WatchlistOutputBoundary presenter;

    public WatchlistInteractor(WatchlistDataAccessInterface dataAccess, WatchlistOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void loadWatchlist() {
        final List<Review> reviews = dataAccess.getAllReviews();
        presenter.prepareSuccessView(new WatchlistOutputData(reviews, ""));
    }

    @Override
    public void updateReview(int movieID, double newRating, String newReviewText, String newDateWatched) {
        final Review existing = dataAccess.getReview(movieID);
        if (existing == null) {
            presenter.prepareFailView("Movie not found in watchlist.");
            return;
        }

        if (newRating < 0.0 || newRating > 5.0) {
            presenter.prepareFailView("Rating must be between 0.0 and 5.0.");
            return;
        }

        LocalDate parsedDate = null;
        if (newDateWatched != null && !newDateWatched.isBlank()) {
            try {
                parsedDate = LocalDate.parse(newDateWatched.trim());
            } catch (DateTimeParseException e) {
                presenter.prepareFailView("Invalid date format. Use YYYY-MM-DD.");
                return;
            }
        }

        existing.updateRating(newRating);
        existing.setReview(newReviewText == null ? "" : newReviewText.trim());
        existing.setDateWatched(parsedDate);

        dataAccess.saveReview(existing);
        presenter.prepareSuccessView(new WatchlistOutputData(dataAccess.getAllReviews(), "Entry updated successfully."));
    }

    @Override
    public void deleteReview(int movieID) {
        if (!dataAccess.existsById(movieID)) {
            presenter.prepareFailView("Movie not found in watchlist.");
            return;
        }

        dataAccess.removeReview(movieID);
        presenter.prepareSuccessView(new WatchlistOutputData(dataAccess.getAllReviews(), "Entry removed from watchlist."));
    }
}
