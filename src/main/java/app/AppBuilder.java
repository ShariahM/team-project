package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.MovieDetailState.MovieDetailState;
import interface_adapter.MovieDetailState.MovieDetailViewModel;
import interface_adapter.watchlist.WatchlistViewModel;
import view.MovieDetailView;
import view.ViewManager;
import view.WatchlistView;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.CardLayout;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private WatchlistView watchlistView;
    private WatchlistViewModel watchlistViewModel;

    // private SearchViewModel searchViewModel;
    // private SearchView searchView;

    // private MovieDetailViewModel movieDetailViewModel;
    // private MovieDetailView movieDetailView;

    // private ExploreViewModel exploreViewModel;
    // private ExploreView exploreView;

    // private StatsViewModel statsViewModel;
    // private StatsView statsView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addWatchlistView() {
        watchlistViewModel = new WatchlistViewModel();
        watchlistView = new WatchlistView(watchlistViewModel);
        cardPanel.add(watchlistView, WatchlistViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addWatchlistUseCase() {
        // final WatchlistOutputBoundary watchlistOutputBoundary = new WatchlistPresenter(viewManagerModel, watchlistViewModel);
        // final WatchlistInputBoundary watchlistInteractor = new WatchlistInteractor(dataAccessObject, watchlistOutputBoundary);
        // final WatchlistController watchlistController = new WatchlistController(watchlistInteractor);
        // watchlistView.setWatchlistController(watchlistController);
        return this;
    }

    public AppBuilder addSearchView() {
        // searchViewModel = new SearchViewModel();
        // searchView = new SearchView(searchViewModel);
        // cardPanel.add(searchView, SearchViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addSearchUseCase() {
        // final SearchOutputBoundary searchOutputBoundary = new SearchPresenter(viewManagerModel, searchViewModel);
        // final SearchInputBoundary searchInteractor = new SearchInteractor(dataAccessObject, searchOutputBoundary);
        // final SearchController searchController = new SearchController(searchInteractor);
        // searchView.setSearchController(searchController);
        return this;
    }

    public AppBuilder addMovieDetailView() {
        return addMovieDetailView(new MovieDetailState());
    }

    public AppBuilder addMovieDetailView(MovieDetailState state) {
        MovieDetailViewModel viewModel = new MovieDetailViewModel();
        viewModel.setState(state);
        MovieDetailView view = new MovieDetailView(viewModel);
        cardPanel.add(view, MovieDetailViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addMovieDetailUseCase() {
        // final MovieDetailOutputBoundary movieDetailOutputBoundary = new MovieDetailPresenter(viewManagerModel, movieDetailViewModel);
        // final MovieDetailInputBoundary movieDetailInteractor = new MovieDetailInteractor(dataAccessObject, movieDetailOutputBoundary);
        // final MovieDetailController movieDetailController = new MovieDetailController(movieDetailInteractor);
        // movieDetailView.setMovieDetailController(movieDetailController);
        return this;
    }

    public AppBuilder addExploreView() {
        // exploreViewModel = new ExploreViewModel();
        // exploreView = new ExploreView(exploreViewModel);
        // cardPanel.add(exploreView, ExploreViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addExploreUseCase() {
        // final ExploreOutputBoundary exploreOutputBoundary = new ExplorePresenter(viewManagerModel, exploreViewModel);
        // final ExploreInputBoundary exploreInteractor = new ExploreInteractor(dataAccessObject, exploreOutputBoundary);
        // final ExploreController exploreController = new ExploreController(exploreInteractor);
        // exploreView.setExploreController(exploreController);
        return this;
    }

    public AppBuilder addStatsView() {
        // statsViewModel = new StatsViewModel();
        // statsView = new StatsView(statsViewModel);
        // cardPanel.add(statsView, StatsViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addStatsUseCase() {
        // final StatsOutputBoundary statsOutputBoundary = new StatsPresenter(viewManagerModel, statsViewModel);
        // final StatsInputBoundary statsInteractor = new StatsInteractor(dataAccessObject, statsOutputBoundary);
        // final StatsController statsController = new StatsController(statsInteractor);
        // statsView.setStatsController(statsController);
        return this;
    }

    public JFrame build() {
        return build(WatchlistViewModel.VIEW_NAME);
    }

    public JFrame build(String initialView) {
        final JFrame application = new JFrame("Movie Diary");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(initialView);
        viewManagerModel.notifyViewChanged();

        return application;
    }
}
