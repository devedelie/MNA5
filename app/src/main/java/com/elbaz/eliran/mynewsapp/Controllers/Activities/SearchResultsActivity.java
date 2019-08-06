package com.elbaz.eliran.mynewsapp.Controllers.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elbaz.eliran.mynewsapp.Models.SearchModels.Doc;
import com.elbaz.eliran.mynewsapp.Models.SearchModels.NYTSearch;
import com.elbaz.eliran.mynewsapp.Models.SearchModels.Response;
import com.elbaz.eliran.mynewsapp.R;
import com.elbaz.eliran.mynewsapp.Utils.NYTStreams;
import com.elbaz.eliran.mynewsapp.Views.NYTAdapterSearchResults;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static android.content.ContentValues.TAG;

public class SearchResultsActivity extends AppCompatActivity {

    private Disposable mDisposable;
    private List<Response> mResponses;
    private List<Doc> mDocs;
    private NYTAdapterSearchResults mNYTAdapterSearchResults;
    Context mContext;
    private String userQueryString;
    private String userBeginDate;
    private String userEndDate;
    private String filterString;
    private String sort;

    // ButterKnife
    @BindView(R.id.search_results_recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        // Create a query String for the stream
        Intent intent = getIntent();
        userBeginDate = intent.getStringExtra(getString(R.string.begin_date));
        userEndDate = intent.getStringExtra(getString(R.string.end_date));
        filterString = intent.getStringExtra(getString(R.string.filter_query));
        userQueryString = intent.getStringExtra(getString(R.string.search_query));
        sort = intent.getStringExtra(getString(R.string.sort));
        Log.d(TAG, "API string built:  "+ userBeginDate + " " + userEndDate + " "+ filterString + " "+ userQueryString + " " + sort);

        ButterKnife.bind(this);

        this.configureToolbar();
        this.configureRecyclerView();
        this.executeHttpRequestWithRetrofit();

    }
    
    @Override
    public void onDestroy(){
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    /**
     * 1 - Toolbar execution
     */
    private void configureToolbar(){
        //Get the toolbar (Serialise)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar
        setSupportActionBar(toolbar);
        //Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the upper button (back button)
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // This method will be called onDestroy to avoid any risk of memory leaks.
    private void disposeWhenDestroy(){
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }

    //-----------------
    // RecyclerView Config
    //-----------------
    protected void configureRecyclerView(){
        mDocs = new ArrayList<>();
        mNYTAdapterSearchResults = new NYTAdapterSearchResults(this.mDocs, mContext, Glide.with(this));
        mRecyclerView.setAdapter(this.mNYTAdapterSearchResults);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext)); /// to check
    }

    //-----------------
    // HTTP (RxJAVA)
    //-----------------

    // 1 - Execute the stream
    private void executeHttpRequestWithRetrofit(){
        // 1.2 - Execute the stream subscribing to Observable defined inside NYTStream
        this.mDisposable = NYTStreams.streamFetchSearchResults(userBeginDate, userEndDate, filterString,userQueryString, sort)
                .subscribeWith(new DisposableObserver<NYTSearch>(){

                    @Override
                    public void onNext(NYTSearch nytSearch) {
                        // 1.3 - Update UI with list of titles
                        Log.e(TAG, "onNext" );
                        updateUI(nytSearch.getResponse().getDocs());
                        // Display a message if returned 0 results
                        int sizeOfList = nytSearch.getResponse().getDocs().size();
                        if (sizeOfList == 0){
                            Toast.makeText(getApplicationContext(), "Couldn't find results for your query", Toast.LENGTH_LONG).show();
                        }
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

    // 3 - Update UI showing only titles
    private void updateUI(List<Doc> titles) {
        Log.d(TAG, "updateUI: ");
        // completely erase the previous list of results each time
        // in order to avoid duplicating it due to  .addAll()
        mDocs.clear();
        mDocs.addAll(titles);
        mNYTAdapterSearchResults.notifyDataSetChanged();
    }
}
