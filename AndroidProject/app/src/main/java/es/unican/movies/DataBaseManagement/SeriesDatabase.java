package es.unican.movies.DataBaseManagement;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {SeriesDB.class}, version = 1)
public abstract class SeriesDatabase extends RoomDatabase {
    public abstract SeriesDao seriesDao();
}
