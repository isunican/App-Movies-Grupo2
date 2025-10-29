package es.unican.movies.DataBaseManagement;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {SeriesDB.class}, version = 1, exportSchema = false)
@TypeConverters({GenreTypeConverter.class})
public abstract class SeriesDatabase extends RoomDatabase {

    public abstract SeriesDao seriesDao();

    private static SeriesDatabase INSTANCE;

    public static SeriesDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            SeriesDatabase.class, "series.db")
                    .build();
        }
        return INSTANCE;
    }
}