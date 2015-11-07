package com.mladenbabic.popularmovies.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mladenbabic.popularmovies.R;
import com.mladenbabic.popularmovies.activity.DetailActivity;
import com.mladenbabic.popularmovies.model.MovieData;
import com.mladenbabic.popularmovies.util.CommonUtil;
import com.mladenbabic.popularmovies.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 * Created by Mladen Babic <email>info@mladenbabic.com</email>  on 11/4/2015.
 */
public class MovieDetailFragment extends Fragment {

    public static final String DETAIL_FRAGMENT_TAG = "DFTAG";
    private static final String TAG = "MovieDetailFragment";

    @Nullable
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.movie_detail_poster_image_view)
    ImageView mPosterMovie;

    @Bind(R.id.movie_detail_backdrop_image_view)
    ImageView mBackdropMovie;

    @Bind(R.id.movie_detail_title_text_view)
    TextView mDetailMovieTitle;

    @Bind(R.id.movie_detail_year_text_view)
    TextView mDetailMovieYear;

    @Bind(R.id.movie_detail_rate_image_view)
    ImageView mDetailRateImageView;

//    @Bind(R.id.playTrailerImageView)
//    ImageView mPlayTrailerImageView;

    @Bind(R.id.movie_detail_rate_value_text_view)
    TextView mDetailRateTextView;

    @Bind(R.id.movie_detail_synopsys_data_text_view)
    TextView mDetailMovieSynopsis;

    private MovieData mMovieData;
    private Bitmap mPosterImage;
    private boolean mTwoPane;

    public static MovieDetailFragment newInstance(Bundle bundle) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        if (getArguments() != null) {
            mPosterImage = getArguments().getParcelable(Constants.POSTER_IMAGE_KEY);
            mMovieData = getArguments().getParcelable(Constants.MOVIE_DETAIL_KEY);
            mTwoPane = getArguments().getBoolean(Constants.TWO_PANE_KEY);
            Log.d(TAG, "onCreate() called with: " + "mMovieData = [" + mMovieData + "]");
            Log.d(TAG, "onCreate() called with: " + "mPosterImage = [" + mPosterImage + "]");
            Log.d(TAG, "onCreate() called with: " + "mTwoPane = [" + mTwoPane + "]");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        Log.d(TAG, "onCreateView() called");
        ButterKnife.bind(this, view);
        if (getActivity() instanceof DetailActivity) {
            DetailActivity detailActivity = (DetailActivity) getActivity();
            detailActivity.setToolbar(mToolbar, true, false);
        }
        initView(mMovieData);
        return view;
    }

    private void initView(MovieData movieData) {
        if (movieData == null) {
            return;
        }

        String imageUrl = Constants.IMAGE_MOVIE_URL + Constants.IMAGE_SIZE_W500 + mMovieData.backdropPath;
        Picasso.with(getActivity()).load(imageUrl).into(mBackdropMovie);

        mPosterMovie.setImageBitmap(mPosterImage);
        mDetailRateImageView.setImageResource(CommonUtil.getRateIcon(movieData.voteAverage, true));
        mDetailRateImageView.setContentDescription(getString(R.string.a11y_movie_title, movieData.originalTitle));

        mDetailMovieTitle.setText(movieData.originalTitle);
        mDetailMovieTitle.setContentDescription(getString(R.string.a11y_movie_title, movieData.originalTitle));

        mDetailRateTextView.setText(String.format("%d/10", Math.round(movieData.voteAverage)));
        mDetailRateTextView.setContentDescription(getString(R.string.a11y_movie_rate, String.format("%d/10", Math.round(movieData.voteAverage))));

        mDetailMovieSynopsis.setText(movieData.overview);
        mDetailMovieSynopsis.setContentDescription(getString(R.string.a11y_movie_overview, movieData.overview));

        if (movieData.getFormattedDate() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(movieData.getFormattedDate().getTime());
            mDetailMovieYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
            mDetailMovieYear.setContentDescription(getString(R.string.a11y_movie_year, String.valueOf(calendar.get(Calendar.YEAR))));
        }
    }
}
