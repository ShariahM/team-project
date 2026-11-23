package use_case.movie_detail;

import java.util.List;

public class MovieDetailOutputData {
    private final String posterURL;
    private final String title;
    private final int year;
    private final List<String> genres;
    private final String director;
    private final List<String> cast;
    private final boolean watched;
    private final Double rating;

    public MovieDetailOutputData(String posterURL, String title, int year,
                                List<String> genres, String director, List<String> cast,
                                boolean watched, Double rating) {
        this.posterURL = posterURL;
        this.title = title;
        this.year = year;
        this.genres = genres == null ? List.of() : List.copyOf(genres);
        this.director = director;
        this.cast = cast == null ? List.of() : List.copyOf(cast);
        this.watched = watched;
        this.rating = rating;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public List<String> getGenres() {
        return genres;
    }

    public String getDirector() {
        return director;
    }

    public List<String> getCast() {
        return cast;
    }

    public boolean isWatched() {
        return watched;
    }

    public Double getRating() {
        return rating;
    }
}
