package com.mladenbabic.popularmovies.http;

import com.mladenbabic.popularmovies.util.Constants;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 11/16/2015.
 */
public class HttpUtil {

    private static MovieDBService service;
    static {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.MOVIE_URL).addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(MovieDBService.class);
    }

    public static MovieDBService getService() {
        return service;
    }
}
