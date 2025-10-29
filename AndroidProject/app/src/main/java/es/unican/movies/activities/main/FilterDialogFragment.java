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

import es.unican.movies.R;

public class FilterDialogFragment extends DialogFragment {

    private ListView listView;
    private String[] genres;

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

        Button btnAceptar = v.findViewById(R.id.btnAceptar);
        btnAceptar.setOnClickListener(view -> {ArrayList<String> seleccionados = new ArrayList<>();
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
