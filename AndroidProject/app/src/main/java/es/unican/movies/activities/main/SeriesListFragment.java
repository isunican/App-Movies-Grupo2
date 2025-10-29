package es.unican.movies.activities.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.unican.movies.DataBaseManagement.SeriesDB;
import es.unican.movies.DataBaseManagement.SeriesDao;
import es.unican.movies.R;
import es.unican.movies.model.Series;

@AndroidEntryPoint
public class SeriesListFragment extends Fragment {

    public interface Listener {
        void onSeriesClicked(Series series);
    }

    public static final int MODE_ALL = 0;
    public static final int MODE_WISHLIST = 1;

    @Inject
    SeriesDao seriesDao;

    private Listener listener;
    private ListView lvSeries;
    private TextView tvEmpty;
    private List<Series> pendingSeries = null;
    private int currentMode = MODE_ALL;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof Listener) {
            listener = (Listener) context;
        } else {
            throw new ClassCastException(context + " must implement SeriesListFragment.Listener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_series_list, container, false);
        lvSeries = root.findViewById(R.id.lvSeries);
        tvEmpty = root.findViewById(R.id.tvEmptyWishlist);

        // Apply view state depending on current mode
        if (currentMode == MODE_WISHLIST) {
            observeWishlist();
        } else {
            if (pendingSeries != null) {
                applySeriesToList(pendingSeries);
                pendingSeries = null;
            }
        }
        return root;
    }

    /**
     * Switches the fragment mode. If view is already created the UI updates immediately,
     * otherwise the mode will be applied in onCreateView.
     */
    public void setMode(int mode) {
        if (mode == currentMode) return;
        currentMode = mode;
        if (getView() == null) return; // will be handled in onCreateView

        if (mode == MODE_WISHLIST) {
            observeWishlist();
        } else {
            // stop observing wishlist
            try {
                seriesDao.getWishlistLive().removeObservers(getViewLifecycleOwner());
            } catch (Exception ignored) { }
            if (tvEmpty != null) tvEmpty.setVisibility(View.GONE);
            if (lvSeries != null) lvSeries.setVisibility(View.VISIBLE);
            if (pendingSeries != null) {
                applySeriesToList(pendingSeries);
                pendingSeries = null;
            }
        }
    }

    public void setSeries(List<Series> series) {
        // store or apply depending on view state and mode
        if (currentMode == MODE_WISHLIST) {
            // ignore incoming series when in wishlist mode
            return;
        }
        if (getContext() == null || lvSeries == null) {
            this.pendingSeries = series;
            return;
        }
        applySeriesToList(series);
    }

    private void applySeriesToList(List<Series> series) {
        SeriesAdapter adapter = new SeriesAdapter(getContext(), series);
        lvSeries.setAdapter(adapter);
        lvSeries.setOnItemClickListener((parent, view, position, id) -> {
            Series s = (Series) parent.getItemAtPosition(position);
            if (listener != null) listener.onSeriesClicked(s);
        });
    }

    private void observeWishlist() {
        // Observe wishlist LiveData so UI updates when DB changes
        seriesDao.getWishlistLive().observe(getViewLifecycleOwner(), dbList -> {
            List<SeriesDB> list = dbList == null ? new ArrayList<>() : dbList;
            if (list.isEmpty()) {
                if (lvSeries != null) lvSeries.setVisibility(View.GONE);
                if (tvEmpty != null) tvEmpty.setVisibility(View.VISIBLE);
            } else {
                if (tvEmpty != null) tvEmpty.setVisibility(View.GONE);
                if (lvSeries != null) lvSeries.setVisibility(View.VISIBLE);
                WishlistAdapter adapter = new WishlistAdapter(requireContext(), list);
                lvSeries.setAdapter(adapter);
                lvSeries.setOnItemClickListener((parent, itemView, position, id) -> {
                    SeriesDB selected = (SeriesDB) parent.getItemAtPosition(position);
                    Series series = convertToSeries(selected);
                    if (listener != null) listener.onSeriesClicked(series);
                });
            }
        });
    }

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
