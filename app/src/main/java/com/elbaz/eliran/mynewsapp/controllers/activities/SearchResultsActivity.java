package com.elbaz.eliran.mynewsapp.controllers.activities;

import android.app.Activity;
import android.content.Context;
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

import com.bumptech.glide.Glide;
import com.elbaz.eliran.mynewsapp.R;
import com.elbaz.eliran.mynewsapp.models.SearchModels.Doc;
import com.elbaz.eliran.mynewsapp.models.SearchModels.NYTSearch;
import com.elbaz.eliran.mynewsapp.models.SearchModels.Response;
import com.elbaz.eliran.mynewsapp.utils.ItemClickSupport;
import com.elbaz.eliran.mynewsapp.utils.NYTStreams;
import com.elbaz.eliran.mynewsapp.views.NYTAdapterSearchResults;

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

    public static final String BUNDLE_URL= "BUNDLE_URL";

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

        // Set the recyclerView to fixed size in order to increase performances
        mRecyclerView.setHasFixedSize(true);
        this.configureToolbar();
        this.configureRecyclerView();
        this.executeHttpRequestWithRetrofit();
        this.configureOnClickRecyclerView();

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
        Toolbar toolbar = findViewById(R.id.toolbar);
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
                            // If no matches - send result to previous activity to invoke a SnackBar pop-up, then finish the activity
                            Intent back = new Intent();
                            setResult(Activity.RESULT_CANCELED, back);
                            finish();
                        }else {
                            // Inform the previous activity that results are OK
                            Intent back = new Intent();
                            setResult(Activity.RESULT_OK, back);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: "+ e );
                        // Hide loading animation when finished HttpRequest
                        Toast.makeText(SearchResultsActivity.this, getString(R.string.search_error), Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                        // Hide loading animation when finished HttpRequest
                        findViewById(R.id.searchResults_loadingAnimation).setVisibility(View.GONE);
                    }
                });
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

                        // Get title URL from adapter into variable
                        String url = mNYTAdapterSearchResults.getUrl(position);
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
