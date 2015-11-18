package com.mladenbabic.popularmovies.loader;

import android.content.Context;

import com.mladenbabic.popularmovies.model.MovieData;
import com.mladenbabic.popularmovies.provider.FavoriteMovieContentProvider;

import java.util.List;

/**
 * Created by Mladen Babic on 11/7/2015.
 */
public class FavoriteResultsLoader extends CommonTaskLoader {
    private static final String TAG = "FavoriteResultsLoader";

    public FavoriteResultsLoader(Context context) {
        super(context);
    }

    @Override
    public List<MovieData> loadInBackground() {
        return FavoriteMovieContentProvider.getFavorites(getContext());
    }
}