package com.elbaz.eliran.mynewsapp.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.elbaz.eliran.mynewsapp.R;
import com.elbaz.eliran.mynewsapp.models.TopStoriesModels.NYTNews;
import com.elbaz.eliran.mynewsapp.models.TopStoriesModels.Result;
import com.elbaz.eliran.mynewsapp.utils.ItemClickSupport;
import com.elbaz.eliran.mynewsapp.utils.NYTStreams;
import com.elbaz.eliran.mynewsapp.views.NYTAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static android.content.ContentValues.TAG;

public class DrawerCategoriesActivity extends AppCompatActivity {

    public static final String BUNDLE_URL= "BUNDLE_URL";

    private Disposable mDisposable;
    private List<Result> mResults;
    private NYTAdapter mNYTAdapter;

    // ButterKnife
    @BindView(R.id.drawer_categories_recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.drawer_categories_swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_categories);

        ButterKnife.bind(this);
        // Set the recyclerView to fixed size in order to increase performances
        mRecyclerView.setHasFixedSize(true);
        this.configureToolbar();
        this.configureRecyclerView();
        this.executeHttpRequestWithRetrofit();
        this.configureOnClickRecyclerView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    // Detect the click on toolbars's "back" button and finish the current activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if ( id == android.R.id.home ) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 1 - Toolbar execution
     */
    private void configureToolbar(){
        //Get the toolbar (Serialise)
        Toolbar toolbar = findViewById(R.id.toolbar);
        // Sets the Toolbar
        setSupportActionBar(toolbar);
        //Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the upper button (back button)
        actionBar.setDisplayHomeAsUpEnabled(true);
        //set Title
        getSupportActionBar().setTitle(MainActivity.pageTitle);
    }


    //-----------------
    // RecyclerView Config
    //-----------------
    protected void configureRecyclerView(){
        mResults = new ArrayList<>();
        mNYTAdapter = new NYTAdapter(this.mResults, getApplicationContext(), Glide.with(this));
        mRecyclerView.setAdapter(this.mNYTAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    //-----------------
    // HTTP (RxJAVA)
    //-----------------

    // 1 - Execute the stream
    private void executeHttpRequestWithRetrofit(){
        // 1.2 - Execute the stream subscribing to Observable defined inside NYTStream
        this.mDisposable = NYTStreams.streamFetchTopStories(MainActivity.category)
                .subscribeWith(new DisposableObserver<NYTNews>(){

                    @Override
                    public void onNext(NYTNews nytNews) {
                        // 1.3 - Update UI with list of titles
                        Log.e(TAG, "onNext DrawerActivity" );
                        updateUI(nytNews.getResults());
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError DrawerActivity: "+ e );
                        Toast.makeText(DrawerCategoriesActivity.this, getString(R.string.internet_connectivity_drawer), Toast.LENGTH_LONG).show();
                        finish();
                    }
                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete DrawerActivity");
                        findViewById(R.id.drawer_activities_loadingAnimation).setVisibility(View.GONE);
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
                        Intent intent = new Intent(v.getContext(), WebPageActivity.class);
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
