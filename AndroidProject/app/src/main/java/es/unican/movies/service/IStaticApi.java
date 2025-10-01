package es.unican.movies.service;

import java.util.List;

import es.unican.movies.model.Movie;
import es.unican.movies.model.Series;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Interface for the static API to retrieve lists of movies and tv series.
 * This data is created and maintained by the course team and is updated periodically.
 * This data is built using the TMDB API.
 */
public interface IStaticApi {

    final static String BASE_URL = "https://personales.unican.es/rivasjm/resources/tmdb/";

    /**
     * Get the aggregate list of movies.
     * @return the aggregate list of movies
     */
    @GET("movies/aggregate.json")
    Call<List<Movie>> getAggregateMovies();

    /**
     * Get the aggregate list of tv series.
     * @return the aggregate list of tv series
     */
    @GET("series/aggregate.json")
    Call<List<Series>> getAggregateSeries();

}
