package es.unican.movies.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;

import es.unican.movies.BuildConfig;
import es.unican.movies.model.Movie;
import es.unican.movies.model.Series;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This class is the implementation of @link{IMoviesRepository}
 */
public class MoviesRepository implements IMoviesRepository {

    /**
     * The singleton instance of this class that can be used anywhere to access movies or series.
     */
    public static final MoviesRepository INSTANCE = new MoviesRepository();

    /**
     * The static API to retrieve lists of movies and tv series from the data source maintained
     * by the course team.
     */
    private static final IStaticApi StaticAPI;

    /**
     * The TMDB API to retrieve details of movies and tv series.
     */
    private static final ITmdbApi TmdbAPI;

    static {
        final Gson gson = new GsonBuilder().create();
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(IStaticApi.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            StaticAPI = retrofit.create(IStaticApi.class);
        }
        {
            OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
            httpClientBuilder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer " + BuildConfig.TMDB_API_KEY)
                            .method(original.method(), original.body());
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ITmdbApi.BASE_URL)
                    .client(httpClientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            TmdbAPI = retrofit.create(ITmdbApi.class);
        }
    }


    @Override
    public void requestAggregateMovies(ICallback<List<Movie>> cb) {
        StaticAPI.getAggregateMovies().enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                List<Movie> movies = response.body();
                cb.onSuccess(movies);
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                cb.onFailure(t);
            }
        });
    }

    @Override
    public void requestAggregateSeries(ICallback<List<Series>> cb) {
        StaticAPI.getAggregateSeries().enqueue(new Callback<List<Series>>() {
            @Override
            public void onResponse(Call<List<Series>> call, Response<List<Series>> response) {
                List<Series> series = response.body();
                cb.onSuccess(series);
            }

            @Override
            public void onFailure(Call<List<Series>> call, Throwable t) {
                cb.onFailure(t);
            }
        });
    }

    @Override
    public void requestMovieDetails(int id, ICallback<Movie> cb) {
        TmdbAPI.getMovieById(id).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                cb.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                cb.onFailure(t);
            }
        });
    }

    @Override
    public void requestSeriesDetails(int id, ICallback<Series> cb) {
        TmdbAPI.getSeriesById(id).enqueue(new Callback<Series>() {
            @Override
            public void onResponse(Call<Series> call, Response<Series> response) {
                cb.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<Series> call, Throwable t) {
                cb.onFailure(t);
            }
        });
    }
}
