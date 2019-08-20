package com.elbaz.eliran.mynewsapp.controllers.fragments;

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
import com.elbaz.eliran.mynewsapp.R;
import com.elbaz.eliran.mynewsapp.controllers.activities.WebPageActivity;
import com.elbaz.eliran.mynewsapp.models.TopStoriesModels.NYTNews;
import com.elbaz.eliran.mynewsapp.models.TopStoriesModels.Result;
import com.elbaz.eliran.mynewsapp.utils.CheckInternetConnection;
import com.elbaz.eliran.mynewsapp.utils.ItemClickSupport;
import com.elbaz.eliran.mynewsapp.utils.NYTStreams;
import com.elbaz.eliran.mynewsapp.utils.SnackbarMessagesAndVibrations;
import com.elbaz.eliran.mynewsapp.views.NYTAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static android.content.ContentValues.TAG;
import static com.elbaz.eliran.mynewsapp.controllers.fragments.TabFragment1.BUNDLE_URL;

/**
 * Created by Eliran Elbaz on 06-Jul-19.
 */
public class TabFragment3 extends Fragment {
    private Disposable mDisposable;
    private List<Result> mResults;
    private NYTAdapter mNYTAdapter;
    private Boolean networkState;
    View rootView;

    // try
    @BindView(R.id.fragment_3_recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.teb_fragment3_swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_3, container, false);
        // Get RootView for snackBarMessage
        rootView = getActivity().getWindow().getDecorView().getRootView();
        // check internet connection
        this.internetConnectivityVerifier();
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

    public boolean internetConnectivityVerifier(){
        // Check for Internet connection
        networkState = CheckInternetConnection.isNetworkAvailable(getActivity().getApplicationContext());
        if (!networkState){
            SnackbarMessagesAndVibrations.showSnakbarMessage(rootView.findViewById(R.id.activity_main_root),getString(R.string.internet_connectivity));
        } return networkState;
    }

    //-----------------
    // RecyclerView Config
    //-----------------
    private void configureRecyclerView(){
        mResults = new ArrayList<>();
        mNYTAdapter = new NYTAdapter(mResults, getContext(), Glide.with(this));
        mRecyclerView.setAdapter(this.mNYTAdapter);
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
                        // Get title URL from adapter into variable
                        String url = mNYTAdapter.getUrl(position);
                        // Instantiate the WebView Activity
                        Intent intent = new Intent(getActivity(), WebPageActivity.class);
                        // Send variable data to the activity
                        intent.putExtra(BUNDLE_URL,url);
                        Log.e("TAG", "Position + URL : "+position + " " + url);
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
                internetConnectivityVerifier();
                if(!networkState){
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
        this.mDisposable = NYTStreams.streamFetchTopStories(getString(R.string.technology))
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

    //-----------------
    // Update UI
    //-----------------

    // 3 - Update UI showing only titles
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

