package es.unican.movies;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;
import static org.mockito.Mockito.*;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import es.unican.movies.activities.main.IMainContract;
import es.unican.movies.activities.main.MainPresenter;
import es.unican.movies.model.Series;
import es.unican.movies.service.ICallback;
import es.unican.movies.service.IMoviesRepository;

@RunWith(AndroidJUnit4.class)
public class SearchBarContentUnitTest {

    @Mock private IMainContract.View view;
    @Mock private IMoviesRepository repo;

    @Captor private ArgumentCaptor<ICallback<List<Series>>> CallBack;
    @Captor private ArgumentCaptor<List<Series>> listaSeries;

    private MainPresenter sut;

    private MockedStatic<Log> logMock;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        logMock = mockStatic(android.util.Log.class);
        when(view.getMoviesRepository()).thenReturn(repo);
        sut = new MainPresenter();
        sut.init(view);
        clearInvocations(repo, view);
    }

    @After
    public void tearDown() {
        logMock.close();
    }

    /**
     * Para la realización de estos test se usa el "Captor" para capturar el CallBack que el
     * presenter envía al repositorio, simulando la respuesta del repository (onSuccess, onFailure)
     * pudiendo así simular que series devuelve el repository con la respuesta dada. Y así, mediante otro
     * "Captor" capturar la lista que devuelve a la vista y hacer asi las comprobaciones necesarias con
     * asserts.
     */

    /**
     * UT1A-Búsqueda valida escribiendo el nombre completo de la serie.
     */
    @Test
    public void BusquedaValida() {
        sut.onSearchBarContentChanged("El Pacificador");

        verify(repo).requestAggregateSeries(CallBack.capture());
        ICallback<List<Series>> cb = CallBack.getValue();
        cb.onSuccess(new ArrayList<>(Arrays.asList(
                serie("El Pacificador", "The Peacemaker"),
                serie("El Padrino", "The Godfather")
        )));

        verify(view).showSeries(listaSeries.capture());
        List<Series> mostradas = listaSeries.getValue();

        Assert.assertEquals(1, mostradas.size());
        Assert.assertEquals("El Pacificador", mostradas.get(0).getName());
        verify(view).showLoadCorrect(1);
        verify(view, never()).showLoadError();
    }
    /**
     * UT1B-Búsqueda sin ningúna coincidncia de series con ese nombre (lista vacía).
     */
    @Test
    public void BusquedaSerieNoExistente() {
        sut.onSearchBarContentChanged("TESTEAR");

        verify(repo).requestAggregateSeries(CallBack.capture());
        ICallback<List<Series>> cb = CallBack.getValue();

        cb.onSuccess(new ArrayList<>(Arrays.asList(
                serie("El Pacificador", "The Peacemaker"),
                serie("El Padrino", "The Godfather"),
                serie("Sherlock", "Sherlock")
        )));

        verify(view).showSeries(listaSeries.capture());
        List<Series> mostradas = listaSeries.getValue();

        Assert.assertTrue(mostradas.isEmpty());
        verify(view).showLoadCorrect(0);
        verify(view, never()).showLoadError();
    }

    /**
     * UT1C-Búsqueda válida con nombre de la seire en mayúsculas.
     */
    @Test
    public void BusquedaValidaMayusqulas() {
        sut.onSearchBarContentChanged("EL PACIFICADOR");

        verify(repo).requestAggregateSeries(CallBack.capture());
        ICallback<List<Series>> cb = CallBack.getValue();

        cb.onSuccess(new ArrayList<>(Arrays.asList(
                serie("El Pacificador", "The Peacemaker"),
                serie("El Padrino", "The Godfather")
        )));

        verify(view).showSeries(listaSeries.capture());
        List<Series> mostradas = listaSeries.getValue();

        Assert.assertEquals(1, mostradas.size());
        Assert.assertEquals("El Pacificador", mostradas.get(0).getName());
        verify(view).showLoadCorrect(1);
        verify(view, never()).showLoadError();
    }

    /**
     * UT1D-Búsqueda sin ningun nombre, devuelve lista con todas las series
     */
    @Test
    public void BusquedaVacia() {
        sut.onSearchBarContentChanged("");

        verify(repo).requestAggregateSeries(CallBack.capture());
        ICallback<List<Series>> cb = CallBack.getValue();

        cb.onSuccess(new ArrayList<>(Arrays.asList(
                serie("El Pacificador", "The Peacemaker"),
                serie("El Padrino", "The Godfather"),
                serie("Sherlock", "Sherlock"))));

        verify(view).showSeries(listaSeries.capture());
        List<Series> mostradas = listaSeries.getValue();

        Assert.assertEquals(3, mostradas.size());
        Assert.assertEquals("El Pacificador", mostradas.get(0).getName());
        Assert.assertEquals("El Padrino", mostradas.get(1).getName());
        Assert.assertEquals("Sherlock", mostradas.get(2).getName());
        verify(view).showLoadCorrect(3);
        verify(view, never()).showLoadError();
    }

    /**
     * UT1E-Búsqueda con nombre parcialmente escrito, devuelve dos series
     */
    @Test
    public void BusquedaParcial() {
        sut.onSearchBarContentChanged("El Pa");

        verify(repo).requestAggregateSeries(CallBack.capture());
        ICallback<List<Series>> cb = CallBack.getValue();

        cb.onSuccess(new ArrayList<>(Arrays.asList(
                serie("El Pacificador", "The Peacemaker"),
                serie("El Padrino", "The Godfather"),
                serie("Sherlock", "Sherlock")
        )));

        verify(view).showSeries(listaSeries.capture());
        List<Series> mostradas = listaSeries.getValue();

        Assert.assertEquals(2, mostradas.size());
        Assert.assertEquals("El Pacificador", mostradas.get(0).getName());
        Assert.assertEquals("El Padrino", mostradas.get(1).getName());
        verify(view).showLoadCorrect(2);
        verify(view, never()).showLoadError();
    }

    /**
     * UT1H- Búsqueda con titulo original de la serie
     */
    @Test
    public void BusquedaValidaTituloOriginal() {
        sut.onSearchBarContentChanged("The Peacemaker");

        verify(repo).requestAggregateSeries(CallBack.capture());
        ICallback<List<Series>> cb = CallBack.getValue();

        cb.onSuccess(new ArrayList<>(Arrays.asList(
                serie("El Pacificador", "The Peacemaker"),
                serie("El Padrino", "The Godfather")
        )));

        verify(view).showSeries(listaSeries.capture());
        List<Series> mostradas = listaSeries.getValue();

        Assert.assertEquals(1, mostradas.size());
        Assert.assertEquals("El Pacificador", mostradas.get(0).getName());
        verify(view).showLoadCorrect(1);
        verify(view, never()).showLoadError();
    }

    /**
     * UT1Z-Error en BD al hacer la búsqueda
     */
    @Test
    public void BusquedaErrorBD() {
        sut.onSearchBarContentChanged("cualquier entrada");

        verify(repo).requestAggregateSeries(CallBack.capture());
        ICallback<List<Series>> cb = CallBack.getValue();

        cb.onFailure(new RuntimeException("fallo simulado"));

        verify(view).showLoadError();
        verify(view, never()).showLoadCorrect(anyInt());
        verify(view, never()).showSeries(anyList());
    }

    private Series serie(String name, String originalName) {
        Series s = new Series();
        s.setName(name);
        s.setOriginalTitle(originalName);
        return s;
    }
}