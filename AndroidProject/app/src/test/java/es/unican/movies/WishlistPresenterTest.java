package es.unican.movies;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import es.unican.movies.DataBaseManagement.SeriesDB;
import es.unican.movies.DataBaseManagement.SeriesDao;

public class WishlistPresenterTest {



    private SeriesDao daoMock;

    @Before
    public void setUp() {

        daoMock = mock(SeriesDao.class);

    }

    @Test
    public void testLoadConListaDeSeries() {
        // UT.1a: El DAO devuelve una lista con varias series
        SeriesDB s1 = new SeriesDB();
        s1.setId(1);
        s1.setName("Dark");
        SeriesDB s2 = new SeriesDB();
        s2.setId(2);
        s2.setName("Lupin");
        List<SeriesDB> lista = Arrays.asList(s1, s2);

        // Simulamos que el DAO devuelve esta lista
        doReturn(lista).when(daoMock).getWishlist();

        // Ejecutamos el metodo


        // Verificamos que la vista recibe la lista

    }

    @Test
    public void testLoadConListaVacia() {
        // UT.1b: El DAO devuelve una lista vacía
        List<SeriesDB> listaVacia = Collections.emptyList();

        doReturn(listaVacia).when(daoMock).getWishlist();



    }

    @Test
    public void testLoadConExcepcion() {
        // UT.1c: El DAO lanza excepción
        doThrow(new RuntimeException("Error de acceso")).when(daoMock).getWishlist();

    }
}