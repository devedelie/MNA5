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


    // API search examples
    // https://api.nytimes.com/svc/search/v2/articlesearch.json?q=los+angeles&fq=sports&api-key=eUdpsuImhyQRapDx4vkN0NMOJZEYSqYA
    // OR   http://api.nytimes.com/svc/search/v2/articlesearch.json?q={query}&fq={filter1}&fq={filter2}&begin_date={begin_date}&end_date={end_date}&facet_filter=true&api-key={API-KEY}
    // http://api.nytimes.com/svc/search/v2/articlesearch.json?q=peres&fq=arts&fq=politics&begin_date=20120505&end_date=20140505&facet_filter=true&api-key=eUdpsuImhyQRapDx4vkN0NMOJZEYSqYA
    @GET ("svc/search/v2/articlesearch.json")
    Observable<NYTSearch> GetResultsForSearch(@Query("q") String searchQueryText,
                                              @Query("api-key") String apiKey);



        public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
}