package interface_adapter.explore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;

public class ExploreState {
    private String query = "";
    private List<MovieResult> movies = Collections.emptyList();
    private String errorMessage;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query == null ? "" : query;
    }

    public List<MovieResult> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieResult> movies) {
        this.movies = movies == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<>(movies));
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static class MovieResult {
        private final int movieID;
        private final String title;
        private final int releaseYear;
        private final Set<String> genres;
        private final String director;
        private final String overview;
        private final String posterURL;

        public MovieResult(int movieID, String title, int releaseYear, Set<String> genres,
                           String director, String overview, String posterURL) {
            this.movieID = movieID;
            this.title = title;
            this.releaseYear = releaseYear;
            this.genres = genres == null
                    ? Collections.emptySet()
                    : Collections.unmodifiableSet(new LinkedHashSet<>(genres));
            this.director = director;
            this.overview = overview;
            this.posterURL = posterURL;
        }

        public int getMovieID() {
            return movieID;
        }

        public String getTitle() {
            return title;
        }

        public int getReleaseYear() {
            return releaseYear;
        }

        public Set<String> getGenres() {
            return genres;
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
    }
}
