package com.mladenbabic.popularmovies.activity;

import android.app.ActivityOptions;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
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
                replaceMovieDetailFragment(MovieDetailFragment.newInstance(new Bundle()));
                initStetho();
            } else {
                selectedPosition = savedInstanceState.getInt(Constants.POSITION_KEY);
            }
        } else {
            mTwoPane = false;
            getSupportActionBar().setElevation(0f);
        }
    }

    private void replaceMovieDetailFragment(MovieDetailFragment fragment) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.movie_detail_container, fragment, MovieDetailFragment.TAG).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.POSITION_KEY, selectedPosition);
    }

    @Override
    public void onItemSelected(MovieData movieData, final Bitmap posterBitmap, View view, int position) {
        Log.d(TAG, "onItemSelected() returned: " + movieData);
        selectedPosition = position;
        if (mTwoPane) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle args = new Bundle();
            args.putParcelable(Constants.MOVIE_DETAIL_KEY, movieData);
            args.putParcelable(Constants.POSTER_IMAGE_KEY, posterBitmap);
            MovieDetailFragment fragment = MovieDetailFragment.newInstance(args);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fragment.setSharedElementReturnTransition(TransitionInflater.from(this).inflateTransition(R.transition.trans_move));
                fragment.setExitTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.no_transition));
                fragmentTransaction.addSharedElement(view, Constants.POSTER_IMAGE_VIEW_KEY);
            }
            fragmentTransaction.replace(R.id.movie_detail_container, fragment, MovieDetailFragment.TAG).commit();
        } else {
            ActivityOptions options = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
