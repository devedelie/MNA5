package com.elbaz.eliran.mynewsapp.Utils;

import com.elbaz.eliran.mynewsapp.Models.NYTTopStories;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Eliran Elbaz on 06-Jul-19.
 */
public interface NytAPIInterface {

    String BASE_URL = "https://api.nytimes.com/svc/topstories/v2/";

    @GET("home")
    Call<List<NYTTopStories>> getTopStories(
            @Query("country") String country ,
            @Query("apiKey") String apiKey

    );

}