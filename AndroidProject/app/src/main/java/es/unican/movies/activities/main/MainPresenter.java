package es.unican.movies.activities.main;

import java.util.List;
import java.util.concurrent.Executors;

import es.unican.movies.DataBaseManagement.SeriesDatabase;
import es.unican.movies.DataBaseManagement.SeriesDB;
import es.unican.movies.DataBaseManagement.SeriesDao;
import es.unican.movies.MoviesApp;
import es.unican.movies.activities.wishlist.WishlistAdapter;
import es.unican.movies.model.Series;
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
    public void onItemClicked(Series series) {
        if (series == null) {
            return;
        }
        view.showSeriesDetails(series);
    }

    @Override
    public void onMenuInfoClicked() {
        view.showInfoActivity();
    }


    /**
     * Loads the series from the repository, and sends them to the view
     */
    private void load() {
        IMoviesRepository repository = view.getMoviesRepository();
        repository.requestAggregateSeries(new ICallback<>() {
            @Override
            public void onSuccess(List<Series> elements) {
                view.showSeries(elements);
                MoviesApp app = (MoviesApp) view.getContext().getApplicationContext();
                SeriesDatabase db = app.getRoom();
                SeriesDB seriesDB = WishlistAdapter.convertToSeriesDB(elements.get(0));
                Executors.newSingleThreadExecutor().execute(() -> {
                    db.seriesDao().addToWishlist(seriesDB);
                });

                view.showLoadCorrect(elements.size());
            }
            @Override
            public void onFailure(Throwable e) {
                view.showLoadError();
            }
        });
    }


}


