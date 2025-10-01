package es.unican.movies.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * A movie using the TMDB data model.
 */
@Getter
@Setter
@Parcel
public class Movie {

    @SerializedName("id")
    protected int id;

    @SerializedName("title")
    protected String title;

    @SerializedName("poster_path")
    protected String posterPath;

}
