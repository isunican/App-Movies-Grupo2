package es.unican.movies.activities.wishlist;

import java.util.List;
import java.util.concurrent.Executors;

import es.unican.movies.DataBaseManagement.SeriesDatabase;
import es.unican.movies.DataBaseManagement.SeriesDB;
import es.unican.movies.DataBaseManagement.SeriesDao;
import es.unican.movies.MoviesApp;
import es.unican.movies.model.Series;
import es.unican.movies.service.ICallback;
import es.unican.movies.service.IMoviesRepository;

public class WishlistPresenter implements IWishlistContract.Presenter {

    IWishlistContract.View view;

    /**
     * Inicializa el presenter con la vista.
     *
     * @param view La vista que implementa IWishlistContract.View (WishlistView).
     */
    @Override
    public void init(IWishlistContract.View view) {
        this.view = view;
        this.view.init();
        load();
    }

    /**
     * Evento cuando el usuario selecciona una serie de la lista.
     *
     * @param series Serie seleccionada (objeto del modelo).
     */
    @Override
    public void onItemClicked(Series series) {
        if (series == null) {
            return;
        }
        view.showSeriesDetails(series);
    }

    /**
     * Evento cuando el usuario hace clic en el botón "Info" del menú.
     * Simplemente pide a la vista que abra la actividad de información.
     */
    @Override
    public void onMenuInfoClicked() {
        view.showInfoActivity();
    }

    /**
     * Carga la lista de series desde la base de datos (Room)
     * y las envía a la vista.
     *
     * Se ejecuta en un hilo secundario porque las operaciones con base de datos
     * no pueden ejecutarse en el hilo principal (UI thread).
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


