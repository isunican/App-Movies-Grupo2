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

/**
 * Utility class with helper methods.
 */
public class Utils {

    /**
     * Parse a JSON resource file into a list of movies.
     * @param context the context
     * @param jsonId the id of the JSON resource file
     * @return a list of movies
     */
    public static List<Movie> parseMovies(Context context, int jsonId) {
        InputStream is = context.getResources().openRawResource(jsonId);
        Type typeToken = new TypeToken<List<Movie>>() { }.getType();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return new GsonBuilder()
                .create()
                .fromJson(reader, typeToken);
    }

}
