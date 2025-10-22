package es.unican.movies.activities.main;

import android.content.Context;

import java.util.List;

import es.unican.movies.DataBaseManagement.SeriesDB;
import es.unican.movies.model.Series;

/**
 * Interfaces that define the contract between the Main Presenter and View.
 */
public interface IWishlistContract {

    /**
     * Methods that must be implemented in the Main Presenter.
     * Only the View should call these methods.
     */
    public interface Presenter {

        /**
         * Links the presenter with its view.
         * Only the View should call this method
         * @param view the view that will be controlled by this presenter
         */
        public void init(View view);

        /**
         * The presenter is informed that a series has been clicked
         * Only the View should call this method
         * @param series the series that has been clicked
         */
        public void onItemClicked(Series series);

        /**
         * The presenter is informed that the Info item in the menu has been clicked
         * Only the View should call this method
         */
        public void onMenuInfoClicked();

    }

    /**
     * Methods that must be implemented in the Main View.
     * Only the Presenter should call these methods.
     */
    public interface View {

        /**
         * Initialize the view. Typically this should initialize all the listeners in the view.
         * Only the Presenter should call this method
         */
        public void init();

        /**
         * Returns a repository that can be called by the Presenter to retrieve series or series.
         * This method must be located in the view because Android resources must be accessed
         * in order to instantiate a repository (for example Internet Access). This requires
         * dependencies to Android. We want to keep the Presenter free of Android dependencies,
         * therefore the Presenter should be unable to instantiate repositories and must rely on
         * the view to create the repository.
         * Only the Presenter should call this method
         * @return a repository that can be called by the Presenter to retrieve series or series
         */




        /**
         * The view is requested to display a notification indicating that the series
         * were loaded correctly.
         * Only the Presenter should call this method
         * @param series number of series
         */
        public void showLoadCorrect(int series);

        /**
         * The view is requested to display a notificacion indicating that the series
         * were not loaded correctly.
         * Only the Presenter should call this method
         */
        public void showLoadError();

        /**
         * The view is requested to display the detailed view of the given series.
         * Only the Presenter should call this method
         * @param series the series
         */
        public void showSeriesDetails(Series series);

        /**
         * The view is requested to open the info activity.
         * Only the Presenter should call this method
         */
        public void showInfoActivity();



        /**
         * Devuelve el contexto Android asociado a la vista.
         *
         * Este método permite que el Presenter (que no tiene acceso directo a clases de Android)
         * pueda obtener un Context cuando lo necesite.
         *
         * @return El objeto Context de la vista (por ejemplo, una Activity).
         */
        public Context getContext();

        /**
         * Muestra en la vista la lista de series almacenadas en la base de datos (wishlist).
         *
         * Este método es invocado por el Presenter cuando ya ha cargado los datos desde el modelo.
         * La vista debe implementar este método para actualizar la interfaz de usuario.
         *
         * @param wishlist Lista de objetos SeriesDB que representan las series guardadas en la wishlist.
         */
        void showSeries(List<SeriesDB> wishlist);
    }
}
