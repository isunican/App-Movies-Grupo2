package es.unican.movies.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


/**
 * A TV series using the TMDB data model.
 */
@Getter
@Setter
@Parcel
public class Series {

    @SerializedName("id")
    protected int id;

    @SerializedName("name")
    protected String name;

    @SerializedName("original_title")
    protected String originalTitle;

    @SerializedName("poster_path")
    protected String posterPath;

    @SerializedName("vote_average")
    protected double voteAverage;

    @SerializedName("vote_count")
    protected int voteCount;

    @SerializedName("in_production")
    protected boolean inProduction;

    @SerializedName("overview")
    protected String overview;

    @SerializedName("first_air_date")
    protected String firstAirDate;

    @SerializedName("last_air_date")
    protected String lastAirDate;

    @SerializedName("number_of_episodes")
    protected int numberOfEpisodes;

    @SerializedName("number_of_seasons")
    protected int numberOfSeasons;

    @SerializedName("genres")
    protected List<Genre> genres;

}
