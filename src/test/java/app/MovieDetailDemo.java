package app;

import interface_adapter.movie_detail.MovieDetailState;
import interface_adapter.movie_detail.MovieDetailViewModel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.util.List;

/** Standalone movie details preview using sample data. */
public final class MovieDetailDemo {
    private MovieDetailDemo() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame demo = new AppBuilder()
                    .addMovieDetailView(createSampleState())
                    .build(MovieDetailViewModel.VIEW_NAME);
            demo.pack();
            demo.setLocationRelativeTo(null);
            demo.setVisible(true);
        });
    }

    public static MovieDetailState createSampleState() {
        MovieDetailState sample = new MovieDetailState();
        sample.setTitle("The Last Lighthouse");
        sample.setYear(2024);
        sample.setGenres(List.of("Drama"));
        sample.setDirector("Jamie Chen");
        sample.setCast(List.of("Alex Rivera"));
        sample.setWatched(true);
        sample.setRating(4.0);
        return sample;
    }
}
