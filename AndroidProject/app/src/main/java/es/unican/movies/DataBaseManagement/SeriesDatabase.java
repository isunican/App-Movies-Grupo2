package es.unican.movies.DataBaseManagement;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {SeriesDB.class}, version = 1)
@TypeConverters({GenreTypeConverter.class})
public abstract class SeriesDatabase extends RoomDatabase {
    public abstract SeriesDao seriesDao();
}
