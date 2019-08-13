package com.elbaz.eliran.mynewsapp.Controllers.Fragments;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.elbaz.eliran.mynewsapp.Models.TopStoriesModels.NYTNews;
import com.elbaz.eliran.mynewsapp.Models.TopStoriesModels.Result;
import com.elbaz.eliran.mynewsapp.R;
import com.elbaz.eliran.mynewsapp.Utils.NYTStreams;
import com.elbaz.eliran.mynewsapp.Views.NYTAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static android.content.ContentValues.TAG;

/**
 * Created by Eliran Elbaz on 06-Jul-19.
 */
public class TabFragment1 extends BaseFragment {

    public static final String BUNDLE_URL= "BUNDLE_URL";


    private Disposable mDisposable;
    private List<Result> mResults;
    private NYTAdapter mNYTAdapter;
    private Boolean networkState;

    // ButterKnife
    @BindView(R.id.fragment_1_recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.teb_fragment1_swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected BaseFragment newInstance() {
        return new TabFragment1();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_tab_1;
    }

    @Override
    protected void configureDesign() {
        this.configureRecyclerView();
        this.executeHttpRequestWithRetrofit();
        this.configureSwipeRefreshLayout();
    }

    @Override
    protected void onDestroyCall() {

    }

    @Override
    protected void disposeWhenDestroyCall() {
    }

    @Override
    protected void internetConnectivityMessageCall() {
    }

    @Override
    protected void internetConnectivityVerifierCall() {

    }

    //-----------------
    // RecyclerView Config
    //-----------------
    protected void configureRecyclerView(){
        mResults = new ArrayList<>();
        mNYTAdapter = new NYTAdapter(this.mResults, getContext(), Glide.with(this));
        mRecyclerView.setAdapter(this.mNYTAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    //-----------------
    // Swipe configuration (reload news)
    //-----------------
    private void configureSwipeRefreshLayout(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(!internetConnectivityVerifier()){
                    internetConnectivityMessage();
                    // Stops the SwipeRefreshLayout animation
                    mSwipeRefreshLayout.setRefreshing(false);
                }else{
                    executeHttpRequestWithRetrofit();
                }
            }
        });
    }

    //-----------------
    // HTTP (RxJAVA)
    //-----------------

    // 1 - Execute the stream
    private void executeHttpRequestWithRetrofit(){
        // 1.2 - Execute the stream subscribing to Observable defined inside NYTStream
        this.mDisposable = NYTStreams.streamFetchTopStories(getString(R.string.home))
                .subscribeWith(new DisposableObserver<NYTNews>(){

                    @Override
                    public void onNext(NYTNews nytNews) {
                        // 1.3 - Update UI with list of titles
                        Log.e(TAG, "onNext" );
                        updateUI(nytNews.getResults());
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: "+ e );
                    }
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }



    //-----------------
    // Update UI
    //-----------------
    // Update UI showing news titles
    private void updateUI(List<Result> titles){
        // Stops the SwipeRefreshLayout animation once our network query has finished correctly
        mSwipeRefreshLayout.setRefreshing(false);
        // completely erase the previous list of results each time
        // in order to avoid duplicating it due to  .addAll()
        mResults.clear();
        mResults.addAll(titles);
        mNYTAdapter.notifyDataSetChanged();
    }
}