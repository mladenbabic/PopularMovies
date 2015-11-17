package com.mladenbabic.popularmovies.provider;

import android.content.Context;
import android.net.Uri;

import com.mladenbabic.popularmovies.BuildConfig;
import com.mladenbabic.popularmovies.model.MovieData;

import nl.littlerobots.cupboard.tools.provider.CupboardContentProvider;
import nl.littlerobots.cupboard.tools.provider.UriHelper;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 11/15/2015.
 */
public class FavoriteMovieContentProvider extends CupboardContentProvider {

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".provider";
    public static final String DB_NAME = "movies.db";

    static {
        cupboard().register(MovieData.class);
    }

    public FavoriteMovieContentProvider() {
        super(AUTHORITY, DB_NAME, 1);
    }

    public static MovieData getMovieData(Context context, long id) {
        UriHelper uriHelper = UriHelper.with(FavoriteMovieContentProvider.AUTHORITY);
        Uri moviesUri = uriHelper.getUri(MovieData.class);
        return cupboard().withContext(context).query(moviesUri, MovieData.class).withSelection("id = ?", "" + id).get();
    }

    public static void deleteMovieData(Context context, long id) {
        UriHelper uriHelper = UriHelper.with(FavoriteMovieContentProvider.AUTHORITY);
        Uri moviesUri = uriHelper.getUri(MovieData.class);
        cupboard().withContext(context).delete(moviesUri, "id = ?", id + "");
    }

    public static void putMovieData(Context context, MovieData mMovieData) {
        UriHelper uriHelper = UriHelper.with(FavoriteMovieContentProvider.AUTHORITY);
        Uri movieUri = uriHelper.getUri(MovieData.class);
        cupboard().withContext(context).put(movieUri, mMovieData);
    }
}
