package es.unican.movies.di;

import android.app.Application;

import androidx.room.Room;

import javax.inject.Singleton;


import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.testing.TestInstallIn;
import es.unican.movies.DataBaseManagement.SeriesDao;
import es.unican.movies.DataBaseManagement.SeriesDatabase;

@Module
@TestInstallIn(
    components = SingletonComponent.class,
    replaces = DatabaseModule.class
)
public class TestDatabaseModule {

    @Provides
    @Singleton
    public SeriesDatabase provideDatabase(Application application) {
        return Room.inMemoryDatabaseBuilder(application, SeriesDatabase.class).allowMainThreadQueries().build();
    }

    @Provides
    public SeriesDao provideSeriesDao(SeriesDatabase database) {
        return database.seriesDao();
    }
}
