package com.elbaz.eliran.mynewsapp.Controllers.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.elbaz.eliran.mynewsapp.Controllers.Activities.WebPageActivity;
import com.elbaz.eliran.mynewsapp.Models.Constants;
import com.elbaz.eliran.mynewsapp.Models.TopStoriesModels.NYTNews;
import com.elbaz.eliran.mynewsapp.Models.TopStoriesModels.Result;
import com.elbaz.eliran.mynewsapp.R;
import com.elbaz.eliran.mynewsapp.Utils.CheckInternetConnection;
import com.elbaz.eliran.mynewsapp.Utils.ItemClickSupport;
import com.elbaz.eliran.mynewsapp.Utils.NYTStreams;
import com.elbaz.eliran.mynewsapp.Views.NYTAdapter;
import com.google.android.material.snackbar.Snackbar;

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
public class TabFragment1 extends Fragment {

    public static final String BUNDLE_URL= "BUNDLE_URL";


    private Disposable mDisposable;
    private List<Result> mResults;
    private NYTAdapter mNYTAdapter;
    private Boolean networkState;

    // ButterKnife
    @BindView(R.id.fragment_1_recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.teb_fragment1_swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_1, container, false);


        // Check for Internet connection
        networkState = CheckInternetConnection.isNetworkAvailable(getActivity().getApplicationContext());
        if (!networkState){
            internetConnectivityMessage();
        }

        // Call during UI creation
        ButterKnife.bind(this, view);
        // Set the recyclerView to fixed size in order to increase performances
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

    // Connectivity failure message
    public void internetConnectivityMessage(){
        Snackbar.make(getActivity().getCurrentFocus(), R.string.internet_connectivity,
                Snackbar.LENGTH_LONG)
                .show();
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
                if(!networkState){
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
        this.mDisposable = NYTStreams.streamFetchTopStories(Constants.TAB1VALUE)
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

    // This method will be called onDestroy to avoid any risk of memory leaks.
    private void disposeWhenDestroy(){
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }

    // -----------------
    // ACTION RecyclerView onClick
    // -----------------
    //  Configure item click on RecyclerView
    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.recyclerview_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Log.e("TAG", "Position : "+position);
                        // Get title URL from adapter into variable
                        String url = mNYTAdapter.getUrl(position);
                        // Instantiate the WebView Activity
                        Intent intent = new Intent(getActivity(), WebPageActivity.class);
                        // Send variable data to the activity
                        intent.putExtra(BUNDLE_URL,url);
                        startActivity(intent);
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