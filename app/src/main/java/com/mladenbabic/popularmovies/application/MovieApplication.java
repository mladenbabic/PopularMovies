package com.mladenbabic.popularmovies.application;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Main application class
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 11/14/2015.
 */
public class MovieApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
