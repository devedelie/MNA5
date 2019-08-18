package com.elbaz.eliran.mynewsapp.adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.elbaz.eliran.mynewsapp.controllers.fragments.TabFragment1;
import com.elbaz.eliran.mynewsapp.controllers.fragments.TabFragment2;
import com.elbaz.eliran.mynewsapp.controllers.fragments.TabFragment3;
import com.elbaz.eliran.mynewsapp.controllers.fragments.TabFragment4;
import com.elbaz.eliran.mynewsapp.R;

/**
 * Created by Eliran Elbaz on 06-Jul-19.
 */
public class PageAdapter extends FragmentPagerAdapter {
    private Context mContext;

    //Default Constructor
    public PageAdapter(Context context, FragmentManager mgr) {
        super(mgr);
        mContext = context;
    }

    @Override
    public int getCount() {
        return(4);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TabFragment1();
            case 1:
                return new TabFragment2();
            case 2:
                return new TabFragment3();
            case 3:
                return new TabFragment4();
            default:
                return null;
        }
    }

    @Override
    public String getPageTitle(int position) {
        switch (position){
            case 0:
                return mContext.getString(R.string.tab_name_1);
            case 1:
                return mContext.getString(R.string.tab_name_2);
            case 2:
                return mContext.getString(R.string.tab_name_3);
            case 3:
                return mContext.getString(R.string.tab_name_4);
            default:
                return null;
        }
    }
}
