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
import es.unican.movies.model.Series;
import es.unican.movies.service.EImageSize;
import es.unican.movies.service.ITmdbApi;

/**
 * Adapter for the list of series.
 */
public class SeriesAdapter extends ArrayAdapter<Series> {

    /**
     * List of series to display.
     */
    private final List<Series> seriesList;

    private final Context context;
    
    // Constructor without OnItemClickListener, which is now handled by the ListView itself
    protected SeriesAdapter(@NonNull Context context, @NonNull List<Series> seriesList) {
        super(context, R.layout.activity_main_movie_item, seriesList);
        this.context = context;
        this.seriesList = seriesList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Series series = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_main_movie_item, parent, false);
        }

        // poster
        ImageView ivPoster = convertView.findViewById(R.id.ivPoster);
        String imageUrl = ITmdbApi.getFullImagePath(series.getPosterPath(), EImageSize.W92);

        Picasso.get().load(imageUrl).fit().centerInside().into(ivPoster);

        // titulo
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(series.getName());




        return convertView;
    }

    @Override
    public int getCount() {
        return seriesList.size();
    }

    @Nullable
    @Override
    public Series getItem(int position) {
        return seriesList.get(position);
    }

}
