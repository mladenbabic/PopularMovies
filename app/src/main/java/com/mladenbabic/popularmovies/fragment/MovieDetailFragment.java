package com.mladenbabic.popularmovies.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mladenbabic.popularmovies.R;
import com.mladenbabic.popularmovies.activity.DetailActivity;
import com.mladenbabic.popularmovies.model.MovieData;
import com.mladenbabic.popularmovies.model.Review;
import com.mladenbabic.popularmovies.model.Trailer;
import com.mladenbabic.popularmovies.provider.FavoriteMovieContentProvider;
import com.mladenbabic.popularmovies.task.CommonAsyncTask;
import com.mladenbabic.popularmovies.task.ReviewsAsyncTask;
import com.mladenbabic.popularmovies.task.TrailersAsyncTask;
import com.mladenbabic.popularmovies.util.CommonUtil;
import com.mladenbabic.popularmovies.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
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

    @Bind(R.id.movie_detail_rate_image_view)
    ImageView mDetailRateImageView;

    @Bind(R.id.movie_detail_play_trailer_image_view)
    ImageView mPlayTrailerImageView;

    @Bind(R.id.movie_detail_rate_value_text_view)
    TextView mDetailRateTextView;

    @Bind(R.id.movie_detail_title_text_view)
    TextView mDetailMovieTitle;

    @Bind(R.id.movie_detail_year_text_view)
    TextView mDetailMovieYear;

    @Bind(R.id.movie_detail_synopsys_data_text_view)
    TextView mDetailMovieSynopsis;

    @Bind(R.id.empty_trailer_list)
    TextView mDetailMovieEmptyTrailers;

    @Bind(R.id.empty_review_list)
    TextView mDetailMovieEmptyReviews;

    @Nullable
    @Bind(R.id.favorite_fab)
    FloatingActionButton mFavoriteFab;

    @Bind({R.id.appbar, R.id.inc_movie_detail})
    List<View> viewContainers;

    @Bind(R.id.inc_no_selected_movie)
    View noSelectedView;

    @Bind(R.id.movie_detail_trailer_progress_bar)
    ProgressBar mTrailersProgressBar;

    @Bind(R.id.movie_detail_review_progress_bar)
    ProgressBar mReviewsProgressBar;

    @Bind(R.id.movie_detail_trailer_container)
    LinearLayout mTrailerLinearLayout;

    @Bind(R.id.detail_movie_reviews_container)
    LinearLayout mReviewLinearLayout;

    private MovieData mMovieData;
    private Bitmap mPosterImage;
    private boolean mAddedInFavorite;
    private ArrayList<Trailer> mTrailers;
    private ArrayList<Review> mReviews;
    private Trailer mMainTrailer;

    public static MovieDetailFragment newInstance(Bundle bundle) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public MovieDetailFragment() {
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.FAVORITE, mAddedInFavorite);
        outState.putParcelableArrayList(Constants.TRAILERS, mTrailers);
        outState.putParcelableArrayList(Constants.REVIEWS, mReviews);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mPosterImage = getArguments().getParcelable(Constants.POSTER_IMAGE_KEY);
            mMovieData = getArguments().getParcelable(Constants.MOVIE_DETAIL_KEY);
            mAddedInFavorite = FavoriteMovieContentProvider.getMovieData(getActivity(), mMovieData.id) != null;
            Log.d(TAG, "onCreate() called with: " + "mMovieData = [" + mMovieData + "]");
            Log.d(TAG, "onCreate() called with: " + "mAddedInFavorite = [" + mAddedInFavorite + "]");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.movie_detail_menu, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.share_movie:
                openShareIntent(mMainTrailer);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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
        if (savedInstanceState != null) {
            mTrailers = savedInstanceState.getParcelableArrayList(Constants.TRAILERS);
            mReviews = savedInstanceState.getParcelableArrayList(Constants.REVIEWS);
            mAddedInFavorite = savedInstanceState.getBoolean(Constants.FAVORITE);
            mMainTrailer = savedInstanceState.getParcelable(Constants.MAIN_TRAILER);
            addTrailerViews(mTrailers);
            addReviewViews(mReviews);
        } else {
            executeTasks(mMovieData);
        }
        initView(mMovieData);
        return view;
    }

    private void executeTasks(MovieData mMovieData) {

        new TrailersAsyncTask(mMovieData.id, mTrailersProgressBar, new CommonAsyncTask.FetchDataListener<Trailer>() {
            @Override
            public void onFetchData(ArrayList<Trailer> resultList) {
                Log.d(TAG, "onFetchData() returned: " + resultList);
                mTrailers = resultList;
                addTrailerViews(mTrailers);
            }
        }).execute();

        new ReviewsAsyncTask(mMovieData.id, mReviewsProgressBar, new CommonAsyncTask.FetchDataListener<Review>() {
            @Override
            public void onFetchData(ArrayList<Review> resultList) {
                Log.d(TAG, "onFetchData() returned: " + resultList);
                mReviews = resultList;
                addReviewViews(mReviews);
            }
        }).execute();

    }


    private void addTrailerViews(List<Trailer> resultList) {
        final LayoutInflater inflater = LayoutInflater.from(getActivity());

       boolean emptyList = resultList == null || resultList.isEmpty();

        if (resultList != null && !resultList.isEmpty()) {
            mMainTrailer = resultList.get(0);
            mPlayTrailerImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openYouTubeIntent(mMainTrailer.key);
                }
            });
        }

        for (Trailer trailer : resultList) {
            final String key = trailer.key;
            final View trailerView = inflater.inflate(R.layout.list_item_trailer, mTrailerLinearLayout, false);
            ImageView trailerImage = ButterKnife.findById(trailerView, R.id.trailer_poster_image_view);
            ImageView playImage = ButterKnife.findById(trailerView, R.id.play_trailer_image_view);
            playImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openYouTubeIntent(key);
                }
            });

            Picasso.with(getActivity())
                    .load(String.format(Constants.YOU_TUBE_IMG_URL, trailer.key))
                    .placeholder(R.drawable.ic_movie_placeholder)
                    .error(R.drawable.ic_movie_placeholder)
                    .into(trailerImage);
            mTrailerLinearLayout.addView(trailerView);
        }

        mDetailMovieEmptyTrailers.setVisibility(emptyList ? View.VISIBLE : View.GONE);

    }

    private void addReviewViews(List<Review> resultList) {

        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        boolean emptyList = resultList == null || resultList.isEmpty();

        for (Review review : resultList) {
            final View reviewView = inflater.inflate(R.layout.list_item_review, mReviewLinearLayout, false);
            TextView reviewAuthor = ButterKnife.findById(reviewView, R.id.list_item_review_author_text_view);
            TextView reviewContent = ButterKnife.findById(reviewView, R.id.list_item_review_content_text_view);
            reviewAuthor.setText(review.author);
            reviewContent.setText(review.content);
            mReviewLinearLayout.addView(reviewView);
        }
        mDetailMovieEmptyReviews.setVisibility(emptyList ? View.VISIBLE : View.GONE);
    }



    private void initView(MovieData movieData) {
        if (movieData == null) {
            toggleNonSelectedView(true);
            return;
        }

        toggleNonSelectedView(false);
        switchFabIcon();

        String imageUrl = Constants.IMAGE_MOVIE_URL + Constants.IMAGE_SIZE_W500 + mMovieData.backdropPath;

        Picasso.with(getActivity())
                .load(imageUrl)
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.drawable.ic_movie_placeholder)
                .into(mBackdropMovie);

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

    private void switchFabIcon() {
        mFavoriteFab.setImageResource(mAddedInFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_outline);
    }

    private void toggleNonSelectedView(boolean noMovieData) {
        toggleVisibleFab(!noMovieData);
        noSelectedView.setVisibility(noMovieData ? View.VISIBLE : View.GONE);
        for (View view : viewContainers) {
            view.setVisibility(noMovieData ? View.GONE : View.VISIBLE);
        }
    }

    private void toggleVisibleFab(boolean showFab) {
        if (mFavoriteFab != null) {
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) mFavoriteFab.getLayoutParams();
            p.setAnchorId(showFab ? R.id.appbar : View.NO_ID);
            mFavoriteFab.setLayoutParams(p);
            mFavoriteFab.setVisibility(showFab ? View.VISIBLE : View.GONE);
        }
    }

    @OnClick(R.id.favorite_fab)
    public void toggleFavorite() {
        //TODO Mladen add fade out/fade in animation for FAB
        int resultMsg;
        if (!mAddedInFavorite) {
            FavoriteMovieContentProvider.putMovieData(getActivity(), mMovieData);
            resultMsg = R.string.added_to_favorite;
            Log.d(TAG, "toggleFavorite() called to add favorite movie");
        } else {
            FavoriteMovieContentProvider.deleteMovieData(getActivity(), mMovieData.id);
            resultMsg = R.string.removed_from_favorite;
            Log.d(TAG, "toggleFavorite() called to delete from favorite movie list");
        }
        mAddedInFavorite = !mAddedInFavorite;
        Snackbar.make(getView(), resultMsg, Snackbar.LENGTH_SHORT).show();
        switchFabIcon();
    }

    private void openYouTubeIntent(String key) {
        Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOU_TUBE_VIDEO_URL + key));
        youTubeIntent.putExtra("force_fullscreen", true);
        startActivity(youTubeIntent);
    }

    private void openShareIntent(Trailer trailer) {
        if(trailer != null){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, Constants.YOU_TUBE_VIDEO_URL + trailer.key);
            intent.putExtra(android.content.Intent.EXTRA_SUBJECT, mMovieData.originalTitle);
            startActivity(Intent.createChooser(intent, getActivity().getString(R.string.share)));
        }
    }
}
