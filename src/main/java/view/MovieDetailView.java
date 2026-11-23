package view;

import interface_adapter.MovieDetailState.MovieDetailState;
import interface_adapter.MovieDetailState.MovieDetailViewModel;
import interface_adapter.MovieDetailState.MovieDetailViewModelListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;

public class MovieDetailView extends JPanel implements MovieDetailViewModelListener {
    private final MovieDetailViewModel viewModel;
    private final JLabel title = new JLabel();
    private final JLabel year = new JLabel();
    private final JLabel genres = new JLabel();
    private final JLabel director = new JLabel();
    private final JLabel cast = new JLabel();
    private final JLabel watched = new JLabel();
    private final JLabel rating = new JLabel();

    public MovieDetailView(MovieDetailViewModel viewModel) {
        super(new BorderLayout(20, 0));
        this.viewModel = viewModel;
        viewModel.addListener(this);
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        add(new JLabel("Poster unavailable"), BorderLayout.WEST);

        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        details.add(title);
        details.add(year);
        details.add(genres);
        details.add(director);
        details.add(cast);
        details.add(watched);
        details.add(rating);
        add(details, BorderLayout.CENTER);
        stateChanged();
    }

    @Override
    public void stateChanged() {
        MovieDetailState state = viewModel.getState();
        title.setText(state.getTitle());
        year.setText("Year: " + (state.getYear() == 0 ? "Unknown" : state.getYear()));
        genres.setText("Genres: " + String.join(", ", state.getGenres()));
        director.setText("Director: " + state.getDirector());
        cast.setText("Cast: " + String.join(", ", state.getCast()));
        watched.setText("Watched: " + (state.isWatched() ? "Yes" : "No"));
        rating.setText("Your rating: " + (state.getRating() == null ? "Unrated" : state.getRating()));
    }
}
