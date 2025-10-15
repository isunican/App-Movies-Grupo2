package es.unican.movies.DataBaseManagement;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SeriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addToWishlist(SeriesDB seriesDB);

    @Query("SELECT * FROM wishlist_series")
    List<SeriesDB> getWishlist();

    @Delete
    void removeFromWishlist(SeriesDB seriesDB);
}
