package es.unican.movies.activities.main;

import android.util.Log;

import java.util.List;
import java.util.concurrent.Executors;

import es.unican.movies.DataBaseManagement.SeriesDatabase;
import es.unican.movies.DataBaseManagement.SeriesDB;
import es.unican.movies.MoviesApp;
import es.unican.movies.model.Series;
import es.unican.movies.service.ICallback;
import es.unican.movies.service.IMoviesRepository;

public class MainPresenter implements IMainContract.Presenter {

    IMainContract.View view;
    private static final String TAG = "MainPresenter";

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
                if (elements == null) {
                    // Avoid NPE and inform the view
                    view.showLoadError();
                    return;
                }

                view.showSeries(elements);

                // Only try to persist a sample if we actually have at least one element
                if (!elements.isEmpty()) {
                    try {
                        MoviesApp app = (MoviesApp) view.getContext().getApplicationContext();
                        SeriesDatabase db = app.getRoom();
                        SeriesDB seriesDB = WishlistAdapter.convertToSeriesDB(elements.get(0));
                        Log.d(TAG, "Inserting sample into wishlist: id=" + seriesDB.getId() + " name=" + seriesDB.getName());
                        Executors.newSingleThreadExecutor().execute(() -> {
                            try {
                                db.seriesDao().addToWishlist(seriesDB);
                                Log.d(TAG, "Insert completed for id=" + seriesDB.getId());

                                // Immediately read back the wishlist from DB to confirm
                                try {
                                    List<SeriesDB> current = db.seriesDao().getWishlist();
                                    Log.d(TAG, "Wishlist size after insert = " + (current == null ? 0 : current.size()));
                                    if (current != null) {
                                        for (SeriesDB s : current) {
                                            Log.d(TAG, "Wishlist contains id=" + s.getId() + " name=" + s.getName());
                                        }
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, "Error reading wishlist after insert", e);
                                }

                            } catch (Exception e) {
                                Log.e(TAG, "Error inserting into wishlist", e);
                            }
                        });
                    } catch (Exception ex) {
                        // Swallow persistence errors to avoid crashing the UI thread
                        Log.e(TAG, "Error preparing persistence", ex);
                    }
                }

                view.showLoadCorrect(elements.size());
            }
            @Override
            public void onFailure(Throwable e) {
                view.showLoadError();
            }
        });
    }


}
