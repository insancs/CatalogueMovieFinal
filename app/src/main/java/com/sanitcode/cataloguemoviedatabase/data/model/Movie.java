package com.sanitcode.cataloguemoviedatabase.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie implements Parcelable {

    @SerializedName("page")
    private Long mPage;

    @SerializedName("results")
    private List<ResultItems> mResultItems;

    @SerializedName("total_pages")
    private Long mTotalPages;

    @SerializedName("total_results")
    private Long mTotalResults;

    public List<ResultItems> getResults() {
        return mResultItems;
    }

    public void setResults(List<ResultItems> resultItems) {
        mResultItems = resultItems;
    }

    public Long getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(Long totalResults) {
        mTotalResults = totalResults;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mPage);
        dest.writeTypedList(this.mResultItems);
        dest.writeValue(this.mTotalPages);
        dest.writeValue(this.mTotalResults);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.mPage = (Long) in.readValue(Long.class.getClassLoader());
        this.mResultItems = in.createTypedArrayList(ResultItems.CREATOR);
        this.mTotalPages = (Long) in.readValue(Long.class.getClassLoader());
        this.mTotalResults = (Long) in.readValue(Long.class.getClassLoader());
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
