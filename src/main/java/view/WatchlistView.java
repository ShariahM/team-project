package view;

import interface_adapter.watchlist.WatchlistState;
import interface_adapter.watchlist.WatchlistViewModel;
import interface_adapter.watchlist.WatchlistViewModelListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Font;

public class WatchlistView extends JPanel implements WatchlistViewModelListener {

    private final WatchlistViewModel watchlistViewModel;

    public WatchlistView(WatchlistViewModel watchlistViewModel) {
        this.watchlistViewModel = watchlistViewModel;
        this.watchlistViewModel.addListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        final JLabel title = new JLabel(WatchlistViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        final JLabel description = new JLabel("Your saved movies and reviews will appear here.");
        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        description.setFont(new Font("Arial", Font.PLAIN, 14));

        add(title);
        add(description);
    }

    @Override
    public void stateChanged() {
        final WatchlistState state = watchlistViewModel.getState();
    }
}
