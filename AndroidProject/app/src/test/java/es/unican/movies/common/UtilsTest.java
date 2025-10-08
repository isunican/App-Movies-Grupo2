package es.unican.movies.common;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UtilsTest extends TestCase {
    @Test
    public void obtenerPuntuacion_isCorrect(){
        String resultado = Utils.obtenerPuntuacionSumaria(-5, 8.2);
        assertEquals("-", resultado);
    }
}