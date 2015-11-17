package com.mladenbabic.popularmovies.activity;

import android.app.ActivityOptions;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.stetho.Stetho;
import com.mladenbabic.popularmovies.R;
import com.mladenbabic.popularmovies.fragment.MainFragment;
import com.mladenbabic.popularmovies.fragment.MovieDetailFragment;
import com.mladenbabic.popularmovies.model.MovieData;
import com.mladenbabic.popularmovies.util.Constants;

import butterknife.ButterKnife;


/**
 * The main activity is used for loading fragments depends on device's size.
 * It adds a detail fragment if detects a device as tablet.
 * Because fragments don't talk to each other, the activity works as handler for control and transfer data for selected movie from the movie fragment to the detail fragment.
 * <p>
 * Created by Mladen Babic <email>info@mladenbabic.com</email>  on 11/4/2015.
 */
public class MainActivity extends BaseDetailActivity implements MainFragment.Callback {

    private static final String TAG = "MainActivity";
    private boolean mTwoPane = true;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setToolbar(false, true);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                fragmentTransaction.add(R.id.movie_detail_container, MovieDetailFragment.newInstance(bundle), MovieDetailFragment.DETAIL_FRAGMENT_TAG).commit();
                initStetho();
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
    }


    @Override
    public void onItemSelected(MovieData movieData, Bitmap posterBitmap, View view, int position) {
        Log.d(TAG, "onItemSelected() returned: " + movieData);
        selectedPosition = position;
        if (mTwoPane) {
            Bundle args = new Bundle();
            args.putParcelable(Constants.MOVIE_DETAIL_KEY, movieData);
            args.putParcelable(Constants.POSTER_IMAGE_KEY, posterBitmap);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);
            getFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, MovieDetailFragment.DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            ActivityOptions options = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                options = ActivityOptions.
                        makeSceneTransitionAnimation(this, view, Constants.POSTER_IMAGE_VIEW_KEY);
            }
            Intent openDetailIntent = new Intent(this, DetailActivity.class);
            openDetailIntent.putExtra(Constants.MOVIE_DETAIL_KEY, movieData);
            openDetailIntent.putExtra(Constants.POSTER_IMAGE_KEY, posterBitmap);
            if (options != null) {
                startActivity(openDetailIntent, options.toBundle());
            } else {
                startActivity(openDetailIntent);
            }
        }
    }

    private void initStetho() {
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }

}
