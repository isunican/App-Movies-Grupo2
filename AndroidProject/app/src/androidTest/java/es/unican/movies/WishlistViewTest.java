package es.unican.movies;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;
import es.unican.movies.DataBaseManagement.SeriesDao;
import es.unican.movies.DataBaseManagement.SeriesDatabase;
import es.unican.movies.activities.main.MainView;
import es.unican.movies.di.DatabaseModule;
import es.unican.movies.injection.RepositoriesModule;
import es.unican.movies.service.IMoviesRepository;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static es.unican.movies.utils.MockRepositories.getTestRepository;


@UninstallModules({RepositoriesModule.class, DatabaseModule.class})
@HiltAndroidTest
public class WishlistViewTest {

    @Rule(order = 0)  // the Hilt rule must execute first
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    // I need the context to access resources, such as the json with test movies or tv series
    final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    // Mock repository that provides data from a JSON file instead of downloading it from the internet.
    @BindValue
    final IMoviesRepository repository = getTestRepository(context, R.raw.sample_series);

    private SeriesDao dao;
    private SeriesDatabase db;

    @Before
    public void setUp() {
        hiltRule.inject();

        db = Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                SeriesDatabase.class
        ).allowMainThreadQueries().build();

        dao = db.seriesDao();
    }-

    /*@Test
    public void testMostrarListaDeseadaValida() {
        SeriesDB s1 = new SeriesDB();
        s1.setId(1);
        s1.setName("El Pacificador");
        s1.setPosterPath(ITmdbApi.getFullImagePath("/gpGPI732PWspwGpPyWrWov8IYmq.jpg", EImageSize.W92));

        SeriesDB s2 = new SeriesDB();
        s2.setId(2);
        s2.setName("Los Simpson");
        s2.setPosterPath(ITmdbApi.getFullImagePath("/gpGPI732PWspwGpPyWrWov8IYmq.jpg", EImageSize.W92));

        dao.addToWishlist(s1);
        dao.addToWishlist(s2);

        onView(withId(R.id.nav_wishlist)).perform(click());
        onView(withId(R.id.lvWishlist)).check(matches(isDisplayed()));
        onView(withText("El Pacificador")).check(matches(isDisplayed()));
        onView(withText("Los Simpson")).check(matches(isDisplayed()));
    }*/

    @Test
    public void testMostrarListaDeseadaVacia() {
        // Simula que no hay series en la lista de deseados

        // Navega a la vista de deseados
        onView(withId(R.id.nav_wishlist)).perform(click());

        // Verifica que se muestra el mensaje de lista vacía
        onView(withText("Aún no tienes series en tu lista de deseados"))
                .check(matches(isDisplayed()));

        // Verifica que la lista está visible (aunque esté vacía)
        onView(withId(R.id.lvWishlist))
                .check(matches(not(isDisplayed())));
    }
}
