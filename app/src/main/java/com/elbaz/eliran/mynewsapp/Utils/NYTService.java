package com.elbaz.eliran.mynewsapp.Utils;

import com.elbaz.eliran.mynewsapp.Models.MostPopularModels.NYTMostPopular;
import com.elbaz.eliran.mynewsapp.Models.SearchModels.NYTSearch;
import com.elbaz.eliran.mynewsapp.Models.TopStoriesModels.NYTNews;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.elbaz.eliran.mynewsapp.Models.Constants.BASE_URL;

/**
 * Created by Eliran Elbaz on 15-Jul-19.
 */

public interface NYTService {

    @GET("svc/topstories/v2/{category}.json")
    Observable<NYTNews> getResultsTopStories(@Path("category") String category,
                                             @Query("api-key") String apiKey);

    @GET("svc/mostpopular/v2/shared/1/{platform}.json")
    Observable<NYTMostPopular> getResultsMostPopular (@Path("platform") String platform,
                                                      @Query("api-key") String apiKey);


    // NYT search API from developers.nytimes.com simulator
    // https://api.nytimes.com/svc/search/v2/articlesearch.json?begin_date=20110505&end_date=20140505&fq=arts&q=disney&sort=oldest&api-key=[YOUR_API_KEY]' \
    @GET ("svc/search/v2/articlesearch.json")
    Observable<NYTSearch> GetResultsForSearch(@Query("begin_date") String beginDate,
                                              @Query("end_date") String endDate,
                                              @Query("fq") String filterQuery,
                                              @Query("q") String searchQueryText,
                                              @Query("sort") String sort,
                                              @Query("api-key") String apiKey);



        public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
}