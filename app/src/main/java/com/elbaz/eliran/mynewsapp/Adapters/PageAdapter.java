package com.elbaz.eliran.mynewsapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.elbaz.eliran.mynewsapp.Controllers.Fragments.TabFragment1;
import com.elbaz.eliran.mynewsapp.Controllers.Fragments.TabFragment2;
import com.elbaz.eliran.mynewsapp.Controllers.Fragments.TabFragment3;
import com.elbaz.eliran.mynewsapp.Controllers.Fragments.TabFragment4;

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
                return TabFragment1.newInstance();
            case 1:
                return TabFragment2.newInstance();
            case 2:
                return TabFragment3.newInstance();
            case 3:
                return TabFragment4.newInstance();
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
