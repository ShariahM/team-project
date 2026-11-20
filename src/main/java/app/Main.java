package app;

import entity.Movie;
import interface_adapter.watchlist.WatchlistViewModel;
import view.MovieDetailView;

import javax.swing.JFrame;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();
        String initialView = WatchlistViewModel.VIEW_NAME;
        if (args.length > 0 && "--movie-details".equals(args[0])) {
            Movie sample = new Movie(1, "The Last Lighthouse", 2024, Set.of("Drama"), "Jamie Chen", "", "");
            appBuilder.addMovieDetailView(new MovieDetailView(sample, null, List.of("Alex Rivera"), true, 4.0));
            initialView = MovieDetailView.VIEW_NAME;
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
