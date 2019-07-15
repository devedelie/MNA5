package com.elbaz.eliran.mynewsapp.Utils;

import com.elbaz.eliran.mynewsapp.Models.NYTNews;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.elbaz.eliran.mynewsapp.Utils.NytAPIInterface.BASE_URL;

public class NytAPIClient implements Callback<List<NYTNews>> {

    /**
     * Create gson and get Retrofit Instance
//     */
//    public void start(){
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//    }


    private static Retrofit getRetrofitInstance(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * Get API Service
     */
    public static NytAPIInterface getApiService(){
        return getRetrofitInstance().create(NytAPIInterface.class);
    }




    @Override
    public void onResponse(Call<List<NYTNews>> call, Response<List<NYTNews>> response) {

    }

    @Override
    public void onFailure(Call<List<NYTNews>> call, Throwable t) {

    }
}



