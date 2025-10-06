package es.unican.movies.common;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

import es.unican.movies.model.Movie;
import es.unican.movies.model.Series;

/**
 * Utility class with helper methods.
 */
public class Utils {

    /**
     * Parse a JSON resource file into a list of series.
     * @param context the context
     * @param jsonId the id of the JSON resource file
     * @return a list of movies
     */
    public static List<Series> parseSeries(Context context, int jsonId) {
        InputStream is = context.getResources().openRawResource(jsonId);
        Type typeToken = new TypeToken<List<Series>>() { }.getType();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return new GsonBuilder()
                .create()
                .fromJson(reader, typeToken);
    }

    /**
     * Método para obtener la puntuación sumaria de una película o serie.
     * Si voteCount o voteAverage son negativos, se devuelve "-".
     * Si no, devuelve el String de la puntuación sumaria calculada.
     * @param voteCount Valor entero del número de votos proveniente del json.
     * @param voteAverage Valor double de la media de votos proveniente del json.
     * @return La puntuación sumaria de una serie en formato String.
     */
    public String obtenerPuntuacionSumaria(int voteCount, double voteAverage) {
        if (voteCount < 0 || voteAverage < 0) {
            return "-";
        }

        double puntuacionNormalizada = 2*Math.log10(1 + voteCount);
        double puntuacionSumaria = (voteAverage + puntuacionNormalizada) / 2;
        puntuacionSumaria = Math.round(puntuacionSumaria * 100.0) / 100.0; // Redondear a dos decimales

        return String.valueOf(puntuacionSumaria);
    }
}
