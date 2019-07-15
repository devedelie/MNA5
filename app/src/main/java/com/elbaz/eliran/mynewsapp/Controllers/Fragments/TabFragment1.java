package com.elbaz.eliran.mynewsapp.Controllers.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.elbaz.eliran.mynewsapp.Adapters.NewsRecyclerViewAdapter;
import com.elbaz.eliran.mynewsapp.Models.Constants;
import com.elbaz.eliran.mynewsapp.Models.NYTNews;
import com.elbaz.eliran.mynewsapp.R;

import java.util.ArrayList;

/**
 * Created by Eliran Elbaz on 06-Jul-19.
 */
public class TabFragment1 extends Fragment {

    private ArrayList<NYTNews> mNYTNews = new ArrayList<>();
    private NewsRecyclerViewAdapter mNewsRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    Constants mConstants;


    public static TabFragment1 newInstance() {
        return (new TabFragment1());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_1, container, false);
        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.fragment_page_1_rootview);

//        loadJson();

        return rootView;
    }


}
