package com.avinash.mymovies.controller;

import com.avinash.mymovies.model.MovieDetail;
import com.avinash.mymovies.model.SearchResultContainer;
import com.avinash.mymovies.services.ApiServices;
import com.avinash.mymovies.utils.AppUtils;

import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchController {

    // TODO : Move these to config files
    public static final String BASE_URL = "http://www.omdbapi.com";
    public static final String API_KEY = "edadca8c";

    public void searchMoviesByName(String searchTerm, String type, String page, Callback<SearchResultContainer> callback) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(AppUtils.getGson()))
                .build();

        ApiServices apiServices = retrofit.create(ApiServices.class);
        apiServices.searchIMDBMoviesByName(API_KEY, searchTerm, type, page).enqueue(callback);
    }

    public void fetchMovieDetailsById(String omdbId, Callback<MovieDetail> callback) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(AppUtils.getGson()))
                .build();
        ApiServices apiServices = retrofit.create(ApiServices.class);
        apiServices.searchIMDBMoviesById(API_KEY, omdbId).enqueue(callback);


    }
}
