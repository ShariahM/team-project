package app;

import data_access.InMemoryRatedMoviesDataAccessObject;
import data_access.TmdbRecommendationsDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.recommendations.RecommendationsController;
import interface_adapter.recommendations.RecommendationsPresenter;
import interface_adapter.recommendations.RecommendationsViewModel;
import interface_adapter.watchlist.WatchlistViewModel;
import use_case.recommendations.RecommendationsInteractor;
import view.RecommendationsView;
import view.ViewManager;
import view.WatchlistView;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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

    private RecommendationsViewModel recommendationsViewModel;
    private RecommendationsView recommendationsView;

    private final JMenu goToMenu = new JMenu("Go to");

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
        // movieDetailViewModel = new MovieDetailViewModel();
        // movieDetailView = new MovieDetailView(movieDetailViewModel);
        // cardPanel.add(movieDetailView, MovieDetailViewModel.VIEW_NAME);
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

    public AppBuilder addRecommendationsView() {
        recommendationsViewModel = new RecommendationsViewModel();
        recommendationsView = new RecommendationsView(recommendationsViewModel);
        cardPanel.add(recommendationsView, RecommendationsViewModel.VIEW_NAME);
        addGoToMenuItem("Recommendations", RecommendationsViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addRecommendationsUseCase() {
        // TODO: swap the in-memory ratings for the persistence DAO once it implements RatedMoviesDataAccessInterface
        final RecommendationsInteractor recommendationsInteractor = new RecommendationsInteractor(
                InMemoryRatedMoviesDataAccessObject.withSampleRatings(),
                new TmdbRecommendationsDataAccessObject(),
                new RecommendationsPresenter(viewManagerModel, recommendationsViewModel));
        recommendationsView.setRecommendationsController(new RecommendationsController(recommendationsInteractor));
        return this;
    }

    /** Adds a "Go to" menu entry that switches to the given view. */
    private void addGoToMenuItem(String label, String viewName) {
        final JMenuItem item = new JMenuItem(label);
        item.addActionListener(event -> {
            viewManagerModel.setState(viewName);
            viewManagerModel.notifyViewChanged();
        });
        goToMenu.add(item);
    }

    public JFrame build() {
        final JFrame application = new JFrame("Movie Diary");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        addGoToMenuItem("Watchlist", WatchlistViewModel.VIEW_NAME);
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(goToMenu);
        application.setJMenuBar(menuBar);

        application.add(cardPanel);

        viewManagerModel.setState(WatchlistViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        return application;
    }
}
