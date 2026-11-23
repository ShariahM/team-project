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

    /**
     * Creates output for a movie search.
     *
     * @param query title that was searched for
     * @param movies matching movie details; {@code null} is treated as an empty list
     */
    public ExploreOutputData(String query, List<MovieData> movies) {
        this.query = query == null ? "" : query.trim();
        if (movies == null) {
            this.movies = Collections.emptyList();
        }
        else {
            this.movies = Collections.unmodifiableList(new ArrayList<>(movies));
        }
    }

    /**
     * Returns the title used for the search.
     *
     * @return trimmed search title, or an empty string
     */
    public String getQuery() {
        return query;
    }

    /**
     * Returns the details of movies found by the search.
     *
     * @return an unmodifiable list of movie details
     */
    public List<MovieData> getMovies() {
        return movies;
    }

    /**
     * Plain movie details transferred from the use case to the presenter.
     */
    public static class MovieData {
        private final int movieID;
        private final String title;
        private final int releaseYear;
        private final Set<String> genres;
        private final String director;
        private final String overview;
        private final String posterURL;

        /**
         * Creates display-independent data for one search result.
         *
         * @param movieID movie identifier
         * @param title movie title
         * @param releaseYear release year, or zero when unknown
         * @param genres movie genres
         * @param director director name
         * @param overview plot overview
         * @param posterURL URL of the movie poster
         */
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

        /**
         * Returns the movie identifier.
         *
         * @return movie identifier
         */
        public int getMovieID() {
            return movieID;
        }

        /**
         * Returns the movie title.
         *
         * @return movie title
         */
        public String getTitle() {
            return title;
        }

        /**
         * Returns the movie release year.
         *
         * @return release year, or zero when unknown
         */
        public int getReleaseYear() {
            return releaseYear;
        }

        /**
         * Returns the movie genres.
         *
         * @return an unmodifiable set of genre names
         */
        public Set<String> getGenres() {
            return genres;
        }

        /**
         * Returns the director name.
         *
         * @return director name, or an empty string when unavailable
         */
        public String getDirector() {
            return director;
        }

        /**
         * Returns the movie overview.
         *
         * @return overview, or an empty string when unavailable
         */
        public String getOverview() {
            return overview;
        }

        /**
         * Returns the movie poster URL.
         *
         * @return poster URL, or an empty string when unavailable
         */
        public String getPosterURL() {
            return posterURL;
        }
    }
}
