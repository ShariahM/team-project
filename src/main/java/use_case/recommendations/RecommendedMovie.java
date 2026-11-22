package use_case.recommendations;

/**
 * One recommendation as plain data, so the Movie entity does not leak into the outer layers.
 */
public class RecommendedMovie {
    private final int movieID;
    private final String title;
    private final int releaseYear;
    private final String posterURL;
    private final String becauseYouLiked;

    public RecommendedMovie(int movieID, String title, int releaseYear, String posterURL,
                            String becauseYouLiked) {
        this.movieID = movieID;
        this.title = title;
        this.releaseYear = releaseYear;
        this.posterURL = posterURL;
        this.becauseYouLiked = becauseYouLiked;
    }

    public int getMovieID() {
        return movieID;
    }

    public String getTitle() {
        return title;
    }

    /** Release year, or 0 if TMDB did not provide one. */
    public int getReleaseYear() {
        return releaseYear;
    }

    public String getPosterURL() {
        return posterURL;
    }

    /** Title of the highest-rated seed movie that led to this recommendation. */
    public String getBecauseYouLiked() {
        return becauseYouLiked;
    }
}
