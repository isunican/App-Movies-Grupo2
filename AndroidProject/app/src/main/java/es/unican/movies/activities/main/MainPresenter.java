package es.unican.movies.activities.main;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import es.unican.movies.DataBaseManagement.SeriesDB;
import es.unican.movies.DataBaseManagement.SeriesDao;
import es.unican.movies.DataBaseManagement.SeriesDatabase;
import es.unican.movies.model.FilterSeries;
import es.unican.movies.model.Genre;
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

    @Override
    public void onGenresSelected(List<String> genres) {
        this.filterSeries.setGenres(genres);
        load(this.filterSeries);
    }

    @Override
    public List<String> getSelectedGenres() {
        return filterSeries.getGenres() == null ? new ArrayList<>() : filterSeries.getGenres();
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

        List<Series> filteredSeriesList = getFilteredSeries(filterSeries);
        currentSeriesWithFilter = filteredSeriesList;
        view.showSeries(filteredSeriesList);
    }

    /**
     * Filters the current series list based on the provided filter.
     * @param filter The filter criteria.
     * @return The filtered list of series.
     */
    private List<Series> getFilteredSeries(FilterSeries filter) {
        final String title = filter.getTitle();
        final List<String> selectedGenres = filter.getGenres();

        if ((title == null || title.trim().isEmpty()) && (selectedGenres == null || selectedGenres.isEmpty())) {
            return currentSeriesList;
        }

        final List<String> normalizedSelectedGenres = (selectedGenres == null || selectedGenres.isEmpty()) ?
                Collections.emptyList() :
                selectedGenres.stream().map(g -> g.trim().toLowerCase()).collect(Collectors.toList());

        return currentSeriesList.stream()
                .filter(series -> matchesTitle(series, title))
                .filter(series -> matchesGenres(series, normalizedSelectedGenres))
                .collect(Collectors.toList());
    }

    /**
     * Checks if the series matches the given title filter.
     * @param series The series to check.
     * @param title The title filter.
     * @return true if the series matches the title filter, false otherwise.
     */
    private boolean matchesTitle(Series series, String title) {
        if (title == null || title.trim().isEmpty()) {
            return true;
        }
        String filterText = title.toLowerCase().trim();
        String originalTitle = series.getOriginalTitle() != null ? series.getOriginalTitle().toLowerCase() : "";
        String name = series.getName() != null ? series.getName().toLowerCase() : "";
        return originalTitle.contains(filterText) || name.contains(filterText);
    }

    /**
     * Checks if the series matches any of the selected genres.
     * @param series The series to check.
     * @param normalizedSelectedGenres The list of selected genres in normalized form (lowercase and trimmed).
     * @return true if the series matches any of the selected genres, false otherwise.
     */
    private boolean matchesGenres(Series series, List<String> normalizedSelectedGenres) {
        if (normalizedSelectedGenres.isEmpty()) {
            return true;
        }
        if (series.getGenres() == null || series.getGenres().isEmpty()) {
            return false;
        }
        List<String> normalizedSeriesGenres = series.getGenres().stream()
                .map(g -> g.name.trim().toLowerCase())
                .collect(Collectors.toList());
        return !Collections.disjoint(normalizedSeriesGenres, normalizedSelectedGenres);
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
                    view.showLoadError();
                    return;
                }

                view.showSeries(elements);
                currentSeriesList = elements;
                currentSeriesWithFilter = currentSeriesList;
                if (!elements.isEmpty()) {
                    try {
                        SeriesDB seriesDB = WishlistAdapter.convertToSeriesDB(elements.get(0));
                        SeriesDao dao = view.getSeriesDao();
                        dao.addToWishlist(seriesDB);

                    } catch (Exception ex) {
                        // Swallow persistence errors to avoid crashing the UI thread
                    }
                }

                view.showLoadCorrect(elements.size());
            }

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
