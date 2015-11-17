package com.mladenbabic.popularmovies.adapter;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mladenbabic.popularmovies.R;
import com.mladenbabic.popularmovies.fragment.MainFragment;
import com.mladenbabic.popularmovies.model.MovieData;
import com.mladenbabic.popularmovies.provider.FavoriteMovieContentProvider;
import com.mladenbabic.popularmovies.util.CommonUtil;
import com.mladenbabic.popularmovies.util.Constants;
import com.mladenbabic.popularmovies.util.PreferenceUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * This adapter is used for a grid component which controls data and reusing view holders.
 * Created by Mladen Babic <email>info@mladenbabic.com</email> on 11/4/2015.
 */
public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.ViewHolder> {

    private List<MovieData> movieList = new ArrayList<>();
    private Calendar mCalendar;
    private int mDefaultColor;
    private MainFragment.Callback mCallback;

    public MovieGridAdapter(ArrayList<MovieData> mMovieLists, int mDefaultColor, MainFragment.Callback mCallback) {
        this.movieList = mMovieLists;
        this.mCalendar = Calendar.getInstance();
        this.mDefaultColor = mDefaultColor;
        this.mCallback = mCallback;
    }

    @Override
    public MovieGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_column, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MovieGridAdapter.ViewHolder holder, final int position) {
        final MovieData movieData = movieList.get(position);
        String sortType = PreferenceUtil.getPrefs(holder.mSortTypeValueTextView.getContext(), Constants.SORT_BY_KEY, Constants.SORT_BY_POPULARITY_DESC);
        holder.mGridItemContainer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bitmap posterBitmap = ((BitmapDrawable) holder.mMovieImageView.getDrawable()).getBitmap();
                mCallback.onItemSelected(movieData, posterBitmap, holder.mMovieImageView, position);
            }
        });

        holder.mGridItemContainer.setContentDescription(holder.mGridItemContainer.getContext().getString(R.string.a11y_movie_title, movieData.originalTitle));

        if (movieData.getFormattedDate() != null) {
            mCalendar.setTime(movieData.getFormattedDate());
            holder.mReleaseDateTextView.setText(String.valueOf(mCalendar.get(Calendar.YEAR)));
            holder.mReleaseDateTextView.setContentDescription(holder.mReleaseDateTextView.getContext().getString(R.string.a11y_movie_year, String.valueOf(mCalendar.get(Calendar.YEAR))));
        }

        if (Constants.SORT_BY_POPULARITY_DESC.equals(sortType)) {
            setIconForType(holder, sortType, movieData);
            holder.mSortTypeValueTextView.setText(String.valueOf(Math.round(movieData.popularity)));
        } else {
            setIconForType(holder, sortType, movieData);
            holder.mSortTypeValueTextView.setText(String.valueOf(Math.round(movieData.voteAverage)));
        }

        String imageUrl = Constants.IMAGE_MOVIE_URL + Constants.IMAGE_SIZE_W185 + movieData.posterPath;
        final RelativeLayout container = holder.mMovieTitleContainer;

        Picasso.with(holder.mMovieImageView.getContext()).load(imageUrl).placeholder(R.drawable.ic_movie_placeholder).
                into(holder.mMovieImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap posterBitmap = ((BitmapDrawable) holder.mMovieImageView.getDrawable()).getBitmap();
                        Palette.from(posterBitmap).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                container.setBackgroundColor(ColorUtils.setAlphaComponent(palette.getMutedColor(mDefaultColor), 190)); //Opacity
                            }
                        });
                    }

                    @Override
                    public void onError() {
                    }
                });
    }

    private void setIconForType(ViewHolder holder, String sortType, MovieData movieData) {
        if (Constants.SORT_BY_POPULARITY_DESC.equals(sortType)) {
            boolean addedInFavorite = FavoriteMovieContentProvider.getMovieData(holder.mSortTypeIconImageView.getContext(), movieData.id) != null;
            holder.mSortTypeIconImageView.setImageResource(addedInFavorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_outline);
        } else {
            holder.mSortTypeIconImageView.setImageResource(CommonUtil.getRateIcon(movieData.voteAverage, false));
        }
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    public void addMovies(List<MovieData> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        movieList = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.grid_item_sort_type_text_view)
        TextView mSortTypeValueTextView;

        @Bind(R.id.grid_item_release_date_text_view)
        TextView mReleaseDateTextView;

        @Bind(R.id.grid_item_poster_image_view)
        ImageView mMovieImageView;

        @Bind(R.id.grid_item_sort_type_image_view)
        ImageView mSortTypeIconImageView;

        @Bind(R.id.grid_item_title_container)
        RelativeLayout mMovieTitleContainer;

        @Bind(R.id.grid_item_container)
        FrameLayout mGridItemContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
