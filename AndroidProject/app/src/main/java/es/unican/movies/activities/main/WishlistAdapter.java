package es.unican.movies.activities.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.unican.movies.DataBaseManagement.SeriesDB;
import es.unican.movies.R;
import es.unican.movies.model.Series;
import es.unican.movies.service.EImageSize;
import es.unican.movies.service.ITmdbApi;

public class WishlistAdapter extends ArrayAdapter<SeriesDB> {

    /**
     * List of series to display.
     */
    private final List<SeriesDB> wishlistList;

    private final Context context;


    /**
     * Constructor del adaptador.
     *
     * @param context Contexto de la aplicación o actividad.
     * @param wishlistList Lista de objetos SeriesDB que se mostrarán.
     *
     * Usa el layout `activity_main_movie_item.xml` para cada fila.
     */
    protected WishlistAdapter(@NonNull Context context, @NonNull List<SeriesDB> wishlistList) {
        super(context, R.layout.activity_main_movie_item, wishlistList);
        this.context = context;
        this.wishlistList = wishlistList;
    }

    /**
     * Método principal del adaptador.
     * Crea (o reutiliza) la vista de cada elemento de la lista y la rellena con los datos de una serie.
     *
     * @param position Índice del elemento dentro de la lista.
     * @param convertView Vista reutilizable (si no es nula, se recicla para optimizar memoria).
     * @param parent Vista padre (el ListView).
     * @return Vista final que representa la serie en esa posición.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SeriesDB series = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_main_movie_item, parent, false);
        }

        // poster
        ImageView ivPoster = convertView.findViewById(R.id.ivPoster);
        String imageUrl = ITmdbApi.getFullImagePath(series.getPosterPath(), EImageSize.W92);
        Picasso.get().load(imageUrl).fit().centerInside().into(ivPoster);

        // titulo
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(series.getName());




        return convertView;
    }

    /**
     * Devuelve el número total de elementos de la lista.
     * El ListView lo usa para saber cuántas filas debe mostrar.
     */
    @Override
    public int getCount() {
        return wishlistList.size();
    }

    /**
     * Devuelve el elemento (SeriesDB) en la posición indicada.
     */
    @Nullable
    @Override
    public SeriesDB getItem(int position) {
        return wishlistList.get(position);
    }

    /**
     * Convierte un objeto del modelo Series (usado en la app)
     * a una entidad SeriesDB (usada en la base de datos Room).
     */
    public static SeriesDB convertToSeriesDB(Series series) {
        SeriesDB seriesDB = new SeriesDB();
        seriesDB.setId(series.getId());
        seriesDB.setName(series.getName());
        seriesDB.setPosterPath(series.getPosterPath());
        seriesDB.setVoteAverage(series.getVoteAverage());
        seriesDB.setVoteCount(series.getVoteCount());
        seriesDB.setFirstAirDate(series.getFirstAirDate());
        seriesDB.setLastAirDate(series.getLastAirDate());
        seriesDB.setNumberOfEpisodes(series.getNumberOfEpisodes());
        seriesDB.setNumberOfSeasons(series.getNumberOfSeasons());
        seriesDB.setGenres(series.getGenres());

        return seriesDB;
    }
}
