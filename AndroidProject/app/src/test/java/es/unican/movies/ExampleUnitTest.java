package es.unican.movies;

import org.junit.Test;

import static org.junit.Assert.*;

import es.unican.movies.model.Movie;
import es.unican.movies.model.Series;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void seriesName_isCorrect() {
        Series series = new Series();
        series.setName("La Mega Película 2.0");
        assertEquals("La Mega Película 2.0", series.getName());
        assertNull(series.getPosterPath());
    }
}