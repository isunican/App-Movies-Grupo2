package es.unican.movies.service;

import es.unican.movies.model.Movie;
import es.unican.movies.model.Series;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Interface for the TMDB API to retrieve details of movies and tv series.
 */
public interface ITmdbApi {

    public final static String BASE_URL = "https://api.themoviedb.org/3/";

    public final static String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/";

    public static String getFullImagePath(String path, EImageSize size) {
        return IMAGE_BASE_URL + size.value + path;
    }

    /**
     * Get the detailed information of the given movie
     * @param id the id of the movie
     * @return the detailed information of the movie
     */
    @GET("movie/{id}")
    Call<Movie> getMovieById(@Path("id") int id);

    /**
     * Get the detailed information of the given series
     * @param id the id of the series
     * @return the detailed information of the series
     */
    @GET("tv/{id}")
    Call<Series> getSeriesById(@Path("id") int id);
}
