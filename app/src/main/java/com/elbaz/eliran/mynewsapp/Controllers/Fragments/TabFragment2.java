package com.elbaz.eliran.mynewsapp.Controllers.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elbaz.eliran.mynewsapp.R;

/**
 * Created by Eliran Elbaz on 06-Jul-19.
 */
public class TabFragment2 extends Fragment {
    public static TabFragment2 newInstance() {
        return (new TabFragment2());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tab_2, container, false);
    }
}