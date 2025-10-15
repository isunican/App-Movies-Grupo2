package es.unican.movies.activities.main;

import java.util.List;

import es.unican.movies.DataBaseManagement.SeriesApp;
import es.unican.movies.DataBaseManagement.SeriesDatabase;
import es.unican.movies.DataBaseManagement.SeriesDB;
import es.unican.movies.DataBaseManagement.SeriesDao;
import es.unican.movies.model.Series;
import es.unican.movies.service.ICallback;
import es.unican.movies.service.IMoviesRepository;

public class MainPresenter implements IMainContract.Presenter {

    IMainContract.View view;
    Boolean wishlist = false;

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
       boolean wishlist;
        if (this.wishlist){
            loadbyWishlist();
        } else {
            loadByAPI();
        }
    }

    private void loadByAPI() {
        IMoviesRepository repository = view.getMoviesRepository();
        repository.requestAggregateSeries(new ICallback<>() {
            @Override
            public void onSuccess(List<Series> elements) {
                view.showSeries(elements);
                SeriesApp app = (SeriesApp) view.getContext().getApplicationContext();
                SeriesDatabase db = app.room;
                SeriesDao dao = db.seriesDao();
                dao.addToWishlist(elements.get(0));
                view.showLoadCorrect(elements.size());
            }
            @Override
            public void onFailure(Throwable e) {
                view.showLoadError();
            }
        });
    }
    private void loadbyWishlist() {
        SeriesApp app = (SeriesApp) view.getContext().getApplicationContext();
        SeriesDatabase db = app.room;
        SeriesDao dao = db.seriesDao();
        List<SeriesDB> wishlist = dao.getWishlist();
        view.showWishlist(wishlist);
    }


}


