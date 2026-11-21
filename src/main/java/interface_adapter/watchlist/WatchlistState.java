package interface_adapter.watchlist;

import java.util.ArrayList;
import java.util.List;

public class WatchlistState {
    private List<WatchlistEntryState> entries = new ArrayList<>();
    private String errorMessage = "";
    private String successMessage = "";

    public List<WatchlistEntryState> getEntries() {
        return entries;
    }

    public void setEntries(List<WatchlistEntryState> entries) {
        this.entries = entries;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
