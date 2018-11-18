package com.sanitcode.cataloguemoviedatabase.data.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.sanitcode.cataloguemoviedatabase.data.local.FavoriteColumn;

import static com.sanitcode.cataloguemoviedatabase.data.local.FavoriteColumn.FAVORITE_ID;
import static com.sanitcode.cataloguemoviedatabase.data.local.FavoriteColumn.FAVORITE_IMAGE;
import static com.sanitcode.cataloguemoviedatabase.data.local.FavoriteColumn.FAVORITE_TITLE;

public class ResultItems implements Parcelable {
    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("id")
    private String id;

    @SerializedName("overview")
    private String overview;

    @SerializedName("popularity")
    private Double popularity;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("title")
    private String title;

    @SerializedName("vote_average")
    private Double rating;

    @SerializedName("vote_count")
    private String voteCount;

    public ResultItems() {
    }

    public ResultItems(Cursor cursor) {
        this.id = FavoriteColumn.getColumnString(cursor,FAVORITE_ID);
        this.title = FavoriteColumn.getColumnString(cursor, FAVORITE_TITLE);
        this.posterPath = FavoriteColumn.getColumnString(cursor, FAVORITE_IMAGE);
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.backdropPath);
        dest.writeValue(this.id);
        dest.writeString(this.overview);
        dest.writeValue(this.popularity);
        dest.writeString(this.posterPath);
        dest.writeString(this.releaseDate);
        dest.writeString(this.title);
        dest.writeValue(this.rating);
        dest.writeString(this.voteCount);
    }

    protected ResultItems(Parcel in) {
        this.backdropPath = in.readString();
        this.id = (String) in.readValue(Long.class.getClassLoader());
        this.overview = in.readString();
        this.popularity = (Double) in.readValue(Double.class.getClassLoader());
        this.posterPath = in.readString();
        this.releaseDate = in.readString();
        this.title = in.readString();
        this.rating = (Double) in.readValue(Double.class.getClassLoader());
        this.voteCount = in.readString();
    }

    public static final Creator<ResultItems> CREATOR = new Creator<ResultItems>() {
        @Override
        public ResultItems createFromParcel(Parcel source) {
            return new ResultItems(source);
        }

        @Override
        public ResultItems[] newArray(int size) {
            return new ResultItems[size];
        }
    };

    @Override
    public String toString() {
        return getPosterPath() + getId();
    }
}
