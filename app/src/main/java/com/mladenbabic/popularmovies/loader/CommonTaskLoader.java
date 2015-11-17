package com.mladenbabic.popularmovies.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 11/14/2015.
 */
public abstract class CommonTaskLoader<T> extends AsyncTaskLoader<List<T>> {

    private List<T> mResults;

    public CommonTaskLoader(Context context) {
        super(context);
    }


    @Override
    public void deliverResult(List<T> apps) {
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
