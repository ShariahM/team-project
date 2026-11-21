package entity;

import java.util.Set;

public class Movie {
    private final int movieID;
    private final String title;
    private final int releaseYear;
    private final Set<String> genres;
    private final String director;
    private final String overview;
    private final String posterURL;
    private final int runtimeMinutes;

    public Movie(int movieID, String title, int releaseYear, Set<String> genres, String director, String overview, String posterURL) {
        this(movieID, title, releaseYear, genres, director, overview, posterURL, 0);
    }

    public Movie(int movieID, String title, int releaseYear, Set<String> genres, String director, String overview, String posterURL, int runtimeMinutes) {
        this.movieID = movieID;
        this.title = title;
        this.releaseYear = releaseYear;
        this.genres = genres;
        this.director = director;
        this.overview = overview;
        this.posterURL = posterURL;
        this.runtimeMinutes = runtimeMinutes;
    }

    public int getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public int getMovieID() {
        return movieID;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public String getDirector() {
        return director;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public String getTitle() {
        return title;
    }

    public Set<String> getGenres() {
        return genres;
    }
}
