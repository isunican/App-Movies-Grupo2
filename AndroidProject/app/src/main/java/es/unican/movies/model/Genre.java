package es.unican.movies.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class Genre {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
}
