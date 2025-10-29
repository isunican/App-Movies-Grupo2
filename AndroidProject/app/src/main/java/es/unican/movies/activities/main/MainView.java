package es.unican.movies.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.unican.movies.R;
import es.unican.movies.activities.details.DetailsSeriesView;
import es.unican.movies.activities.info.InfoActivity;
import es.unican.movies.model.Series;
import es.unican.movies.service.IMoviesRepository;
import hilt_aggregated_deps._es_unican_movies_activities_main_MainView_GeneratedInjector;

/**
 * Activity to show the list of series and host fragments.
 */
@AndroidEntryPoint
public class MainView extends AppCompatActivity implements IMainContract.View, SeriesListFragment.Listener {

    /**
     * Presenter that will take control of this view.
     */
    private IMainContract.Presenter presenter;

    /**
     * SearchView for filtering series by title.
     */
    private SearchView searchView;

    /**
     * Repository that can be used to retrieve movies or series.
     */
    @Inject
    IMoviesRepository repository;

    private static final String TAG_LIST = "series_list";

    // toast
    private Toast currentToast;

    // cache the last series list so we can reapply it when fragments are recreated
    private List<Series> cachedSeries = null;

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menuItemInfo) {
            presenter.onMenuInfoClicked();
            return true;
        } else if (itemId == R.id.action_filter) {
            new es.unican.movies.activities.main.FilterDialogFragment().show(getSupportFragmentManager(), "DialogFilter");
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void init() {
        // Initialize fragment container with the series list fragment by default
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        SeriesListFragment listFragment = (SeriesListFragment) fm.findFragmentByTag(TAG_LIST);
        if (listFragment == null) {
            listFragment = new SeriesListFragment();
            tx.add(R.id.fragment_container, listFragment, TAG_LIST);
        }
        tx.commitNowAllowingStateLoss();

        // Wire BottomNavigationView// Wire BottomNavigationView
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setOnItemSelectedListener(item -> {
                int id = item.getItemId();
                FragmentTransaction t = getSupportFragmentManager().beginTransaction();
                FragmentManager fm2 = getSupportFragmentManager();

                SeriesListFragment sf = (SeriesListFragment) fm2.findFragmentByTag(TAG_LIST);

                if (id == R.id.nav_home) {
                    // show list fragment in ALL mode
                    if (sf == null) {
                        sf = new SeriesListFragment();
                        t.add(R.id.fragment_container, sf, TAG_LIST);
                    } else {
                        t.show(sf);
                    }
                    sf.setMode(SeriesListFragment.MODE_ALL);
                    t.commitNowAllowingStateLoss();

                    if (cachedSeries != null && sf != null) {
                        sf.setSeries(cachedSeries);
                    }
                    return true;
                } else if (id == R.id.nav_wishlist) {
                    // show wishlist inside the same fragment
                    if (sf == null) {
                        sf = new SeriesListFragment();
                        t.add(R.id.fragment_container, sf, TAG_LIST);
                    } else {
                        t.show(sf);
                    }
                    sf.setMode(SeriesListFragment.MODE_WISHLIST);
                    t.commitNowAllowingStateLoss();
                    return true;
                }
                return false;
            });
            // set default selected
            bottomNav.setSelectedItemId(R.id.nav_home);
        }

        // Wire search bar
        SearchTitleBarHandler();
    }

    private void SearchTitleBarHandler(){
        searchView = findViewById(R.id.searchTitle);
        if (searchView == null) return;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // Is called when the user submits the query
            @Override
            public boolean onQueryTextSubmit(String query) {
                // we don't need to do anything special on submit
                return true;
            }

            // Is called when the text in the search bar changes
            @Override
            public boolean onQueryTextChange(String title) {
                if (title != null) {
                    presenter.onSearchBarContentChanged(title);
                } else {
                    presenter.onSearchBarContentChanged("");
                }
                return true;
            }
        });
    }

    @Override
    public IMoviesRepository getMoviesRepository() {
        return repository;
    }

    @Override
    public void showSeries(List<Series> series) {
        // cache the list so we can reapply it when returning from other tabs
        this.cachedSeries = series;

        // Forward the series to the fragment (if present)
        SeriesListFragment fragment = (SeriesListFragment) getSupportFragmentManager().findFragmentByTag(TAG_LIST);
        if (fragment != null) {
            fragment.setSeries(series);
        } else {
            // If fragment not present, create it and pass the data after it's attached
            SeriesListFragment listFragment = new SeriesListFragment();
            FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
            tx.add(R.id.fragment_container, listFragment, TAG_LIST);
            tx.commitNowAllowingStateLoss();
            listFragment.setSeries(series);
        }
    }


    @Override
    public void showLoadCorrect(int series) {
        Toast.makeText(this, "Loaded " + series + " series", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadError() {
        Toast.makeText(this, "Error loading series", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showSearchErrorNotFound() {
        if (currentToast != null) {
            currentToast.cancel(); // Cancela el anterior si sigue visible
        }
        currentToast = Toast.makeText(this, "No se encontraron coincidencias", Toast.LENGTH_SHORT);
        currentToast.show();}

    @Override
    public void showSeriesDetails(Series series) {
        Intent intent = new Intent(this, DetailsSeriesView.class);
        intent.putExtra(DetailsSeriesView.INTENT_MOVIE, Parcels.wrap(series));
        startActivity(intent);
    }

    @Override
    public void showInfoActivity() {
        startActivity(new Intent(this, InfoActivity.class));
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onSeriesClicked(Series series) {
        presenter.onItemClicked(series);
    }


}