package com.elbaz.eliran.mynewsapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.elbaz.eliran.mynewsapp.R;

/**
 * Created by Eliran Elbaz on 06-Jul-19.
 */
public class PageAdapter extends FragmentPagerAdapter {

    //Default Constructor
    public PageAdapter(FragmentManager mgr) {
        super(mgr);
    }

    @Override
    public int getCount() {
        return(4);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return Tab1Fragment.newInstance();
            case 1:
                return Tab2Fragment.newInstance();
            case 2:
                return Tab3Fragment.newInstance();
            case 3:
                return Tab4Fragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "TOP STORIES";
            case 1:
                return "MOST POPULAR";
            case 2:
                return "TECH";
            case 3:
                return "SPORT";
            default:
                return null;
        }
    }
}
