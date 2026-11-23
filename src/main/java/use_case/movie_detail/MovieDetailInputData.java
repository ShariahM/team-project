package use_case.movie_detail;

public class MovieDetailInputData {
    private final int movieId;

    public MovieDetailInputData(int movieId) {
        this.movieId = movieId;
    }

    public int getMovieId() {
        return movieId;
    }
}
