package es.unican.movies;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

public class TestUnitarias {
    @Test
    public void obtenerPuntuacion_isCorrect(){
        String resultado = series.obtenerPuntuacionSumaria(8.2, 3019);
        assertEquals("7.58", resultado);
    }
}
