package com.elbaz.eliran.mynewsapp.Controllers.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elbaz.eliran.mynewsapp.Models.GithubUser;
import com.elbaz.eliran.mynewsapp.R;
import com.elbaz.eliran.mynewsapp.Utils.GithubCalls;

import java.util.List;

/**
 * Created by Eliran Elbaz on 06-Jul-19.
 */
public class TabFragment2 extends Fragment implements GithubCalls.Callbacks{

    TextView mTextView;

    public static TabFragment2 newInstance() {
        return (new TabFragment2());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_2, container, false);
        mTextView = (TextView) view.findViewById(R.id.github_text);
        // here I think should come mRecyclerView = (RecyclerView) findView......


        executeHttpRequestWithRetrofit();

        return view;

    }

    // 4 - Execute HTTP request and update UI
    private void executeHttpRequestWithRetrofit(){
        this.updateUIWhenStartingHTTPRequest();
        GithubCalls.fetchCategoryOfNews(this, "home.json");
    }

    //-----------------
    // HTTP Request
    //-----------------
    @Override
    public void onResponse(@Nullable List<GithubUser> users) {
        if (users != null) this.updateUIWithListOfUsers(users);
    }

    @Override
    public void onFailure() {
        this.updateUIWhenStoppingHTTPRequest("Error: Please check your internet connection");
    }
    //-----------------

    //-----------------
    // Update UI
    //-----------------

    private void updateUIWhenStartingHTTPRequest(){
        this.mTextView.setText("Downloading...");
    }

    private void updateUIWhenStoppingHTTPRequest(String response){
        this.mTextView.setText(response);
    }

    // 3 - Update UI showing only name of users
    private void updateUIWithListOfUsers(List<GithubUser> users){
        StringBuilder stringBuilder = new StringBuilder();
        for (GithubUser user : users){
            stringBuilder.append("-"+user.getLogin()+"\n");
        }
        updateUIWhenStoppingHTTPRequest(stringBuilder.toString());
    }
}
