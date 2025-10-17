package es.unican.movies;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import java.util.concurrent.Executors;

import dagger.hilt.android.HiltAndroidApp;
import es.unican.movies.DataBaseManagement.SeriesDatabase;
import es.unican.movies.DataBaseManagement.SeriesDB;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
/**
 * This class is the entry point of the application.
 */
@HiltAndroidApp
public class MoviesApp extends Application {
    private static MoviesApp instance;
    private SeriesDatabase room;
    private static final String TAG = "MoviesApp";

    public void onCreate() {
        super.onCreate();
        instance = this;
        room = Room.databaseBuilder(this, SeriesDatabase.class, "series_database")
                .build();

        // Debug: seed the DB with a sample entry if empty to make it easy to test the wishlist UI
        if (BuildConfig.DEBUG) {
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    if (room.seriesDao().getWishlist() == null || room.seriesDao().getWishlist().isEmpty()) {
                        SeriesDB sample = new SeriesDB();
                        sample.setId(999999);
                        sample.setName("Debug Sample Series");
                        sample.setPosterPath(null);
                        room.seriesDao().addToWishlist(sample);
                        Log.d(TAG, "Inserted debug sample into wishlist (id=999999)");
                    } else {
                        Log.d(TAG, "Wishlist already contains items, not seeding debug sample");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error seeding wishlist for debug", e);
                }
            });
        }
    }
}
