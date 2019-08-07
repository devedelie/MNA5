package com.elbaz.eliran.mynewsapp.Controllers.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elbaz.eliran.mynewsapp.Controllers.Activities.WebPageActivity;
import com.elbaz.eliran.mynewsapp.R;
import com.elbaz.eliran.mynewsapp.Utils.CheckInternetConnection;
import com.elbaz.eliran.mynewsapp.Utils.ItemClickSupport;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

import static com.elbaz.eliran.mynewsapp.Controllers.Fragments.TabFragment1.BUNDLE_URL;

/**
 * Created by Eliran Elbaz on 07-Aug-19.
 */
public abstract class BaseFragment extends Fragment {

    private Disposable mDisposable;
    private Boolean networkState;
    @BindView(R.id.fragment_1_recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.teb_fragment1_swipe_container) SwipeRefreshLayout mSwipeRefreshLayout;

    // 1 - Force developer implement those methods
    protected abstract BaseFragment newInstance();
    protected abstract int getFragmentLayout();
    protected abstract void configureDesign();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //  Get layout identifier from abstract method
        View view = inflater.inflate(getFragmentLayout(), container, false);

        // Configure Design (should call this method instead of override onCreateView())
        this.configureDesign();

        // Check for Internet connection
        networkState = CheckInternetConnection.isNetworkAvailable(getActivity().getApplicationContext());
        if (!networkState){
            internetConnectivityMessage();
        }
        // Binding Views
        ButterKnife.bind(this, view);

        this.configureOnClickRecyclerView();
        return(view);
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

    // This method will be called onDestroy to avoid any risk of memory leaks.
    protected void disposeWhenDestroy(){
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) this.mDisposable.dispose();
    }

    // -----------------
    // ACTION RecyclerView onClick
    // -----------------

    // 1 - Configure item click on RecyclerView
    protected void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(mRecyclerView, R.layout.recyclerview_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Log.e("TAG", "Position : "+position);
                        // Get title URL from adapter into variable
                        String url="";
                        // Instantiate the WebView Activity
                        Intent intent = new Intent(getActivity(), WebPageActivity.class);
                        // Send variable data to the activity
                        intent.putExtra(BUNDLE_URL,url);
                        startActivity(intent);
                    }
                });
    }


}
