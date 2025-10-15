package es.unican.movies;

import android.app.Application;

import androidx.room.Room;

import dagger.hilt.android.HiltAndroidApp;
import es.unican.movies.DataBaseManagement.SeriesDatabase;
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

    public void onCreate() {
        super.onCreate();
        instance = this;
        room = Room.databaseBuilder(this, SeriesDatabase.class, "series_database")
                .build();
    }
}
