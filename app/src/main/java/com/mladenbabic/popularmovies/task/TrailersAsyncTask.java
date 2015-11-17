package com.mladenbabic.popularmovies.task;

import android.util.Log;
import android.widget.ProgressBar;

import com.mladenbabic.popularmovies.BuildConfig;
import com.mladenbabic.popularmovies.http.HttpUtil;
import com.mladenbabic.popularmovies.model.Trailer;
import com.mladenbabic.popularmovies.model.TrailersResults;

import java.io.IOException;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 11/15/2015.
 */
public class TrailersAsyncTask extends CommonAsyncTask<Trailer> {

    private static final String TAG = "TrailersAsyncTask";

    private long mMovieId;

    public TrailersAsyncTask(long mMovieId, ProgressBar mProgressBar, FetchDataListener mListener) {
        super(mProgressBar, mListener);
        this.mMovieId = mMovieId;
    }


    @Override
    protected ArrayList<Trailer> doInBackground(Void... params) {

        Call<TrailersResults> createdCall = HttpUtil.getService().getTrailersResults(mMovieId, BuildConfig.THE_MOVIE_DB_API_KEY);
        try {
            Response<TrailersResults> result = createdCall.execute();
            return result.body().results;
        } catch (IOException e) {
            Log.e(TAG, "IOException occurred in doInBackground()");
        }
        return null;
    }

}
