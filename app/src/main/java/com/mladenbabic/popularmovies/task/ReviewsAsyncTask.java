package com.mladenbabic.popularmovies.task;

import android.util.Log;
import android.widget.ProgressBar;

import com.mladenbabic.popularmovies.BuildConfig;
import com.mladenbabic.popularmovies.http.HttpUtil;
import com.mladenbabic.popularmovies.model.Review;
import com.mladenbabic.popularmovies.model.ReviewResults;

import java.io.IOException;
import java.util.ArrayList;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 11/15/2015.
 */
public class ReviewsAsyncTask extends CommonAsyncTask<Review> {

    private static final String TAG = "ReviewsAsyncTask";
    private final long mMovieId;

    public ReviewsAsyncTask(long movieId, ProgressBar mProgressBar, FetchDataListener mListener) {
        super(mProgressBar, mListener);
        this.mMovieId = movieId;
    }

    @Override
    protected ArrayList<Review> doInBackground(Void... params) {
        Call<ReviewResults> createdCall = HttpUtil.getService().getReviewsResults(mMovieId, BuildConfig.THE_MOVIE_DB_API_KEY);
        try {
            Response<ReviewResults> result = createdCall.execute();
            return result.body().results;
        } catch (IOException e) {
            Log.e(TAG, "IOException occurred in doInBackground()");
        }
        return null;
    }
}
