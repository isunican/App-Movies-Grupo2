package es.unican.movies.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.unican.movies.R;
import es.unican.movies.activities.details.DetailsView;
import es.unican.movies.activities.info.InfoActivity;
import es.unican.movies.model.Movie;
import es.unican.movies.service.IMoviesRepository;

/**
 * Activity to show the list of movies.
 */
@AndroidEntryPoint
public class MainView extends AppCompatActivity implements IMainContract.View {

    /**
     * Presenter that will take control of this view.
     */
    private IMainContract.Presenter presenter;

    /**
     * Repository that can be used to retrieve movies or series.
     */
    @Inject
    IMoviesRepository repository;

    /**
     * Reference to the ListView that shows the list of movies
     */
    private ListView lvMovies; // Added ListView member

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The default theme does not include a toolbar.
        // In this app the toolbar is explicitly declared in the layout
        // This sets this toolbar as the activity ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // instantiate presenter, let it take control
        presenter = new MainPresenter();
        presenter.init(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * This is called when an item in the action bar menu is selected.
     * @param item The menu item that was selected.
     *
     * @return true if we have handled the selection
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menuItemInfo) {
            presenter.onMenuInfoClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void init() {
        lvMovies = findViewById(R.id.lvMovies);
        lvMovies.setOnItemClickListener((parent, view, position, id) -> {
            Movie movie = (Movie) parent.getItemAtPosition(position);
            presenter.onItemClicked(movie);
        });
    }

    @Override
    public IMoviesRepository getMoviesRepository() {
        return repository;
    }

    @Override
    public void showMovies(List<Movie> movies) {
        MovieAdapter adapter = new MovieAdapter(this, movies);
        lvMovies.setAdapter(adapter);
    }

    @Override
    public void showLoadCorrect(int movies) {
        Toast.makeText(this, "Loaded " + movies + " movies", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadError() {
        Toast.makeText(this, "Error loading movies", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMovieDetails(Movie movie) {
        Intent intent = new Intent(this, DetailsView.class);
        intent.putExtra(DetailsView.INTENT_MOVIE, Parcels.wrap(movie));
        startActivity(intent);
    }

    @Override
    public void showInfoActivity() {
        startActivity(new Intent(this, InfoActivity.class));
    }
}