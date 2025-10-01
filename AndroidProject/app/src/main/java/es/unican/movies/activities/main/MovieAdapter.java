package es.unican.movies.activities.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.unican.movies.R;
import es.unican.movies.model.Movie;
import es.unican.movies.service.EImageSize;
import es.unican.movies.service.ITmdbApi;

/**
 * Adapter for the list of movies.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    /**
     * List of movies to display.
     */
    private final List<Movie> movieList;

    private final Context context;
    
    // Constructor without OnItemClickListener, which is now handled by the ListView itself
    protected MovieAdapter(@NonNull Context context, @NonNull List<Movie> movieList) {
        super(context, R.layout.activity_main_movie_item, movieList);
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_main_movie_item, parent, false);
        }

        // poster
        ImageView ivPoster = convertView.findViewById(R.id.ivPoster);
        String imageUrl = ITmdbApi.getFullImagePath(movie.getPosterPath(), EImageSize.W92);
        Picasso.get().load(imageUrl).fit().centerInside().into(ivPoster);

        // titulo
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(movie.getTitle());

        return convertView;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Nullable
    @Override
    public Movie getItem(int position) {
        return movieList.get(position);
    }

}
