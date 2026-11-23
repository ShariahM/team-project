package use_case.explore;

/**
 * Search input prepared by the controller for the explore use case.
 */
public class ExploreInputData {
    private final String query;

    /**
     * Creates search input for the supplied movie title.
     *
     * @param query movie title entered by the user; {@code null} is stored as an empty string
     */
    public ExploreInputData(String query) {
        this.query = query == null ? "" : query;
    }

    /**
     * Returns the movie title to search for.
     *
     * @return the search title
     */
    public String getQuery() {
        return query;
    }
}
