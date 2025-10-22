package es.unican.movies.DataBaseManagement;
import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import es.unican.movies.model.Genre;

public class GenreTypeConverter {

    private static Gson gson = new Gson();

    /**
     * Convierte una lista de Géneros (List<Genre>) a un String en formato JSON.
     * Room usará este método para guardar la lista en la base de datos.
     *
     * @param genres La lista de géneros a convertir.
     * @return Un String en formato JSON que representa la lista.
     */
    @TypeConverter
    public static String fromGenreList(List<Genre> genres) {
        if (genres == null) {
            return null;
        }
        return gson.toJson(genres);
    }

    /**
     * Convierte un String en formato JSON a una lista de Géneros (List<Genre>).
     * Room usará este método para reconstruir la lista al leerla de la base de datos.
     *
     * @param genresString El String en formato JSON.
     * @return La lista de géneros reconstruida.
     */
    @TypeConverter
    public static List<Genre> toGenreList(String genresString) {
        if (genresString == null || genresString.isEmpty()) {
            return Collections.emptyList(); // Devuelve una lista vacía si no hay nada
        }
        // Usamos TypeToken para decirle a Gson que el destino es una List<Genre>
        Type listType = new TypeToken<List<Genre>>() {}.getType();
        return gson.fromJson(genresString, listType);
    }
}

