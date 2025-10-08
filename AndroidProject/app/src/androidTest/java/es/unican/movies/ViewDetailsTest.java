package es.unican.movies;




import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;
import es.unican.movies.injection.RepositoriesModule;
import es.unican.movies.model.Series;





import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.anything;

import static es.unican.movies.utils.MockRepositories.getTestRepository;

import es.unican.movies.activities.main.MainView;
import es.unican.movies.service.IMoviesRepository;

@RunWith(AndroidJUnit4.class)
@UninstallModules(RepositoriesModule.class)
@HiltAndroidTest
public class ViewDetailsTest {

    @Rule(order = 0)  // the Hilt rule must execute first
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    // I need the context to access resources, such as the json with test movies or tv series
    final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    // Mock repository that provides data from a JSON file instead of downloading it from the internet.
    @BindValue
    final IMoviesRepository repository = getTestRepository(context, R.raw.sample_series);

    @Test
    public void viewDetailsSuccess() {
        onData(anything())
                .inAdapterView(withId(R.id.lvSeries))
                .atPosition(0)
                .perform(click());

        onView(withId(R.id.tvTitulo)).check(matches(withText("El Pacificador")));
        onView(withId(R.id.tvFechaUERellenar)).check(matches(withText("2022")));
        onView(withId(R.id.tvFechaPERellenar)).check(matches(withText("2025-09-25")));
        onView(withId(R.id.tvTempRellenar)).check(matches(withText("2")));
        onView(withId(R.id.tvCaptRellenar)).check(matches(withText("16")));
        onView(withId(R.id.tvGenerosRellenar)).check(matches(withText(
                "Action & Adventure, Sci-Fi & Fantasy, Drama"
        )));
        onView(withId(R.id.tvPuntSumariaRellenar)).check(matches(withText("8.20")));
        onView(withId(R.id.tvPuntMediaRellenar)).check(matches(withText("7.58")));

    }


    @Test
    public void viewDetailsMissingData() {
        ActivityScenario.launch(MainView.class);

        onData(anything())
                .inAdapterView(withId(R.id.lvSeries))
                .atPosition(1)
                .check(matches(withText("Serie Incompleta")));

        onData(anything())
                .inAdapterView(withId(R.id.lvSeries))
                .atPosition(1)
                .perform(click());

        onView(withId(R.id.lvSeries)).check(doesNotExist());

        onView(withId(R.id.tvTitle)).check(matches(withText("Serie Incompleta")));
        onView(withId(R.id.tvFechaPEBase)).check(matches(withText("-")));
        onView(withId(R.id.tvFechaUEBase)).check(matches(withText("-")));
        onView(withId(R.id.tvNumTempBase)).check(matches(withText("-")));
        onView(withId(R.id.tvNumCapBase)).check(matches(withText("-")));
        onView(withId(R.id.tvGenerosBase)).check(matches(withText("-")));
        onView(withId(R.id.tvPuntSumariaBase)).check(matches(withText("-")));
        onView(withId(R.id.tvPuntMediaBase)).check(matches(withText("-")));
    }

    @Test
    public void viewDetailsAnomalousData() {
        ActivityScenario.launch(MainView.class);

        onData(anything())
                .inAdapterView(withId(R.id.lvSeries))
                .atPosition(1)
                .check(matches(withText("Serie Anómala")));

        onData(anything())
                .inAdapterView(withId(R.id.lvSeries))
                .atPosition(1)
                .perform(click());

        onView(withId(R.id.lvSeries)).check(doesNotExist());

        onView(withId(R.id.tvTitle)).check(matches(withText("Serie Anómala")));
        onView(withId(R.id.tvFechaPEBase)).check(matches(withText("-")));
        onView(withId(R.id.tvFechaUEBase)).check(matches(withText("-")));
        onView(withId(R.id.tvNumTempBase)).check(matches(withText("-")));
        onView(withId(R.id.tvNumCapBase)).check(matches(withText("-")));
        onView(withId(R.id.tvGenerosBase)).check(matches(withText("-")));
        onView(withId(R.id.tvPuntSumariaBase)).check(matches(withText("-")));
        onView(withId(R.id.tvPuntMediaBase)).check(matches(withText("-")));
    }
}
