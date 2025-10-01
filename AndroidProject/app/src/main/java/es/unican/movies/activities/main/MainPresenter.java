package es.unican.movies.activities.main;

import android.util.Log;

import java.util.List;

import es.unican.movies.model.Movie;
import es.unican.movies.service.ICallback;
import es.unican.movies.service.IMoviesRepository;

public class MainPresenter implements IMainContract.Presenter {

    IMainContract.View view;


    @Override
    public void init(IMainContract.View view) {
        this.view = view;
        this.view.init();
        load();
    }

    @Override
    public void onItemClicked(Movie movie) {
        if (movie == null) {
            return;
        }
        view.showMovieDetails(movie);
    }

    @Override
    public void onMenuInfoClicked() {
        view.showInfoActivity();
    }

    /**
     * Loads the movies from the repository, and sends them to the view
     */
    private void load() {
        IMoviesRepository repository = view.getMoviesRepository();
        repository.requestAggregateMovies(new ICallback<List<Movie>>() {
            @Override
            public void onSuccess(List<Movie> elements) {
                view.showMovies(elements);
                view.showLoadCorrect(elements.size());
            }

            @Override
            public void onFailure(Throwable e) {
                view.showLoadError();
            }
        });
    }
}
