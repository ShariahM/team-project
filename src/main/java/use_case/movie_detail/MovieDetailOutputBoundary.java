package use_case.movie_detail;

public interface MovieDetailOutputBoundary {
    void prepareSuccessView(MovieDetailOutputData outputData);

    void prepareFailView(String errorMessage);
}
