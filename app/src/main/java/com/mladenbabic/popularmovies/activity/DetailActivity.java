package com.mladenbabic.popularmovies.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.MenuItem;

import com.mladenbabic.popularmovies.R;
import com.mladenbabic.popularmovies.fragment.MovieDetailFragment;

import butterknife.ButterKnife;

/**
 * The activity which is used for setting a detail fragment and showing movie detail data.
 * Created by Mladen Babic <email>info@mladenbabic.com</email>  on 11/4/2015.
 */
public class DetailActivity extends BaseDetailActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Bundle bundle = new Bundle(getIntent().getExtras());
            fragmentTransaction.add(R.id.movie_detail_container, MovieDetailFragment.newInstance(bundle)).commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
