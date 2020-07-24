package com.avinash.mymovies.services;

import com.avinash.mymovies.model.MovieDetail;
import com.avinash.mymovies.model.SearchResultContainer;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {

    @GET("/")
    Call<SearchResultContainer> searchIMDBMoviesByName(
            @Query("apiKey") String apiKey,
            @Query("s") String term,
            @Query("type") String type,
            @Query("page") String page
    );

    @GET("/")
    Call<MovieDetail> searchIMDBMoviesById(
            @Query("apiKey") String apiKey,
            @Query("i") String omdbId
    );

}
