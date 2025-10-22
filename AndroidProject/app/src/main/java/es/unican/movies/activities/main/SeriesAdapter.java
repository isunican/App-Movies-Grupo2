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

import java.util.Collections;
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

    // Constructor without OnItemClickListener, which is now handled by the ListView itself
    protected SeriesAdapter(@NonNull Context context, @Nullable List<Series> seriesList) {
        super(context, R.layout.activity_main_movie_item, seriesList == null ? Collections.emptyList() : seriesList);
        this.seriesList = seriesList == null ? Collections.emptyList() : seriesList;
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
        if (series != null && series.getPosterPath() != null) {
            String imageUrl = ITmdbApi.getFullImagePath(series.getPosterPath(), EImageSize.W92);
            Picasso.get().load(imageUrl).fit().centerInside().into(ivPoster);
        } else {
            ivPoster.setImageDrawable(null);
        }

        // titulo
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(series != null ? series.getName() : "");

        return convertView;
    }

    @Override
    public int getCount() {
        return seriesList == null ? 0 : seriesList.size();
    }

    @Nullable
    @Override
    public Series getItem(int position) {
        if (seriesList == null || position < 0 || position >= seriesList.size()) return null;
        return seriesList.get(position);
    }

}
