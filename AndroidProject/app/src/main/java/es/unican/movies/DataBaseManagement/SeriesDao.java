package es.unican.movies.DataBaseManagement;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import es.unican.movies.model.Series;

@Dao
public interface SeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addToWishlist(Series series);

    @Query("SELECT * FROM wishlist_series")
    List<SeriesDB> getWishlist();

    @Delete
    void removeFromWishlist(SeriesDB seriesDB);
}
