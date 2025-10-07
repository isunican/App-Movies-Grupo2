package es.unican.movies.activities.details;

import android.view.View;

import es.unican.movies.model.Series;

public interface IDetailsContract {
    public interface DetailPresenter {
        void init(DetailsView view);

    }

    public interface DetailsView {


        void init();

        void showError(String message);


        void showDetailContent();
    }
}
