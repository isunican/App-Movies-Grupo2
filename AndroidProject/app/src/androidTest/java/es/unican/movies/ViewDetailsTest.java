package es.unican.movies;




import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.unican.movies.model.Series;





import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.allOf;

import es.unican.movies.activities.main.MainView;

@RunWith(AndroidJUnit4.class)
public class ViewDetailsTest {
    @Test
    public void viewSSuccess() {
        ActivityScenario.launch(MainView.class);

        onData(anything())
                .inAdapterView(withId(R.id.lvSeries))
                .atPosition(1)
                .onChildView(withId(R.id.txtNombreSerieItem))
                .check(matches(withText("El Pacificador")));

        onData(anything())
                .inAdapterView(withId(R.id.lvSeries))
                .atPosition(1)
                .perform(click());

        onView(withId(R.id.lvSeries)).check(doesNotExist());

        onView(withId(R.id.txtNombreSerieDet)).check(matches(withText("El Pacificador")));
        onView(withId(R.id.txtAnyoPrimeraEmision)).check(matches(withText("2022")));
        onView(withId(R.id.txtFechaUltimoCapitulo)).check(matches(withText("2025-09-25")));
        onView(withId(R.id.txtNumTemporadas)).check(matches(withText("2")));
        onView(withId(R.id.txtNumEpisodios)).check(matches(withText("16")));
        onView(withId(R.id.txtGeneros)).check(matches(withText("Action & Adventure, Sci-Fi & Fantasy, Drama")));
        onView(withId(R.id.imgPoster)).check(matches(isDisplayed()));
        onView(withId(R.id.txtVoteAverage)).check(matches(withText("8.20")));
        onView(withId(R.id.txtSummaryScore)).check(matches(withText("7.58")));
    }

    @Test
    public void viewMissingData() {
        ActivityScenario.launch(MainView.class);

        onData(anything())
                .inAdapterView(withId(R.id.lvSeries))
                .atPosition(1)
                .onChildView(withId(R.id.txtNombreSerieItem))
                .check(matches(withText("Serie Incompleta")));

        onData(anything())
                .inAdapterView(withId(R.id.lvSeries))
                .atPosition(1)
                .perform(click());

        onView(withId(R.id.lvSeries)).check(doesNotExist());

        onView(withId(R.id.txtNombreSerieDet)).check(matches(withText("Serie Incompleta")));
        onView(withId(R.id.txtAnyoPrimeraEmision)).check(matches(withText("-")));
        onView(withId(R.id.txtFechaUltimoCapitulo)).check(matches(withText("-")));
        onView(withId(R.id.txtNumTemporadas)).check(matches(withText("-")));
        onView(withId(R.id.txtNumEpisodios)).check(matches(withText("-")));
        onView(withId(R.id.txtGeneros)).check(matches(withText("-")));
        onView(withId(R.id.txtVoteAverage)).check(matches(withText("-")));
        onView(withId(R.id.txtSummaryScore)).check(matches(withText("-")));
    }

    @Test
    public void viewAnomalousData() {
        ActivityScenario.launch(MainView.class);

        onData(anything())
                .inAdapterView(withId(R.id.lvSeries))
                .atPosition(1)
                .onChildView(withId(R.id.txtNombreSerieItem))
                .check(matches(withText("Serie Anómala")));

        onData(anything())
                .inAdapterView(withId(R.id.lvSeries))
                .atPosition(1)
                .perform(click());

        onView(withId(R.id.lvSeries)).check(doesNotExist());

        onView(withId(R.id.txtNombreSerieDet)).check(matches(withText("Serie Anómala")));
        onView(withId(R.id.txtAnyoPrimeraEmision)).check(matches(withText("-")));
        onView(withId(R.id.txtFechaUltimoCapitulo)).check(matches(withText("-")));
        onView(withId(R.id.txtGeneros)).check(matches(withText("-")));
        onView(withId(R.id.txtNumTemporadas)).check(matches(withText("-")));
        onView(withId(R.id.txtNumEpisodios)).check(matches(withText("-")));
        onView(withId(R.id.txtVoteAverage)).check(matches(withText("-")));
        onView(withId(R.id.txtSummaryScore)).check(matches(withText("-")));
    }
}
