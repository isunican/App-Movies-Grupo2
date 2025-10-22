package es.unican.movies.DataBaseManagement;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

import es.unican.movies.model.Genre;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    @ColumnInfo(name = "last_air_date")
    public String lastAirDate;

    @ColumnInfo(name = "number_of_episodes")
    public int numberOfEpisodes;

    @ColumnInfo(name = "number_of_seasons")
    public int numberOfSeasons;

    @ColumnInfo(name = "vote_average")
    public double voteAverage;

    @ColumnInfo(name = "vote_count")
    public int voteCount;

    @ColumnInfo(name = "genres")
    public List<Genre> genres;



}
