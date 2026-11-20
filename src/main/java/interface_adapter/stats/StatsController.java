package interface_adapter.stats;

import use_case.stats.StatsInputBoundary;

public class StatsController {
    private final StatsInputBoundary statsInteractor;

    public StatsController(StatsInputBoundary statsInteractor) {
        this.statsInteractor = statsInteractor;
    }

    public void execute() {
        statsInteractor.execute();
    }
}
