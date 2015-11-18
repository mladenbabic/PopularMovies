package com.mladenbabic.popularmovies.fragment;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mladenbabic.popularmovies.R;
import com.mladenbabic.popularmovies.activity.MainActivity;
import com.mladenbabic.popularmovies.adapter.MovieGridAdapter;
import com.mladenbabic.popularmovies.loader.FavoriteResultsLoader;
import com.mladenbabic.popularmovies.loader.ResultsLoader;
import com.mladenbabic.popularmovies.model.MovieData;
import com.mladenbabic.popularmovies.ui.SpacesItemDecoration;
import com.mladenbabic.popularmovies.util.Constants;
import com.mladenbabic.popularmovies.util.DeviceUtil;
import com.mladenbabic.popularmovies.util.PreferenceUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * The class is responsible for loading movie and showing data at the grid component.
 *
 * @author Mladen Babic <email>info@mladenbabic.com</email>
 */
public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, LoaderManager.LoaderCallbacks<List<MovieData>> {

    public static final String TAG = "MainFragment";

    public static Fragment newInstance() {
        return new MainFragment();
    }

    public interface Callback {
        void onItemSelected(MovieData movieData, Bitmap posterBitmap, View view, int position);
    }

    @Bind(R.id.main_movie_grid_recycle_view)
    RecyclerView mPopularGridView;

    @Bind(R.id.main_movie_sw_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Bind(R.id.main_grid_empty_container)
    LinearLayout mNoInternetContainer;

    @Bind(R.id.inc_no_movies)
    View noMoviesView;

    private ArrayList<MovieData> mMovieLists;
    private MovieGridAdapter mMovieAdapter;
    private MainActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMovieLists = new ArrayList<>();
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String sortType;
        boolean result;

        switch (item.getItemId()) {
            case R.id.show_favorites:
                sortType = Constants.SHOW_FAVORITES;
                result = true;
                break;
            case R.id.sort_by_popularity_desc:
                sortType = Constants.SORT_BY_POPULARITY_DESC;
                result = true;
                break;
            case R.id.sort_by_rates_desc:
                sortType = Constants.SORT_BY_RATING_DESC;
                result = true;
                break;
            default:
                sortType = Constants.SORT_BY_POPULARITY_DESC;
                result = super.onOptionsItemSelected(item);
                break;
        }

        item.setChecked(true);
        PreferenceUtil.savePrefs(getActivity(), Constants.MODE_VIEW, sortType);
        restartLoader();
        mSwipeRefreshLayout.setRefreshing(true);
        return result;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        int gridColumns = getResources().getInteger(R.integer.grid_columns);
        int progressViewOffsetStart = getResources().getInteger(R.integer.progress_view_offset_start);
        int progressViewOffsetEnd = getResources().getInteger(R.integer.progress_view_offset_end);

        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setProgressViewOffset(true, progressViewOffsetStart, progressViewOffsetEnd);

        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), gridColumns);

        mPopularGridView.setLayoutManager(mLayoutManager);
        mPopularGridView.setHasFixedSize(true);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        int colorPrimaryLight = ContextCompat.getColor(getActivity(), (R.color.colorPrimaryTransparent));
        mPopularGridView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        mMovieAdapter = new MovieGridAdapter(mMovieLists, colorPrimaryLight, false, (Callback) getActivity());

        mPopularGridView.setAdapter(mMovieAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<List<MovieData>> onCreateLoader(int id, Bundle args) {
        return PreferenceUtil.getPrefs(getActivity(), Constants.MODE_VIEW, Constants.SORT_BY_POPULARITY_DESC).equals(Constants.SHOW_FAVORITES) ? new FavoriteResultsLoader(getActivity()) :
        new ResultsLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<MovieData>> loader, List<MovieData> data) {
        mSwipeRefreshLayout.setRefreshing(false);
        mMovieLists = (ArrayList<MovieData>) data;
        mMovieAdapter.addMovies(data);
        if (data == null || data.isEmpty()) {
            if (!DeviceUtil.isOnline(getActivity())) {
                mNoInternetContainer.setVisibility(View.VISIBLE);
            } else {
                toggleShowEmptyMovie(false);
            }
        } else {
            toggleShowEmptyMovie(true);
        }

        if (mActivity != null && mActivity.getSelectedPosition() != -1) {
            mPopularGridView.scrollToPosition(mActivity.getSelectedPosition());
        }

        Snackbar.make(getView(), data == null ? R.string.movies_not_found : R.string.movies_data_loaded, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onLoaderReset(Loader<List<MovieData>> loader) {
        mSwipeRefreshLayout.setRefreshing(false);
        mMovieAdapter.addMovies(null);
    }

    @Override
    public void onRefresh() {
        restartLoader();
    }

    private void toggleShowEmptyMovie(boolean showMovieGrid) {
        noMoviesView.setVisibility(showMovieGrid ? View.GONE : View.VISIBLE);
        mNoInternetContainer.setVisibility(View.GONE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
