package com.mladenbabic.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.mladenbabic.popularmovies.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieData implements Parcelable {

    public long id;
    @SerializedName("original_title")
    public String originalTitle;
    public String overview;
    @SerializedName("release_date")
    public String releaseDate;
    @SerializedName("poster_path")
    public String posterPath;
    @SerializedName("backdrop_path")
    public String backdropPath;
    @SerializedName("vote_average")
    public double voteAverage;
    public double popularity;


    public Date getFormattedDate() {
        if (!TextUtils.isEmpty(releaseDate)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.MOVIE_DATE_FORMAT);
            try {
                return simpleDateFormat.parse(releaseDate);
            } catch (ParseException e) {
                Log.e("MovieData", "getFormattedDate() returned error: " + e);
            }
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.originalTitle);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
        dest.writeDouble(this.voteAverage);
        dest.writeDouble(this.popularity);
    }

    public MovieData() {
    }

    protected MovieData(Parcel in) {
        this.id = in.readLong();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.voteAverage = in.readDouble();
        this.popularity = in.readDouble();
    }

    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        public MovieData createFromParcel(Parcel source) {
            return new MovieData(source);
        }

        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };
}
