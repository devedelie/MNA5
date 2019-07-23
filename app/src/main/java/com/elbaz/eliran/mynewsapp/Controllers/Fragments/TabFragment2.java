package com.elbaz.eliran.mynewsapp.Controllers.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.elbaz.eliran.mynewsapp.Models.MostPopularModels.NYTMostPopular;
import com.elbaz.eliran.mynewsapp.Models.MostPopularModels.ResultMostPopular;
import com.elbaz.eliran.mynewsapp.R;
import com.elbaz.eliran.mynewsapp.Utils.NYTStreams;
import com.elbaz.eliran.mynewsapp.Views.NYTAdapterMostPopular;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static android.content.ContentValues.TAG;

/**
 * Created by Eliran Elbaz on 06-Jul-19.
 */
public class TabFragment2 extends Fragment {

    private Disposable mDisposable;
    private List<ResultMostPopular> mResultMostPopulars;
    private NYTAdapterMostPopular mNYTAdapterMostPopular;

    // try
    @BindView(R.id.fragment_2_recyclerView) RecyclerView mRecyclerView;


    public static TabFragment2 newInstance() {
        return (new TabFragment2());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_2, container, false);
        // Call during UI creation
        ButterKnife.bind(this, view);

        // Set the recyclerView to fix size in order to increase performances
        mRecyclerView.setHasFixedSize(true);

        this.configureRecyclerView();
        this.executeHttpRequestWithRetrofit();

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    //-----------------
    // RecyclerView Config
    //-----------------
    private void configureRecyclerView(){
        mResultMostPopulars = new ArrayList<>();
        mNYTAdapterMostPopular = new NYTAdapterMostPopular(this.mResultMostPopulars, getContext(), Glide.with(this));
        mRecyclerView.setAdapter(this.mNYTAdapterMostPopular);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //-----------------
    // HTTP (RxJAVA)
    //-----------------

    // 1 - Execute the stream
    private void executeHttpRequestWithRetrofit(){
        // 1.2 - Execute the stream subscribing to Observable defined inside NYTStream
        this.mDisposable = NYTStreams.streamFetchMostPopular("facebook")
                .subscribeWith(new DisposableObserver<NYTMostPopular>(){

                    @Override
                    public void onNext(NYTMostPopular nytMostPopular) {
                        // 1.3 - Update UI with list of titles
                        Log.d(TAG, "onNext fragment 2" );
                        updateUI(nytMostPopular.getResults());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError fragment 2: "+ e );
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete fragment 2");
                    }
                });
    }

    // This method will be called onDestroy to avoid any risk of memory leaks.
    private void disposeWhenDestroy(){
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }

    //-----------------
    // Update UI
    //-----------------

    // 3 - Update UI showing only titles
    private void updateUI(List<ResultMostPopular> titles){
        Log.d(TAG, "updateUI: ");
        mResultMostPopulars.clear();
        mResultMostPopulars.addAll(titles);
        mNYTAdapterMostPopular.notifyDataSetChanged();
    }
}
