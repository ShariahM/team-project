package interface_adapter.explore;

/**
 * Receives notifications when explore view-model state changes.
 */
public interface ExploreViewModelListener {
    /**
     * Called after the explore view-model state has been updated.
     */
    void stateChanged();
}
