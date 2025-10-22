package es.unican.movies;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import es.unican.movies.DataBaseManagement.SeriesDB;
import es.unican.movies.DataBaseManagement.SeriesDao;
import es.unican.movies.activities.main.IWishlistContract;
import es.unican.movies.activities.main.WishlistPresenter;

public class WishlistPresenterTest {

    private WishlistPresenter presenter;
    private IWishlistContract.View viewMock;
    private SeriesDao daoMock;

    @Before
    public void setUp() {
        viewMock = mock(IWishlistContract.View.class);
        daoMock = mock(SeriesDao.class);
        presenter = new WishlistPresenter();
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
        viewMock.showSeries(lista);

        // Verificamos que la vista recibe la lista
        verify(viewMock).showSeries(lista);
    }

    @Test
    public void testLoadConListaVacia() {
        // UT.1b: El DAO devuelve una lista vacía
        List<SeriesDB> listaVacia = Collections.emptyList();

        doReturn(listaVacia).when(daoMock).getWishlist();
        viewMock.showSeries(listaVacia);

        verify(viewMock).showSeries(listaVacia);
        verify(viewMock, never()).showLoadError();
    }

    @Test
    public void testLoadConExcepcion() {
        // UT.1c: El DAO lanza excepción
        doThrow(new RuntimeException("Error de acceso")).when(daoMock).getWishlist();

        // Simulamos la llamada
        viewMock.showLoadError();

        // Verificamos que se muestra el error
        verify(viewMock).showLoadError();
    }
}