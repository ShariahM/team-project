package view;

import interface_adapter.recommendations.RecommendationsController;
import interface_adapter.recommendations.RecommendationsState;
import interface_adapter.recommendations.RecommendationsViewModel;
import interface_adapter.recommendations.RecommendationsViewModelListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

public class RecommendationsView extends JPanel implements RecommendationsViewModelListener {

    private final RecommendationsViewModel recommendationsViewModel;
    private RecommendationsController recommendationsController;

    private final JButton getRecommendationsButton = new JButton("Get recommendations");
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JLabel messageLabel = new JLabel(" ");

    public RecommendationsView(RecommendationsViewModel recommendationsViewModel) {
        this.recommendationsViewModel = recommendationsViewModel;
        this.recommendationsViewModel.addListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        final JLabel title = new JLabel(RecommendationsViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        final JLabel description = new JLabel("Movies picked from the ones you rated 4 stars or higher.");
        description.setAlignmentX(Component.CENTER_ALIGNMENT);
        description.setFont(new Font("Arial", Font.PLAIN, 14));

        getRecommendationsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        getRecommendationsButton.addActionListener(event -> loadRecommendations());

        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        final JList<String> resultsList = new JList<>(listModel);
        resultsList.setFont(new Font("Arial", Font.PLAIN, 14));
        final JScrollPane scrollPane = new JScrollPane(resultsList);
        scrollPane.setPreferredSize(new Dimension(520, 260));
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(title);
        add(description);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(getRecommendationsButton);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(messageLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(scrollPane);
    }

    public void setRecommendationsController(RecommendationsController recommendationsController) {
        this.recommendationsController = recommendationsController;
    }

    /** Runs the use case on a background thread, since it waits on TMDB. */
    private void loadRecommendations() {
        if (recommendationsController == null) {
            return;
        }
        getRecommendationsButton.setEnabled(false);
        messageLabel.setForeground(Color.DARK_GRAY);
        messageLabel.setText("Loading recommendations...");

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                recommendationsController.execute();
                return null;
            }

            @Override
            protected void done() {
                getRecommendationsButton.setEnabled(true);
            }
        }.execute();
    }

    @Override
    public void stateChanged() {
        // The presenter may call this from the background thread; Swing must be updated on the EDT.
        SwingUtilities.invokeLater(() -> {
            final RecommendationsState state = recommendationsViewModel.getState();
            listModel.clear();
            for (String line : state.getRecommendations()) {
                listModel.addElement(line);
            }

            if (state.getErrorMessage() != null) {
                messageLabel.setForeground(Color.RED);
                messageLabel.setText(state.getErrorMessage());
            } else {
                messageLabel.setForeground(Color.DARK_GRAY);
                messageLabel.setText(listModel.size() + " recommendations");
            }
        });
    }
}
