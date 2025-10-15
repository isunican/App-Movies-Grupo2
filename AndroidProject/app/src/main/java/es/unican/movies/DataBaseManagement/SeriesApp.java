package es.unican.movies.DataBaseManagement;

import android.app.Application;

import androidx.room.Room;

public class SeriesApp extends Application {

    public SeriesDatabase room = Room.databaseBuilder(this, SeriesDatabase.class, "series_database")
            .build();
}
