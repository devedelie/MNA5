package com.elbaz.eliran.mynewsapp.controllers.activities;

import androidx.viewpager.widget.ViewPager;

import com.elbaz.eliran.mynewsapp.R;
import com.elbaz.eliran.mynewsapp.adapters.PageAdapter;
import com.google.android.material.navigation.NavigationView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created by Eliran Elbaz on 10-Jul-19.
 */

@RunWith(RobolectricTestRunner.class)
//@RunWith(MockitoJUnitRunner.class)
public class MainActivityTest {
    private MainActivity mActivity;
    private ViewPager mViewPager;
    private PageAdapter mPageAdapter;
    private NavigationView mNavigationView;

    String[] dialogTextArray;

    private MainActivity activity;

    @Before
    public void setUp() throws Exception {
        activity = Robolectric.buildActivity( MainActivity.class )
                .create()
                .resume()
                .get();
    }

    @Test
    public void shouldNotBeNull() throws Exception
    {
        assertNotNull( activity );
    }

    @Test
    public void checkIfNavigationDrawerContainsItem() throws Exception {
        mActivity = Robolectric.setupActivity(MainActivity.class);
        mViewPager = mActivity.findViewById(R.id.activity_main_viewpager);
        mPageAdapter = (PageAdapter)mViewPager.getAdapter();


        mNavigationView = mActivity.findViewById(R.id.activity_main_nav_view);
        assertEquals("Top Stories", mNavigationView.getMenu().findItem(R.id.activity_main_drawer_1).getTitle().toString() );
        assertEquals("Notifications", mNavigationView.getMenu().findItem(R.id.activity_main_drawer_2).getTitle().toString() );
        assertEquals("bla bla", mNavigationView.getMenu().findItem(R.id.activity_main_drawer_3).getTitle().toString());
    }

}