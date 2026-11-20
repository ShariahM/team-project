package view;

import entity.Movie;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;
import java.util.List;

public class MovieDetailView extends JPanel {
    public static final String VIEW_NAME = "movie details";

    public MovieDetailView(Movie movie, Icon poster, List<String> cast, boolean watched, Double rating) {
        super(new BorderLayout(20, 0));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        JLabel posterLabel = poster == null ? new JLabel("Poster unavailable") : new JLabel(poster);
        add(posterLabel, BorderLayout.WEST);

        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        JLabel title = new JLabel(movie.getTitle());
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        details.add(title);
        details.add(new JLabel("Year: " + movie.getReleaseYear()));
        details.add(new JLabel("Genres: " + String.join(", ", movie.getGenres())));
        details.add(new JLabel("Director: " + movie.getDirector()));
        details.add(new JLabel("Cast: " + String.join(", ", cast)));
        details.add(new JLabel("Watched: " + (watched ? "Yes" : "No")));
        details.add(new JLabel("Your rating: " + (rating == null ? "Unrated" : rating.toString())));
        add(details, BorderLayout.CENTER);
    }
}
