package app;

import data_access.InMemoryStatsDataAccessObject;
import entity.Movie;
import entity.Review;
import interface_adapter.ViewManagerModel;
import interface_adapter.stats.StatsController;
import interface_adapter.stats.StatsPresenter;
import interface_adapter.stats.StatsViewModel;
import interface_adapter.watchlist.WatchlistViewModel;
import use_case.stats.StatsDataAccessInterface;
import use_case.stats.StatsInputBoundary;
import use_case.stats.StatsInteractor;
import use_case.stats.StatsOutputBoundary;
import view.StatsView;
import view.ViewManager;
import view.WatchlistView;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.CardLayout;
import java.time.LocalDate;
import java.util.Set;

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

    private StatsViewModel statsViewModel;
    private StatsView statsView;
    // TODO: swap for the real persistence DAO once it implements StatsDataAccessInterface
    private final StatsDataAccessInterface statsDataAccessObject = createSampleStatsData();

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
        statsViewModel = new StatsViewModel();
        statsView = new StatsView(statsViewModel);
        cardPanel.add(statsView, StatsViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addStatsUseCase() {
        final StatsOutputBoundary statsOutputBoundary = new StatsPresenter(viewManagerModel, statsViewModel);
        final StatsInputBoundary statsInteractor = new StatsInteractor(statsDataAccessObject, statsOutputBoundary);
        final StatsController statsController = new StatsController(statsInteractor);
        statsView.setStatsController(statsController);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Movie Diary");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(WatchlistViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        return application;
    }

    private static StatsDataAccessInterface createSampleStatsData() {
        final InMemoryStatsDataAccessObject dao = new InMemoryStatsDataAccessObject();
        dao.addReview(new Review(new Movie(1, "Inception", 2010, Set.of("Sci-Fi", "Action"), "Christopher Nolan", "", "", 148),
                4.5, "Great", LocalDate.of(2026, 10, 3)));
        dao.addReview(new Review(new Movie(2, "Parasite", 2019, Set.of("Thriller", "Drama"), "Bong Joon-ho", "", "", 132),
                5.0, "Perfect", LocalDate.of(2026, 10, 20)));
        dao.addReview(new Review(new Movie(3, "Paddington 2", 2017, Set.of("Family", "Comedy"), "Paul King", "", "", 103),
                4.0, "Lovely", LocalDate.of(2026, 11, 2)));
        return dao;
    }
}
