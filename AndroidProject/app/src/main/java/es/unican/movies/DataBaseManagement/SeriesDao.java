package es.unican.movies.DataBaseManagement;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import es.unican.movies.model.Series;

/**
 * Interfaz DAO (Data Access Object) para gestionar el acceso a la base de datos
 * relacionada con la lista de deseos ("wishlist") de series.
 *
 * Room generará automáticamente la implementación de este DAO a partir de esta interfaz.
 */
@Dao
public interface SeriesDao {

    /**
     * Inserta una serie en la lista de deseos.
     * Si ya existe una serie con el mismo identificador (clave primaria),
     * se reemplaza por la nueva (gracias a OnConflictStrategy.REPLACE).
     *
     * @param series Objeto SeriesDB a insertar en la base de datos.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addToWishlist(SeriesDB series);

    /**
     * Devuelve toda la lista de deseos de series.
     *
     * Esta versión devuelve una lista normal (no reactiva),
     * por lo que debe ejecutarse fuera del hilo principal (por ejemplo, en un hilo secundario).
     *
     * @return Lista de objetos SeriesDB almacenados en la tabla "wishlist_series".
     */
    @Query("SELECT * FROM wishlist_series")
    List<SeriesDB> getWishlist();

    /**
     * Devuelve toda la lista de deseos de series, pero como LiveData.
     *
     * LiveData permite observar los cambios en tiempo real:
     * cada vez que cambia la tabla "wishlist_series", los observadores se actualizan automáticamente.
     *
     * @return LiveData con la lista de series almacenadas.
     */
    @Query("SELECT * FROM wishlist_series")
    LiveData<List<SeriesDB>> getWishlistLive();

    /**
     * Elimina una serie específica de la lista de deseos.
     *
     * @param seriesDB Objeto SeriesDB que se desea eliminar de la base de datos.
     */
    @Delete
    void removeFromWishlist(SeriesDB seriesDB);

    /**
     * Elimina todas las series de la lista de deseos.
     *
     */
    @Query("DELETE FROM wishlist_series")
    void clearWishlist();
}
