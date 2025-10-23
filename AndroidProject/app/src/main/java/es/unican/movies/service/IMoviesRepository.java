package es.unican.movies.service;

import java.util.List;

import es.unican.movies.model.Movie;
import es.unican.movies.model.Series;

/**
 * Interface for the repository to access movies and series.
 */
public interface IMoviesRepository {

    /**
     * Request an aggregate list of movies.
     * @param cb the callback to be called when the request is successful or fails
     */
    public void requestAggregateMovies(ICallback<List<Movie>> cb);

    /**
     * Request an aggregate list of series.
     * @param cb the callback to be called when the request is successful or fails
     */
    public void requestAggregateSeries(ICallback<List<Series>> cb);

    /**
     * Request the details of a movie.
     * @param id the TMDB id of the movie
     * @param cb the callback to be called when the request is successful or fails
     */
    public void requestMovieDetails(int id, ICallback<Movie> cb);

    /**
     * Request the details of a series.
     * @param id the TMDB id of the series
     * @param cb the callback to be called when the request is successful or fails
     */
    public void requestSeriesDetails(int id, ICallback<Series> cb);
}
