package com.mladenbabic.popularmovies.loader;

import android.content.Context;
import android.util.Log;

import com.mladenbabic.popularmovies.BuildConfig;
import com.mladenbabic.popularmovies.http.HttpUtil;
import com.mladenbabic.popularmovies.model.MovieData;
import com.mladenbabic.popularmovies.model.MovieResults;
import com.mladenbabic.popularmovies.util.Constants;
import com.mladenbabic.popularmovies.util.PreferenceUtil;

import java.io.IOException;
import java.util.List;

import retrofit.Call;
import retrofit.Response;

/**
 * Task retrieves data from the web server
 * Created by Mladen Babic on 11/7/2015.
 */
public class ResultsLoader extends CommonTaskLoader {
    private static final String TAG = "ResultsLoader";

    public ResultsLoader(Context context) {
        super(context);
    }

    @Override
    public List<MovieData> loadInBackground() {
        Call<MovieResults> createdCall = HttpUtil.getService().getMovieResults(BuildConfig.THE_MOVIE_DB_API_KEY, PreferenceUtil.getPrefs(getContext(), Constants.MODE_VIEW, Constants.SORT_BY_POPULARITY_DESC));
        try {
            Response<MovieResults> result = createdCall.execute();
            return result.body().results;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "IOException occurred in loadInBackground()");
        }
        return null;
    }
}