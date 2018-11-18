package com.sanitcode.cataloguemoviedatabase.api;


import com.sanitcode.cataloguemoviedatabase.data.model.Movie;
import com.sanitcode.cataloguemoviedatabase.data.model.ResultItems;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiCall {

    @GET("movie/popular")
    Call<Movie> getPopularMovie(@Query("api_key") String apiKey);

    @GET("movie/now_playing")
    Call<Movie> getNowPlayingMovie(@Query("api_key") String apiKey);

    @GET("movie/upcoming")
    Call<Movie> getUpcomingMovie(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<ResultItems> getMovieById(@Path("id") String id, @Query("api_key") String apiKey);

    @GET("search/movie/")
    Call<Movie> getMovieBySearch(@Query("query") String q, @Query("api_key") String apiKey);
}
