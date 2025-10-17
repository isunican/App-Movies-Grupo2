package es.unican.movies.activities.wishlist;

import android.content.Context;
import android.util.Log;
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

import es.unican.movies.DataBaseManagement.SeriesDB;
import es.unican.movies.R;
import es.unican.movies.model.Series;
import es.unican.movies.service.EImageSize;
import es.unican.movies.service.ITmdbApi;

public class WishlistAdapter extends ArrayAdapter<SeriesDB> {

    /**
     * List of series to display.
     */
    private final List<SeriesDB> wishlistList;

    private final Context context;

    // Constructor without OnItemClickListener, which is now handled by the ListView itself
    protected WishlistAdapter(@NonNull Context context, @NonNull List<SeriesDB> wishlistList) {
        super(context, R.layout.activity_main_movie_item, wishlistList);
        this.context = context;
        this.wishlistList = wishlistList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SeriesDB series = getItem(position);

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
        return wishlistList.size();
    }

    @Nullable
    @Override
    public SeriesDB getItem(int position) {
        return wishlistList.get(position);
    }

    public static SeriesDB convertToSeriesDB(Series series) {
        SeriesDB seriesDB = new SeriesDB();
        seriesDB.setId(series.getId());
        seriesDB.setName(series.getName());
        seriesDB.setPosterPath(series.getPosterPath());
        return seriesDB;
    }
}
