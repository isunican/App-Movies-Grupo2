package es.unican.movies.activities.info;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import es.unican.movies.R;

/**
 * Activity to show the info of the app.
 */
public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }
}