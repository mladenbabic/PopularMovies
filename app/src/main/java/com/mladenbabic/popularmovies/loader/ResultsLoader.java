package com.mladenbabic.popularmovies.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.mladenbabic.popularmovies.BuildConfig;
import com.mladenbabic.popularmovies.http.MovieDBService;
import com.mladenbabic.popularmovies.model.MovieData;
import com.mladenbabic.popularmovies.model.MovieResults;
import com.mladenbabic.popularmovies.util.Constants;
import com.mladenbabic.popularmovies.util.PreferenceUtil;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.Response;

/**
 * Created by Mladen on 11/7/2015.
 */
public class ResultsLoader extends AsyncTaskLoader<List<MovieData>> {
    private static final String TAG = "ResultsLoader";
    private List<MovieData> mResults;
    private MovieDBService service;

    public ResultsLoader(Context context, MovieDBService service) {
        super(context);
        this.service = service;
    }

    @Override
    public List<MovieData> loadInBackground() {
        Call<MovieResults> createdCall = service.getMovieResults(BuildConfig.THE_MOVIE_DB_API_KEY, PreferenceUtil.getPrefs(getContext(), Constants.SORT_BY_KEY, Constants.SORT_BY_POPULARITY_DESC));
        try {
            Response<MovieResults> result = createdCall.execute();
            return result.body().results;
        } catch (IOException e) {
            Log.e(TAG, "IOException occurred in loadInBackground()");
        }
        return null;
    }

    @Override
    public void deliverResult(List<MovieData> apps) {
        mResults = apps;
        if (isStarted()) {
            super.deliverResult(apps);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mResults != null) {
            deliverResult(mResults);
        }

        if (takeContentChanged() || mResults == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
    }
}