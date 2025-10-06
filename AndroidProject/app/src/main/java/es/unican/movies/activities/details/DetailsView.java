package es.unican.movies.activities.details;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.parceler.Parcels;

import es.unican.movies.R;
import es.unican.movies.activities.main.MainPresenter;
import es.unican.movies.model.Series;

/**
 * Activity to show the details of a TV series.
 */
public class DetailsView extends AppCompatActivity implements IDetailsContract.DetailsView {

    public static final String INTENT_MOVIE = "INTENT_MOVIE";

    DetailsPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        // instantiate presenter, let it take control
        presenter = new DetailsPresenter();
        presenter.init(this);


        
    }

    @Override
    public void init() {
        Series series = Parcels.unwrap(getIntent().getExtras().getParcelable(INTENT_MOVIE));
        assert series != null;
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvId = findViewById(R.id.tvId);
        tvTitle.setText(series.getName());
        tvId.setText(String.valueOf(series.getId()));
    }
    @Override
    public void showError(String message) {
        // TODO
    }



    @Override
    public void ShowDetailContent(Series serie){

    }


}