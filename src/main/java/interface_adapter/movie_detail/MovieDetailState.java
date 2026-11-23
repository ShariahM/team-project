package interface_adapter.movie_detail;

import java.util.List;

public class MovieDetailState {
    private String posterURL = "";
    private String title = "";
    private int year;
    private List<String> genres = List.of();
    private String director = "";
    private List<String> cast = List.of();
    private boolean watched;
    private Double rating;
}
