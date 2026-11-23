package app;

import javax.swing.JFrame;

public class Main {
    /**
     * Creates and displays the movie-search application.
     *
     * @param args command-line arguments; unused
     */
    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();
        final JFrame application = appBuilder
                .addExploreView()
                .addExploreUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
