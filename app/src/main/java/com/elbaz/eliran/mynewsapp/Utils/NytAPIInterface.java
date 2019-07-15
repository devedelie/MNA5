package com.elbaz.eliran.mynewsapp.Utils;

import com.elbaz.eliran.mynewsapp.Models.NYTNews;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Eliran Elbaz on 06-Jul-19.
 */
public interface NytAPIInterface {

    String BASE_URL = "https://api.nytimes.com/svc/topstories/v2/";
    // Example of full path: "https://api.nytimes.com/svc/topstories/v2/science.json?api-key=yourkey"
    // One Query makes a sequence as the following: "?{xxx}="
    // 1st and 2nd Queries makes the following sequence "?{xxx}&{yyy}="

    @GET("{category}.json")
    Call<List<NYTNews>> getStories2(
            @Path("category") String category ,
            @Query("api-Key") String apiKey

    );



}