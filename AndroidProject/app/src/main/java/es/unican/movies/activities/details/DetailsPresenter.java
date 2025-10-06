package es.unican.movies.activities.details;

public class DetailsPresenter implements IDetailsContract.DetailPresenter {
    IDetailsContract.DetailsView view;
    @Override
    public void init(IDetailsContract.DetailsView view) {
        this.view = view;
        this.view.init();

    }

    public void loadDetails(){

    }
}
