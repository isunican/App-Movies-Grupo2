package es.unican.movies.activities.main;

import java.util.List;
import java.util.concurrent.Executors;

import es.unican.movies.DataBaseManagement.SeriesDatabase;
import es.unican.movies.DataBaseManagement.SeriesDB;
import es.unican.movies.DataBaseManagement.SeriesDao;
import es.unican.movies.MoviesApp;
import es.unican.movies.model.Series;

public class WishlistPresenter implements IWishlistContract.Presenter {

    IWishlistContract.View view;
    Boolean wishlist = false;

    @Override
    public void init(IWishlistContract.View view) {
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
        MoviesApp app = (MoviesApp) view.getContext().getApplicationContext();
        SeriesDatabase db = app.getRoom();
        SeriesDao dao = db.seriesDao();
        Executors.newSingleThreadExecutor().execute(() -> {
            List<SeriesDB> wishlist = dao.getWishlist();
            view.showSeries(wishlist);
        });
    }




}


