package use_case.explore;

/**
 * Search input prepared by the controller for the explore use case.
 */
public class ExploreInputData {
    private final String query;

    public ExploreInputData(String query) {
        this.query = query == null ? "" : query;
    }

    public String getQuery() {
        return query;
    }
}
