package es.unican.movies.activities.main;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import es.unican.movies.R;

public class OrdenDialogFragment extends DialogFragment {

    private Spinner spinnerTipoOrden;
    private RadioGroup radioGroupOrden;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflamos nuestro layout personalizado
        View v = inflater.inflate(R.layout.dialog_ordenar, container, false);

        spinnerTipoOrden = v.findViewById(R.id.spinnerTipoOrden);
        radioGroupOrden = v.findViewById(R.id.radioGroupOrden);
        Button btnAplicar = v.findViewById(R.id.btnAplicarOrden);

        // Configurar el spinner con el array de strings
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.tipo_orden,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoOrden.setAdapter(adapter);

        // Listener del botón aplicar
        btnAplicar.setOnClickListener(view -> {
            String tipo = spinnerTipoOrden.getSelectedItem().toString();
            boolean ascendente = (radioGroupOrden.getCheckedRadioButtonId() == R.id.radioAsc);

            // Enviar el resultado al fragmento o activity que llame al diálogo
            Bundle result = new Bundle();
            result.putString("tipo", tipo);
            result.putBoolean("ascendente", ascendente);
            getParentFragmentManager().setFragmentResult("orden_key", result);

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
