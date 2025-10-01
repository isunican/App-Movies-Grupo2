package es.unican.movies;

import org.junit.Test;

import static org.junit.Assert.*;

import es.unican.movies.model.Movie;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void movieTitle_isCorrect() {
        Movie movie = new Movie();
        movie.setTitle("La Mega Película 2.0");
        assertEquals("La Mega Película 2.0", movie.getTitle());
        assertNull(movie.getPosterPath());
    }
}