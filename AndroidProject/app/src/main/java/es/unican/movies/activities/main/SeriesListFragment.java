package es.unican.movies.activities.main;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import es.unican.movies.R;
import es.unican.movies.model.Series;

public class SeriesListFragment extends Fragment {

    public interface Listener {
        void onSeriesClicked(Series series);
    }

    private Listener listener;
    private ListView lvSeries;
    private List<Series> pendingSeries = null;

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
        // If there is pending data, apply it now
        if (pendingSeries != null) {
            applySeriesToList(pendingSeries);
            pendingSeries = null;
        }
        return root;
    }

    public void setSeries(List<Series> series) {
        // store or apply depending on view state
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
}
