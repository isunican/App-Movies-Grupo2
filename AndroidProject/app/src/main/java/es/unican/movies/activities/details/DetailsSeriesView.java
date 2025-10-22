package es.unican.movies.activities.details;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import es.unican.movies.R;
import es.unican.movies.common.Utils;
import es.unican.movies.model.Series;
import es.unican.movies.service.EImageSize;
import es.unican.movies.service.ITmdbApi;

/**
 * Activity to show the details of a TV series.
 */
public class DetailsSeriesView extends AppCompatActivity implements IDetailsContract.DetailsView {

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
        showDetailContent();
    }
    @Override
    public void showError(String message) {
        // TODO
    }



    @Override
    public void showDetailContent(){

        Series series = Parcels.unwrap(getIntent().getExtras().getParcelable(INTENT_MOVIE));
        assert series != null;



        String generosARellenar = Utils.generateStringFromList(series.getGenres(), ", ");

        TextView tvTitle = findViewById(R.id.tvTitulo);
        TextView tvVoteAverage = findViewById(R.id.tvPuntMediaRellenar);
        TextView sumariaVotes =  findViewById(R.id.tvPuntSumariaRellenar);
        ImageView ivPoster = findViewById(R.id.fotoSerie);
        TextView fechaPE = findViewById(R.id.tvFechaPERellenar);
        TextView fechaUE = findViewById(R.id.tvFechaUERellenar);
        TextView chapters = findViewById(R.id.tvCaptRellenar);
        TextView seasons = findViewById(R.id.tvTempRellenar);
        TextView genres = findViewById(R.id.tvGenerosRellenar);

        String puntuacion = Utils.obtenerPuntuacionSumaria(series.getVoteCount(), series.getVoteAverage());
        String imageUrl = ITmdbApi.getFullImagePath(series.getPosterPath(), EImageSize.W92);
        Picasso.get().load(imageUrl).fit().centerInside().into(ivPoster);



        genres.setText(generosARellenar);
        tvTitle.setText(series.getName());
        tvVoteAverage.setText(String.valueOf(series.getVoteAverage()));
        sumariaVotes.setText(puntuacion);
        fechaPE.setText(series.getFirstAirDate());
        fechaUE.setText(series.getLastAirDate());
        chapters.setText(String.valueOf(series.getNumberOfEpisodes()));
        seasons.setText(String.valueOf(series.getNumberOfSeasons()));
    }


}