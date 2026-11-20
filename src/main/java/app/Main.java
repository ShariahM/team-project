package app;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();
        final JFrame application = appBuilder
                .addWatchlistView()
                .addWatchlistUseCase()
                // .addSearchView()
                // .addSearchUseCase()
                // .addMovieDetailView()
                // .addMovieDetailUseCase()
                // .addExploreView()
                // .addExploreUseCase()
                .addStatsView()
                .addStatsUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
