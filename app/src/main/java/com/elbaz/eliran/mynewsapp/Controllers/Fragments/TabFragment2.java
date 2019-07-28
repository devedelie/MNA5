package com.elbaz.eliran.mynewsapp.Controllers.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.elbaz.eliran.mynewsapp.Controllers.Activities.WebPageActivity;
import com.elbaz.eliran.mynewsapp.Models.MostPopularModels.NYTMostPopular;
import com.elbaz.eliran.mynewsapp.Models.MostPopularModels.ResultMostPopular;
import com.elbaz.eliran.mynewsapp.R;
import com.elbaz.eliran.mynewsapp.Utils.ItemClickSupport;
import com.elbaz.eliran.mynewsapp.Utils.NYTStreams;
import com.elbaz.eliran.mynewsapp.Views.NYTAdapterMostPopular;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static android.content.ContentValues.TAG;
import static com.elbaz.eliran.mynewsapp.Controllers.Fragments.TabFragment1.BUNDLE_URL;

/**
 * Created by Eliran Elbaz on 06-Jul-19.
 */
public class TabFragment2 extends Fragment {

    private Disposable mDisposable;
    private List<ResultMostPopular> mResultMostPopulars;
    private NYTAdapterMostPopular mNYTAdapterMostPopular;

    // try
    @BindView(R.id.fragment_2_recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.teb_fragment2_swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;


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
        this.configureSwipeRefreshLayout();
        this.configureOnClickRecyclerView();

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

    // -----------------
    // ACTION RecyclerView onClick
    // -----------------

    // 1 - Configure item click on RecyclerView
    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.recyclerview_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Log.e("TAG", "Position : "+position);
//                        // 1 - Get user from adapter
//                        Result mResults = mNYTAdapter.getURLInPosition(position);
//                        // 2 - Show result in a Toast
//                        Toast.makeText(getContext(), "the address is : "+mResults.getUrl(), Toast.LENGTH_LONG).show();

                        // Get title URL from adapter into variable
                        String url = mNYTAdapterMostPopular.getUrl(position);
                        // Instantiate the WebView Activity
                        Intent intent = new Intent(getActivity(), WebPageActivity.class);
                        // Send variable data to the activity
                        intent.putExtra(BUNDLE_URL,url);
                        startActivity(intent);
                    }
                });
    }

    //-----------------
    // Swipe configuration (reload news)
    //-----------------
    private void configureSwipeRefreshLayout(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeHttpRequestWithRetrofit();
            }
        });
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
        // Stops the SwipeRefreshLayout animation once our network query has finished correctly
        mSwipeRefreshLayout.setRefreshing(false);
        // completely erase the previous list of results each time
        // in order to avoid duplicating it due to  .addAll()
        mResultMostPopulars.clear();
        mResultMostPopulars.addAll(titles);
        mNYTAdapterMostPopular.notifyDataSetChanged();
    }
}
