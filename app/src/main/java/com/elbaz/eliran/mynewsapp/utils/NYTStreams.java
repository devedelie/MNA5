package com.elbaz.eliran.mynewsapp.utils;

import android.util.Log;

import com.elbaz.eliran.mynewsapp.models.MostPopularModels.NYTMostPopular;
import com.elbaz.eliran.mynewsapp.models.SearchModels.NYTSearch;
import com.elbaz.eliran.mynewsapp.models.TopStoriesModels.NYTNews;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;
import static com.elbaz.eliran.mynewsapp.models.Constants.API_KEY;

/**
 * Created by Eliran Elbaz on 17-Jul-19.
 */
public class NYTStreams {

    public static Observable<NYTNews> streamFetchTopStories(String category){
        NYTService nytService = NYTService.retrofit.create(NYTService.class);
        return nytService.getResultsTopStories(category, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<NYTMostPopular> streamFetchMostPopular(String daysPeriod){
        Log.d(TAG, "streamFetchMostPopular: "+ daysPeriod);
        NYTService nytService = NYTService.retrofit.create(NYTService.class);
        return nytService.getResultsMostPopular(daysPeriod, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<NYTSearch> streamFetchSearchResults (String beginDate, String endDate,String filterQuery, String searchQueryText, String sort){
        NYTService nytService = NYTService.retrofit.create(NYTService.class);
        Log.d(TAG, "Passed values to API string: "+ beginDate + " " + endDate + " " + filterQuery + " "+ searchQueryText + " "+ sort);
        return nytService.GetResultsForSearch(beginDate,endDate,filterQuery,searchQueryText,sort, API_KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);

    }

}
