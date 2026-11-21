package data_access;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import entity.Movie;
import entity.Review;
import entity.Watchlist;
import use_case.stats.StatsDataAccessInterface;
import use_case.watchlist.WatchlistDataAccessInterface;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class JsonMovieDataAccessObject implements WatchlistDataAccessInterface, StatsDataAccessInterface {

    private final File jsonFile;
    private final Watchlist watchlist;
    private final Gson gson;

    public JsonMovieDataAccessObject(String filePath) {
        this.jsonFile = new File(filePath);
        this.watchlist = new Watchlist();
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (date, type, context) ->
                        new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE)))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, context) ->
                        LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
                .setPrettyPrinting()
                .create();

        if (jsonFile.exists() && jsonFile.length() > 0) {
            load();
        } else {
            createParentDirectories();
            save();
        }
    }

    private void createParentDirectories() {
        File parent = jsonFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }
    }

    private void load() {
        try (FileReader reader = new FileReader(jsonFile)) {
            Type listType = new TypeToken<ArrayList<Review>>() {}.getType();
            List<Review> loadedReviews = gson.fromJson(reader, listType);
            if (loadedReviews != null) {
                for (Review review : loadedReviews) {
                    watchlist.addReview(review);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not load watchlist data from file: " + jsonFile.getPath(), e);
        }
    }

    private void save() {
        createParentDirectories();
        try (FileWriter writer = new FileWriter(jsonFile)) {
            gson.toJson(watchlist.getReviews(), writer);
        } catch (IOException e) {
            throw new RuntimeException("Could not save watchlist data to file: " + jsonFile.getPath(), e);
        }
    }

    @Override
    public void saveReview(Review review) {
        watchlist.addReview(review);
        save();
    }

    @Override
    public void removeReview(int movieID) {
        watchlist.removeReview(movieID);
        save();
    }

    @Override
    public Review getReview(int movieID) {
        return watchlist.getReview(movieID);
    }

    @Override
    public List<Review> getAllReviews() {
        return new ArrayList<>(watchlist.getReviews());
    }

    @Override
    public boolean existsById(int movieID) {
        return watchlist.getReview(movieID) != null;
    }
}
