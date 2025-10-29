package es.unican.movies.activities.main;



import java.util.List;
import java.util.concurrent.Executors;

import es.unican.movies.DataBaseManagement.SeriesDB;
import es.unican.movies.DataBaseManagement.SeriesDatabase;
import es.unican.movies.MoviesApp;
import es.unican.movies.model.FilterSeries;
import es.unican.movies.model.Series;
import es.unican.movies.service.ICallback;
import es.unican.movies.service.IMoviesRepository;

/**
 * Presenter for the main activity, implementing the M-V-P pattern.
 * It is responsible for handling user actions, fetching data from the repository,
 * and updating the view.
 */
public class MainPresenter implements IMainContract.Presenter {

    /** The view attached to this presenter. */
    IMainContract.View view;
    private static final String TAG = "MainPresenter";
    /** The filter criteria for the series list. */
    private FilterSeries filterSeries = new FilterSeries();

    private List<Series> currentSeriesList = null;
    private List<Series> currentSeriesWithFilter = null;

    /**
     * Initializes the presenter.
     * @param view The view that this presenter will control.
     */
    @Override
    public void init(IMainContract.View view) {
        this.view = view;
        this.view.init();
        load();
    }

    /**
     * Handles the logic when the content of the search bar changes.
     * It filters the list of series based on the provided title.
     * If the title is empty, it loads the complete list.
     * @param title The text from the search bar.
     */
    @Override
    public void onSearchBarContentChanged(String title){

        this.filterSeries.setTitle(title);
        load(filterSeries);
    }

    /**
     * Handles the click on a series item in the list.
     * @param series The series that was clicked.
     */
    @Override
    public void onItemClicked(Series series) {
        if (series == null) {
            return;
        }
        view.showSeriesDetails(series);
    }

    /**
     * Handles the click on the info menu item.
     */
    @Override
    public void onMenuInfoClicked() {
        view.showInfoActivity();
    }


    /**
     * Loads the series from the repository, filters them based on the provided filter,
     * and updates the view.
     * @param filterSeries The filter to apply to the series list.
     */
    private void load(FilterSeries filterSeries) {
        if (currentSeriesList == null) {
            view.showLoadError();
            return;
        }

        String title = filterSeries.getTitle();
        if (title == null || title.trim().isEmpty()) {
            // Reset to full list (non-destructive)
            currentSeriesWithFilter = currentSeriesList;
            view.showSeries(currentSeriesList);

            return;
        }

        String filterText = title.toLowerCase().trim();
        List<Series> filtered = new java.util.ArrayList<>();
        for (Series s : currentSeriesList) {
            String originalTitle = s.getOriginalTitle() != null ? s.getOriginalTitle().toLowerCase() : "";
            String name = s.getName() != null ? s.getName().toLowerCase() : "";
            if (originalTitle.contains(filterText) || name.contains(filterText)) {
                filtered.add(s);
            }
        }

        currentSeriesWithFilter = filtered; // new list, original stays unchanged
        view.showSeries(filtered);

    }
    /**
     * Loads all series from the repository without any filter and updates the view.
     * It also includes debugging logic to persist a sample series to the database.
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
                currentSeriesList = elements;
                currentSeriesWithFilter = currentSeriesList;
                // Only try to persist a sample if we actually have at least one element
                if (!elements.isEmpty()) {
                    try {
                        MoviesApp app = (MoviesApp) view.getContext().getApplicationContext();
                        SeriesDatabase db = app.getRoom();
                        SeriesDB seriesDB = WishlistAdapter.convertToSeriesDB(elements.get(0));
                        Executors.newSingleThreadExecutor().execute(() -> {
                            try {
                                db.seriesDao().addToWishlist(seriesDB);

                                // Immediately read back the wishlist from DB to confirm
                                readWishList(db);

                            } catch (Exception e) {
                            }
                        });
                    } catch (Exception ex) {
                        // Swallow persistence errors to avoid crashing the UI thread
                    }
                }

                view.showLoadCorrect(elements.size());
            }

            /**
             * Reads and logs the current content of the wishlist from the database.
             * Used for debugging purposes.
             * @param db The database instance.
             */
            private void readWishList(SeriesDatabase db) {
                try {
                    List<SeriesDB> current = db.seriesDao().getWishlist();
                    if (current != null) {
                        for (SeriesDB s : current) {
                        }
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(Throwable e) {
                view.showLoadError();
            }
        });
    }


}
