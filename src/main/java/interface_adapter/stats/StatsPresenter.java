package interface_adapter.stats;

import interface_adapter.ViewManagerModel;
import use_case.stats.StatsOutputBoundary;
import use_case.stats.StatsOutputData;

public class StatsPresenter implements StatsOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final StatsViewModel statsViewModel;

    public StatsPresenter(ViewManagerModel viewManagerModel, StatsViewModel statsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.statsViewModel = statsViewModel;
    }

    @Override
    public void prepareSuccessView(StatsOutputData outputData) {
        final StatsState state = statsViewModel.getState();
        state.setTotalMoviesWatched(outputData.getTotalMoviesWatched());
        state.setAverageRating(outputData.getAverageRating());
        state.setHighestRatedGenre(outputData.getHighestRatedGenre());
        state.setTotalMinutesWatched(outputData.getTotalMinutesWatched());
        state.setMinutesPerMonth(outputData.getMinutesPerMonth());
        statsViewModel.notifyStateChanged();

        viewManagerModel.setState(StatsViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }
}
