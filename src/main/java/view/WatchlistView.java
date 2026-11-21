package view;

import interface_adapter.watchlist.WatchlistController;
import interface_adapter.watchlist.WatchlistEntryState;
import interface_adapter.watchlist.WatchlistState;
import interface_adapter.watchlist.WatchlistViewModel;
import interface_adapter.watchlist.WatchlistViewModelListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

public class WatchlistView extends JPanel implements WatchlistViewModelListener {

    private final WatchlistViewModel watchlistViewModel;
    private WatchlistController watchlistController;

    private final JPanel entriesListPanel = new JPanel();
    private final JLabel statusMessageLabel = new JLabel(" ");

    public WatchlistView(WatchlistViewModel watchlistViewModel) {
        this.watchlistViewModel = watchlistViewModel;
        this.watchlistViewModel.addListener(this);

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        final JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));

        final JLabel title = new JLabel(WatchlistViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        statusMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusMessageLabel.setFont(new Font("Arial", Font.ITALIC, 13));

        final JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        final JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> {
            if (watchlistController != null) {
                watchlistController.loadWatchlist();
            }
        });
        toolbar.add(refreshButton);

        headerPanel.add(title);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(statusMessageLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        headerPanel.add(toolbar);

        add(headerPanel, BorderLayout.NORTH);

        entriesListPanel.setLayout(new BoxLayout(entriesListPanel, BoxLayout.Y_AXIS));
        final JScrollPane scrollPane = new JScrollPane(entriesListPanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void setWatchlistController(WatchlistController watchlistController) {
        this.watchlistController = watchlistController;
    }

    @Override
    public void stateChanged() {
        final WatchlistState state = watchlistViewModel.getState();

        if (state.getErrorMessage() != null && !state.getErrorMessage().isBlank()) {
            statusMessageLabel.setText(state.getErrorMessage());
            statusMessageLabel.setForeground(Color.RED);
        } else if (state.getSuccessMessage() != null && !state.getSuccessMessage().isBlank()) {
            statusMessageLabel.setText(state.getSuccessMessage());
            statusMessageLabel.setForeground(new Color(0, 128, 0));
        } else {
            statusMessageLabel.setText(" ");
        }

        renderEntries(state.getEntries());
    }

    private void renderEntries(List<WatchlistEntryState> entries) {
        entriesListPanel.removeAll();

        if (entries == null || entries.isEmpty()) {
            final JLabel emptyLabel = new JLabel("Your watchlist is currently empty.");
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 15));
            entriesListPanel.add(Box.createRigidArea(new Dimension(0, 40)));
            entriesListPanel.add(emptyLabel);
        } else {
            for (WatchlistEntryState entry : entries) {
                entriesListPanel.add(createEntryCard(entry));
                entriesListPanel.add(Box.createRigidArea(new Dimension(0, 12)));
            }
        }

        entriesListPanel.revalidate();
        entriesListPanel.repaint();
    }

    private JPanel createEntryCard(WatchlistEntryState entry) {
        final JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        final JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        final String yearStr = entry.getReleaseYear() > 0 ? " (" + entry.getReleaseYear() + ")" : "";
        final JLabel movieTitle = new JLabel(entry.getTitle() + yearStr);
        movieTitle.setFont(new Font("Arial", Font.BOLD, 16));

        final String directorStr = (entry.getDirector() == null || entry.getDirector().isBlank())
                ? "" : "Directed by: " + entry.getDirector();
        final JLabel directorLabel = new JLabel(directorStr);
        directorLabel.setFont(new Font("Arial", Font.ITALIC, 13));
        directorLabel.setForeground(Color.DARK_GRAY);

        final String dateStr = (entry.getDateWatched() == null || entry.getDateWatched().isBlank())
                ? "Not watched yet" : "Watched on: " + entry.getDateWatched();
        final JLabel dateLabel = new JLabel(dateStr);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        final JLabel ratingLabel = new JLabel(String.format("Rating: %.1f / 5.0", entry.getRating()));
        ratingLabel.setFont(new Font("Arial", Font.BOLD, 13));

        final String reviewPreview = (entry.getReviewText() == null || entry.getReviewText().isBlank())
                ? "No review text added." : "\"" + entry.getReviewText() + "\"";
        final JLabel reviewLabel = new JLabel(reviewPreview);
        reviewLabel.setFont(new Font("Arial", Font.PLAIN, 13));

        infoPanel.add(movieTitle);
        if (!directorStr.isBlank()) {
            infoPanel.add(directorLabel);
        }
        infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        infoPanel.add(ratingLabel);
        infoPanel.add(dateLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 4)));
        infoPanel.add(reviewLabel);

        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        final JButton editButton = new JButton("Edit");
        final JButton deleteButton = new JButton("Remove");

        editButton.addActionListener(e -> showEditDialog(entry));
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Remove \"" + entry.getTitle() + "\" from your watchlist?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION
            );
            if (confirm == JOptionPane.YES_OPTION && watchlistController != null) {
                watchlistController.deleteReview(entry.getMovieId());
            }
        });

        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(buttonPanel, BorderLayout.EAST);
        return card;
    }

    private void showEditDialog(WatchlistEntryState entry) {
        final JTextField ratingField = new JTextField(String.valueOf(entry.getRating()), 5);
        final JTextField dateField = new JTextField(entry.getDateWatched(), 10);
        final JTextArea reviewArea = new JTextArea(entry.getReviewText(), 4, 20);
        reviewArea.setLineWrap(true);
        reviewArea.setWrapStyleWord(true);

        final JPanel dialogPanel = new JPanel(new BorderLayout(5, 5));
        final JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        fieldsPanel.add(new JLabel("Rating (0.0 - 5.0):"));
        fieldsPanel.add(ratingField);
        fieldsPanel.add(new JLabel("Date Watched (YYYY-MM-DD):"));
        fieldsPanel.add(dateField);

        dialogPanel.add(fieldsPanel, BorderLayout.NORTH);
        dialogPanel.add(new JLabel("Review:"), BorderLayout.CENTER);
        dialogPanel.add(new JScrollPane(reviewArea), BorderLayout.SOUTH);

        final int result = JOptionPane.showConfirmDialog(
                this,
                dialogPanel,
                "Edit Entry: " + entry.getTitle(),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION && watchlistController != null) {
            try {
                double newRating = Double.parseDouble(ratingField.getText().trim());
                String newDate = dateField.getText().trim();
                String newReview = reviewArea.getText().trim();
                watchlistController.updateReview(entry.getMovieId(), newRating, newReview, newDate);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Rating must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
