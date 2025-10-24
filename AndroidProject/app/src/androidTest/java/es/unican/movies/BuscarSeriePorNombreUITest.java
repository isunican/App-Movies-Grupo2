package es.unican.movies;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.allOf;

import static es.unican.movies.utils.MockRepositories.getTestRepository;

import android.content.Context;
import android.widget.EditText;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;

import es.unican.movies.activities.main.MainView;
import es.unican.movies.injection.RepositoriesModule;
import es.unican.movies.service.IMoviesRepository;
import es.unican.movies.utils.MockRepositories;

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

        onView(allOf(isAssignableFrom(EditText.class), isDisplayed()))
                .perform(click(), typeText("El Pacificador"), closeSoftKeyboard());


        onData(anything())
                .inAdapterView(allOf(withId(R.id.lvSeries), isDisplayed()))
                .atPosition(0)
                .onChildView(withId(R.id.tvTitle))
                .check(matches(withText("El Pacificador")));
    }


    @Test
    public void BuscarSerieNoEncontrada() {

        onView(allOf(isAssignableFrom(EditText.class), isDisplayed()))
                .perform(click(), typeText("TESTEADOR"), closeSoftKeyboard());


        onView(withId(R.id.lvSeries))
                .check(matches(hasChildCount(0)));
    }
}