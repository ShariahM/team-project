package use_case.explore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Data returned after a movie title search.
 */
public class ExploreOutputData {
    private final String query;
    private final List<MovieData> movies;

    public ExploreOutputData(String query, List<MovieData> movies) {
        this.query = query == null ? "" : query.trim();
        if (movies == null) {
            this.movies = Collections.emptyList();
        }
        else {
            this.movies = Collections.unmodifiableList(new ArrayList<>(movies));
        }
    }

    public String getQuery() {
        return query;
    }

    public List<MovieData> getMovies() {
        return movies;
    }

    public static class MovieData {
        private final int movieID;
        private final String title;
        private final int releaseYear;
        private final Set<String> genres;
        private final String director;
        private final String overview;
        private final String posterURL;

        public MovieData(int movieID, String title, int releaseYear, Set<String> genres,
                         String director, String overview, String posterURL) {
            this.movieID = movieID;
            this.title = title == null ? "" : title;
            this.releaseYear = releaseYear;
            this.genres = genres == null
                    ? Collections.emptySet()
                    : Collections.unmodifiableSet(new LinkedHashSet<>(genres));
            this.director = director == null ? "" : director;
            this.overview = overview == null ? "" : overview;
            this.posterURL = posterURL == null ? "" : posterURL;
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
