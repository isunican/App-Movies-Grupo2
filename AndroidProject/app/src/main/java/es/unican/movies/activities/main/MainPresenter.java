package es.unican.movies.activities.main;

import android.util.Log;

import java.util.List;
import java.util.concurrent.Executors;

import es.unican.movies.DataBaseManagement.SeriesDB;
import es.unican.movies.DataBaseManagement.SeriesDatabase;
import es.unican.movies.MoviesApp;
import es.unican.movies.model.FilterSeries;
import es.unican.movies.model.Series;
import es.unican.movies.service.ICallback;
import es.unican.movies.service.IMoviesRepository;

public class MainPresenter implements IMainContract.Presenter {

    IMainContract.View view;
    private static final String TAG = "MainPresenter";
    private FilterSeries filterSeries = new FilterSeries();

    @Override
    public void init(IMainContract.View view) {
        this.view = view;
        this.view.init();
        load();
    }

    @Override
    public void onSearchBarContentChanged(String title){
        if (title == null || title.trim().isEmpty()) {
            load();
            return;
        }
        this.filterSeries.setTitle(title);
        load(filterSeries);
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
     * Loads the series from the repository, and sends them to the view with filter
     */
    private void load(FilterSeries filterSeries) {
        IMoviesRepository repository = view.getMoviesRepository();

        repository.requestAggregateSeries(new ICallback<List<Series>>() {
            @Override
            public void onSuccess(List<Series> elements) {
                if (elements == null) {
                    view.showLoadError();
                    return;
                }
                String filterText = filterSeries.getTitle().toLowerCase();
                elements.removeIf(s -> {
                    String originalTitle = s.getOriginalTitle() != null ? s.getOriginalTitle().toLowerCase() : "";
                    String name = s.getName() != null ? s.getName().toLowerCase() : "";

                    // Si ninguno contiene el texto del filtro, lo removemos
                    return !originalTitle.contains(filterText) && !name.contains(filterText);
                });
                view.showSeries(elements);
                view.showLoadCorrect(elements.size());
            }

            @Override
            public void onFailure(Throwable e) {
                view.showLoadError();
            }
        });
    }
    /**
     * Loads the series from the repository, and sends them to the view
     */
    private void load() {
        IMoviesRepository repository = view.getMoviesRepository();
        repository.requestAggregateSeries(new ICallback<List<Series>>() {
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
                                readWishList(db);

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

            private void readWishList(SeriesDatabase db) {
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
            }

            @Override
            public void onFailure(Throwable e) {
                view.showLoadError();
            }
        });
    }


}
