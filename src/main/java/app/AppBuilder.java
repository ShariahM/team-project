package app;

import data_access.JsonMovieDataAccessObject;
import entity.Movie;
import entity.Review;
import interface_adapter.ViewManagerModel;
import interface_adapter.watchlist.WatchlistController;
import interface_adapter.watchlist.WatchlistPresenter;
import interface_adapter.watchlist.WatchlistViewModel;
import use_case.watchlist.WatchlistDataAccessInterface;
import use_case.watchlist.WatchlistInputBoundary;
import use_case.watchlist.WatchlistInteractor;
import use_case.watchlist.WatchlistOutputBoundary;
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
    private WatchlistController watchlistController;

    private final WatchlistDataAccessInterface movieDataAccessObject = initDataAccessObject();

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

    private static WatchlistDataAccessInterface initDataAccessObject() {
        final JsonMovieDataAccessObject dao = new JsonMovieDataAccessObject("data/movies.json");
        if (dao.getAllReviews().isEmpty()) {
            dao.saveReview(new Review(
                    new Movie(1, "Inception", 2010, Set.of("Sci-Fi", "Action"), "Christopher Nolan",
                            "A thief who steals corporate secrets through dream-sharing technology.", "", 148),
                    4.5, "Mind-bending thriller with incredible visuals and score.", LocalDate.of(2026, 10, 3)
            ));
            dao.saveReview(new Review(
                    new Movie(2, "Parasite", 2019, Set.of("Thriller", "Drama"), "Bong Joon-ho",
                            "Greed and class discrimination threaten the relationship between wealthy and destitute families.", "", 132),
                    5.0, "Masterpiece in storytelling and social commentary.", LocalDate.of(2026, 10, 20)
            ));
            dao.saveReview(new Review(
                    new Movie(3, "Paddington 2", 2017, Set.of("Family", "Comedy"), "Paul King",
                            "Paddington picks up odd jobs to buy a pop-up book, which gets stolen.", "", 103),
                    4.0, "Pure joy from start to finish.", LocalDate.of(2026, 11, 2)
            ));
        }
        return dao;
    }

    public AppBuilder addWatchlistView() {
        watchlistViewModel = new WatchlistViewModel();
        watchlistView = new WatchlistView(watchlistViewModel);
        cardPanel.add(watchlistView, WatchlistViewModel.VIEW_NAME);
        return this;
    }

    public AppBuilder addWatchlistUseCase() {
        final WatchlistOutputBoundary watchlistOutputBoundary = new WatchlistPresenter(viewManagerModel, watchlistViewModel);
        final WatchlistInputBoundary watchlistInteractor = new WatchlistInteractor(movieDataAccessObject, watchlistOutputBoundary);
        watchlistController = new WatchlistController(watchlistInteractor);
        watchlistView.setWatchlistController(watchlistController);
        watchlistController.loadWatchlist();
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

    public JFrame build() {
        final JFrame application = new JFrame("Movie Diary");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        viewManagerModel.setState(WatchlistViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();

        return application;
    }
}
