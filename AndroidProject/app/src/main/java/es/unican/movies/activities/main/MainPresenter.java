package es.unican.movies.activities.main;

import java.util.List;
import java.util.Objects;

import es.unican.movies.model.FilterSeries;
import es.unican.movies.model.Series;
import es.unican.movies.service.ICallback;
import es.unican.movies.service.IMoviesRepository;

public class MainPresenter implements IMainContract.Presenter {

    IMainContract.View view;
    FilterSeries filterSeries = new FilterSeries();

    @Override
    public void init(IMainContract.View view) {
        this.view = view;
        this.view.init();
        load();
    }

    @Override
    public void onSearchBarContentChanged(String title){
        if (String.valueOf(title).isEmpty()) {
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

        repository.requestAggregateSeries(new ICallback<>() {
            @Override
            public void onSuccess(List<Series> elements) {
                assert elements != null;
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
        repository.requestAggregateSeries(new ICallback<>() {
            @Override
            public void onSuccess(List<Series> elements) {

                view.showSeries(elements);
                view.showLoadCorrect(elements.size());
            }

            @Override
            public void onFailure(Throwable e) {
                view.showLoadError();
            }
        });
    }

}
