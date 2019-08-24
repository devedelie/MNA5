package com.elbaz.eliran.mynewsapp;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.elbaz.eliran.mynewsapp.adapters.PageAdapter;
import com.elbaz.eliran.mynewsapp.controllers.fragments.TabFragment1;
import com.elbaz.eliran.mynewsapp.controllers.fragments.TabFragment2;
import com.elbaz.eliran.mynewsapp.controllers.fragments.TabFragment3;
import com.elbaz.eliran.mynewsapp.controllers.fragments.TabFragment4;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Eliran Elbaz on 13-Jul-19.
 */
public class MainActivityTest {
    Context mContext;
    FragmentManager mFragmentManager;

    // Test for returning 4 tabs
    @Test
    public void PageAdapter_TestNumberOfTabs_assertCorrectNumber () throws Exception{
        // Create a new PageAdapter instance
        PageAdapter pageAdapter = new PageAdapter(mContext, mFragmentManager );
        // Check that the method returns 4 as the number of tabs to show
        assertEquals(4, pageAdapter.getCount());
    }

    //Test to verify that the fragments are not returning null
    @Test
    public void Fragment1ShouldNotBeNull() throws Exception {
        Fragment Fragment1 = new TabFragment1();
        assertNotNull(Fragment1);
    }
    @Test
    public void Fragment2ShouldNotBeNull() throws Exception {
        Fragment Fragment2 = new TabFragment2();
        assertNotNull(Fragment2);
    }
    @Test
    public void Fragment3ShouldNotBeNull() throws Exception {
        Fragment Fragment3 = new TabFragment3();
        assertNotNull(Fragment3);
    }
    @Test
    public void Fragment4ShouldNotBeNull() throws Exception {
        Fragment Fragment4 = new TabFragment4();
        assertNotNull(Fragment4);
    }

}