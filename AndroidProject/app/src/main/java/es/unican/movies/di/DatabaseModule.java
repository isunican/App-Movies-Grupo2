package es.unican.movies.di;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import es.unican.movies.DataBaseManagement.SeriesDao;
import es.unican.movies.DataBaseManagement.SeriesDatabase;

@Module
@InstallIn(SingletonComponent.class)
public class DatabaseModule {

    @Provides
    @Singleton
    public SeriesDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application, SeriesDatabase.class, "series_database").build();
    }

    @Provides
    public SeriesDao provideSeriesDao(SeriesDatabase database) {
        return database.seriesDao();
    }
}
