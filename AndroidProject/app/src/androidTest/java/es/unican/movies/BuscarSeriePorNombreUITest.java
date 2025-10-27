package es.unican.movies;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;

import static es.unican.movies.utils.MockRepositories.getTestRepository;

import android.content.Context;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;

import es.unican.movies.activities.main.MainView;
import es.unican.movies.injection.RepositoriesModule;
import es.unican.movies.service.IMoviesRepository;

@UninstallModules(RepositoriesModule.class)
@HiltAndroidTest
public class BuscarSeriePorNombreUITest {

    @Rule(order = 0)
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @BindValue
    final IMoviesRepository repository = getTestRepository(context, R.raw.sample_series);

    /**
     * Para la realizacion de estos test de interfaz, se ha seguido el esquema de click en la lupa, escribir el texto y hacer comprobaciones.
     */
    @Test
    public void BuscarSerieExito() {
        onView(withId(R.id.searchTitle)).perform(click());

        onView(withId(R.id.searchTitle))
                .perform(typeText("El Pacificador"), closeSoftKeyboard());

        onData(anything())
                .inAdapterView(withId(R.id.lvSeries))
                .atPosition(0)
                .onChildView(withId(R.id.tvTitle))
                .check(matches(withText("El Pacificador")));
    }

    @Test
    public void BuscarSerieNoEncontrada() {
        onView(withId(R.id.searchTitle)).perform(click());


        onView(withId(R.id.searchTitle))
                .perform(typeText("TESTEADOR"), closeSoftKeyboard());

        onView(withId(R.id.lvSeries)).check(matches(hasChildCount(0)));

    }
}