package interface_adapter.explore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * UI state for the movie-search screen.
 */
public class ExploreState {
    private String query = "";
    private List<MovieResult> movies = Collections.emptyList();
    private String errorMessage;

    /**
     * Returns the title currently shown in the search field.
     *
     * @return search title
     */
    public String getQuery() {
        return query;
    }

    /**
     * Sets the title currently shown in the search field.
     *
     * @param query search title; {@code null} is stored as an empty string
     */
    public void setQuery(String query) {
        this.query = query == null ? "" : query;
    }

    /**
     * Returns the movie results currently displayed.
     *
     * @return an unmodifiable list of results
     */
    public List<MovieResult> getMovies() {
        return movies;
    }

    /**
     * Replaces the displayed movie results.
     *
     * @param movies results to display; {@code null} is treated as an empty list
     */
    public void setMovies(List<MovieResult> movies) {
        this.movies = movies == null
                ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<>(movies));
    }

    /**
     * Returns the current search error, if any.
     *
     * @return error message, or {@code null} when there is no error
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets the current search error.
     *
     * @param errorMessage message to display, or {@code null} to clear the error
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * Plain display values for one movie result.
     */
    public static class MovieResult {
        private final int movieID;
        private final String title;
        private final int releaseYear;
        private final Set<String> genres;
        private final String director;
        private final String overview;
        private final String posterURL;

        /**
         * Creates a result containing the fields displayed or used by the explore view.
         *
         * @param movieID movie identifier
         * @param title movie title
         * @param releaseYear release year, or zero when unknown
         * @param genres movie genres
         * @param director director name
         * @param overview plot overview
         * @param posterURL URL of the movie poster
         */
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
         * @return unmodifiable genre names
         */
        public Set<String> getGenres() {
            return genres;
        }

        /**
         * Returns the director name.
         *
         * @return director name
         */
        public String getDirector() {
            return director;
        }

        /**
         * Returns the movie overview.
         *
         * @return overview text
         */
        public String getOverview() {
            return overview;
        }

        /**
         * Returns the movie poster URL.
         *
         * @return poster URL
         */
        public String getPosterURL() {
            return posterURL;
        }
    }
}
