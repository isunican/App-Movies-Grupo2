package es.unican.movies.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.unican.movies.DataBaseManagement.SeriesDB;
import es.unican.movies.DataBaseManagement.SeriesDao;
import es.unican.movies.R;
import es.unican.movies.activities.details.DetailsSeriesView;
import es.unican.movies.model.Series;

/**
 * Fragment that displays the wishlist loaded from Room.
 */
@AndroidEntryPoint
public class WishlistFragment extends Fragment {

    private static final String TAG = "WishlistFragment";

    @Inject
    SeriesDao seriesDao;

    private ListView lvWishlist;
    private TextView tvEmpty;

    /**
     * Método llamado cuando se crea la vista del fragmento.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_wishlist, container, false);
        lvWishlist = root.findViewById(R.id.lvWishlist);
        tvEmpty = root.findViewById(R.id.tvEmptyWishlist);
        return root;
    }

    /**
     * Método llamado una vez que la vista ya ha sido creada.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observe wishlist LiveData so UI updates when DB changes
        seriesDao.getWishlistLive().observe(getViewLifecycleOwner(), dbList -> {
            List<SeriesDB> list = dbList == null ? new ArrayList<>() : dbList;
            Log.d(TAG, "Observed wishlist change, size=" + list.size());
            if (list.isEmpty()) {
                lvWishlist.setVisibility(View.GONE);
                tvEmpty.setVisibility(View.VISIBLE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                lvWishlist.setVisibility(View.VISIBLE);
                WishlistAdapter adapter = new WishlistAdapter(requireContext(), list);
                lvWishlist.setAdapter(adapter);
                lvWishlist.setOnItemClickListener((AdapterView<?> parent, View itemView, int position, long id) -> {
                    SeriesDB selected = (SeriesDB) parent.getItemAtPosition(position);
                    Series series = convertToSeries(selected);
                    Intent intent = new Intent(requireContext(), DetailsSeriesView.class);
                    intent.putExtra(DetailsSeriesView.INTENT_MOVIE, Parcels.wrap(series));
                    startActivity(intent);
                });
            }
        });
    }

    /**
     * Convierte una entidad de base de datos (SeriesDB) en un objeto de modelo (Series)
     * para poder pasarla a la pantalla de detalles.
     */
    private Series convertToSeries(SeriesDB db) {
        Series s = new Series();
        s.setId(db.getId());
        s.setName(db.getName());
        s.setPosterPath(db.getPosterPath());
        s.setVoteAverage(db.getVoteAverage());
        s.setVoteCount(db.getVoteCount());
        s.setFirstAirDate(db.getFirstAirDate());
        s.setLastAirDate(db.getLastAirDate());
        s.setNumberOfEpisodes(db.getNumberOfEpisodes());
        s.setNumberOfSeasons(db.getNumberOfSeasons());
        s.setGenres(db.getGenres());
        return s;
    }
}
