package com.elbaz.eliran.mynewsapp.Controllers.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.elbaz.eliran.mynewsapp.R;

/**
 * Created by Eliran Elbaz on 06-Jul-19.
 */
public class TabFragment4 extends Fragment {
    public static TabFragment4 newInstance() {
        return (new TabFragment4());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_tab_4, container, false);
        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.fragment_page_4_rootview);

        return rootView;
    }
}