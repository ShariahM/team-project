package view;

import interface_adapter.explore.ExploreController;
import interface_adapter.explore.ExploreState;
import interface_adapter.explore.ExploreViewModel;
import interface_adapter.explore.ExploreViewModelListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.util.concurrent.ExecutionException;

public class ExploreView extends JPanel implements ExploreViewModelListener {
    private final ExploreViewModel exploreViewModel;
    private final JTextField queryField = new JTextField(30);
    private final JButton searchButton = new JButton(ExploreViewModel.SEARCH_BUTTON_LABEL);
    private final JLabel statusLabel = new JLabel(" ");
    private final DefaultListModel<ExploreState.MovieResult> resultsModel = new DefaultListModel<>();

    private ExploreController exploreController;

    public ExploreView(ExploreViewModel exploreViewModel) {
        if (exploreViewModel == null) {
            throw new IllegalArgumentException("Explore view model must not be null.");
        }
        this.exploreViewModel = exploreViewModel;
        this.exploreViewModel.addListener(this);

        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));

        final JLabel title = new JLabel(ExploreViewModel.TITLE_LABEL);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        final JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.add(title);

        final JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        queryField.setToolTipText("Enter a movie title");
        searchPanel.add(queryField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        final JPanel topPanel = new JPanel(new BorderLayout(0, 12));
        topPanel.add(header, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(statusLabel, BorderLayout.SOUTH);

        final JList<ExploreState.MovieResult> resultsList = new JList<>(resultsModel);
        resultsList.setCellRenderer(new MovieRenderer());
        resultsList.setVisibleRowCount(12);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultsList), BorderLayout.CENTER);

        searchButton.addActionListener(event -> searchMovies());
        queryField.addActionListener(event -> searchMovies());
        stateChanged();
    }

    public void setExploreController(ExploreController exploreController) {
        this.exploreController = exploreController;
    }

    @Override
    public void stateChanged() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(this::stateChanged);
            return;
        }

        final ExploreState state = exploreViewModel.getState();
        queryField.setText(state.getQuery());
        resultsModel.clear();
        for (ExploreState.MovieResult movie : state.getMovies()) {
            resultsModel.addElement(movie);
        }

        if (state.getErrorMessage() != null) {
            statusLabel.setText(state.getErrorMessage());
        }
        else if (!state.getQuery().isEmpty() && state.getMovies().isEmpty()) {
            statusLabel.setText("No movies found.");
        }
        else if (!state.getMovies().isEmpty()) {
            statusLabel.setText(state.getMovies().size() + " movie(s) found.");
        }
        else {
            statusLabel.setText(" ");
        }
    }

    private void searchMovies() {
        if (exploreController == null) {
            statusLabel.setText("Movie search is not available.");
            return;
        }

        final String query = queryField.getText();
        final ExploreState pendingState = exploreViewModel.getState();
        pendingState.setQuery(query);
        pendingState.setErrorMessage(null);
        exploreViewModel.setState(pendingState);
        searchButton.setEnabled(false);
        statusLabel.setText("Searching...");

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                exploreController.execute(query);
                return null;
            }

            @Override
            protected void done() {
                searchButton.setEnabled(true);
                try {
                    get();
                }
                catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    statusLabel.setText("Movie search was interrupted.");
                }
                catch (ExecutionException exception) {
                    final Throwable cause = exception.getCause();
                    final String message = cause == null || cause.getMessage() == null
                            ? "Movie search failed."
                            : "Movie search failed: " + cause.getMessage();
                    statusLabel.setText(message);
                }
            }
        }.execute();
    }

    private static class MovieRenderer extends JLabel implements ListCellRenderer<ExploreState.MovieResult> {
        MovieRenderer() {
            setOpaque(true);
            setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends ExploreState.MovieResult> list,
                                                      ExploreState.MovieResult movie, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            final String year = movie.getReleaseYear() > 0
                    ? " (" + movie.getReleaseYear() + ")"
                    : "";
            setText(movie.getTitle() + year);
            setToolTipText(movie.getOverview().isEmpty() ? null : movie.getOverview());
            setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            return this;
        }
    }
}
