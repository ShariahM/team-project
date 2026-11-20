package view;

import interface_adapter.stats.StatsController;
import interface_adapter.stats.StatsState;
import interface_adapter.stats.StatsViewModel;
import interface_adapter.stats.StatsViewModelListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class StatsView extends JPanel implements StatsViewModelListener {

    private static final DateTimeFormatter MONTH_FORMAT = DateTimeFormatter.ofPattern("MMM yyyy");

    private final StatsViewModel statsViewModel;
    private StatsController statsController;

    private final JLabel totalMoviesLabel = new JLabel();
    private final JLabel averageRatingLabel = new JLabel();
    private final JLabel topGenreLabel = new JLabel();
    private final JLabel totalMinutesLabel = new JLabel();
    private final JTextArea monthlyArea = new JTextArea(8, 24);

    public StatsView(StatsViewModel statsViewModel) {
        this.statsViewModel = statsViewModel;
        this.statsViewModel.addListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        final JLabel title = new JLabel(StatsViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        add(title);
        add(Box(15));

        for (JLabel label : new JLabel[]{totalMoviesLabel, averageRatingLabel, topGenreLabel, totalMinutesLabel}) {
            label.setAlignmentX(Component.CENTER_ALIGNMENT);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            add(label);
        }

        final JLabel monthlyTitle = new JLabel("Minutes watched per month");
        monthlyTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        monthlyTitle.setFont(new Font("Arial", Font.BOLD, 14));
        add(Box(15));
        add(monthlyTitle);

        monthlyArea.setEditable(false);
        monthlyArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        final JScrollPane scrollPane = new JScrollPane(monthlyArea);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(scrollPane);

        final JButton refresh = new JButton("Refresh");
        refresh.setAlignmentX(Component.CENTER_ALIGNMENT);
        refresh.addActionListener(e -> {
            if (statsController != null) {
                statsController.execute();
            }
        });
        add(Box(15));
        add(refresh);

        stateChanged();
    }

    public void setStatsController(StatsController statsController) {
        this.statsController = statsController;
    }

    @Override
    public void stateChanged() {
        final StatsState state = statsViewModel.getState();
        totalMoviesLabel.setText("Movies watched: " + state.getTotalMoviesWatched());
        averageRatingLabel.setText(String.format("Average rating: %.1f", state.getAverageRating()));
        topGenreLabel.setText("Highest rated genre: "
                + (state.getHighestRatedGenre() == null ? "N/A" : state.getHighestRatedGenre()));
        totalMinutesLabel.setText("Total minutes watched: " + state.getTotalMinutesWatched());

        final StringBuilder sb = new StringBuilder();
        for (Map.Entry<YearMonth, Integer> entry : state.getMinutesPerMonth().entrySet()) {
            sb.append(String.format("%-10s %6d min%n", entry.getKey().format(MONTH_FORMAT), entry.getValue()));
        }
        monthlyArea.setText(sb.length() == 0 ? "No dated entries yet." : sb.toString());
    }

    private static Component Box(int height) {
        return javax.swing.Box.createRigidArea(new Dimension(0, height));
    }
}
