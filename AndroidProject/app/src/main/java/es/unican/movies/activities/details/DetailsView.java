package es.unican.movies.activities.details;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.parceler.Parcels;

import es.unican.movies.R;
import es.unican.movies.model.Series;

/**
 * Activity to show the details of a TV series.
 */
public class DetailsView extends AppCompatActivity {

    public static final String INTENT_MOVIE = "INTENT_MOVIE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Series series = Parcels.unwrap(getIntent().getExtras().getParcelable(INTENT_MOVIE));
        assert series != null;
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvId = findViewById(R.id.tvId);
        tvTitle.setText(series.getName());
        tvId.setText(String.valueOf(series.getId()));
        
    }
}