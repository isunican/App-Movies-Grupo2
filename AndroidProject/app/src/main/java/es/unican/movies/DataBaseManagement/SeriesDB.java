package es.unican.movies.DataBaseManagement;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Ignore;

@Entity(tableName = "wishlist_series")
public class SeriesDB {

    @PrimaryKey
    public int id;

    public String name;

    @ColumnInfo(name = "original_name")
    public String originalName;

    public String overview;

    @ColumnInfo(name = "poster_path")
    public String posterPath;

    @ColumnInfo(name = "backdrop_path")
    public String backdropPath;

    @ColumnInfo(name = "first_air_date")
    public String firstAirDate;

    @ColumnInfo(name = "vote_average")
    public double voteAverage;

    @ColumnInfo(name = "vote_count")
    public int voteCount;

    public double popularity;

    public String status;

    public String tagline;

    // 🔸 Campos complejos o listas (ignorados, no se guardan en Room)
    @Ignore public String homepage;
    @Ignore public String type;
    @Ignore public boolean adult;
    @Ignore public boolean inProduction;
    @Ignore public String originalLanguage;

    public SeriesDB() {}

    public String getPosterPathWishlist() {
        return posterPath;
    }

    public String getNameWishlist() {
        return name;
    }
}
