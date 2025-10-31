package es.unican.movies.activities.main;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import es.unican.movies.R;

public class FilterDialogFragment extends DialogFragment {

    private static final String SELECTED_GENRES = "selected_genres";
    private ListView listView;
    private String[] genres;
    private List<String> selectedGenres;

    public static FilterDialogFragment newInstance(ArrayList<String> selectedGenres) {
        FilterDialogFragment fragment = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putStringArrayList(SELECTED_GENRES, selectedGenres);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            selectedGenres = getArguments().getStringArrayList(SELECTED_GENRES);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_filter, container, false);

        listView = v.findViewById(R.id.listViewGeneros);
        genres = getResources().getStringArray(R.array.genres);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_list_item_multiple_choice,
                genres
        );
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        if (selectedGenres != null) {
            for (int i = 0; i < genres.length; i++) {
                if (selectedGenres.contains(genres[i])) {
                    listView.setItemChecked(i, true);
                }
            }
        }

        Button btnAceptar = v.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(view -> {
            ArrayList<String> seleccionados = new ArrayList<>();
            for (int i = 0; i < genres.length; i++) {
                if (listView.isItemChecked(i)) {
                    seleccionados.add(genres[i]);
                }
            }
            Bundle result = new Bundle();
            result.putStringArrayList("genres", seleccionados);
            getParentFragmentManager().setFragmentResult("filter_key", result);
            dismiss();
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog d = getDialog();

        if (d != null && d.getWindow() != null) {
            int width = (int) (requireContext().getResources().getDisplayMetrics().widthPixels * 0.85);
            d.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }
}
