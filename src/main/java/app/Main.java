package app;

import interface_adapter.MovieDetailState.MovieDetailState;
import interface_adapter.MovieDetailState.MovieDetailViewModel;
import interface_adapter.watchlist.WatchlistViewModel;

import javax.swing.JFrame;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();
        String initialView = WatchlistViewModel.VIEW_NAME;
        if (args.length > 0 && "--movie-details".equals(args[0])) {
            MovieDetailState sample = new MovieDetailState();
            sample.setTitle("The Last Lighthouse");
            sample.setYear(2024);
            sample.setGenres(List.of("Drama"));
            sample.setDirector("Jamie Chen");
            sample.setCast(List.of("Alex Rivera"));
            sample.setWatched(true);
            sample.setRating(4.0);
            appBuilder.addMovieDetailView(sample);
            initialView = MovieDetailViewModel.VIEW_NAME;
        }

        final JFrame application = appBuilder
                .addWatchlistView()
                .addWatchlistUseCase()
                // .addSearchView()
                // .addSearchUseCase()
                // .addMovieDetailView()
                // .addMovieDetailUseCase()
                // .addExploreView()
                // .addExploreUseCase()
                // .addStatsView()
                // .addStatsUseCase()
                .build(initialView);

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
