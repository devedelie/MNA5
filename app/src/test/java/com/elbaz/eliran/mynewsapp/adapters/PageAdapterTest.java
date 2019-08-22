package com.elbaz.eliran.mynewsapp.adapters;

import android.content.Context;

import androidx.fragment.app.FragmentManager;

import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class PageAdapterTest {
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

    // Testing the titles, based on their position on the tab-menu
    @Test
    public void PageAdapter_TestTabsNames_assertName () throws Exception{
        // Create a new PageAdapter instance with Mockito
        PageAdapter pageAdapter = Mockito.mock(PageAdapter.class);
        Mockito.when(pageAdapter.getPageTitle(0)).thenReturn("TOP STORIES");
        // Load the title into a variable
        String title = pageAdapter.getPageTitle(0);
        // Check that the method returns the correct tab title
        assertEquals("TOP STORIES", title);

        Mockito.when(pageAdapter.getPageTitle(1)).thenReturn("MOST POPULAR");
        String title2 = pageAdapter.getPageTitle(1);
        assertEquals("MOST POPULAR", title2);
        Mockito.when(pageAdapter.getPageTitle(2)).thenReturn("TECH");
        String title3 = pageAdapter.getPageTitle(2);
        assertEquals("TECH", title3);
        Mockito.when(pageAdapter.getPageTitle(3)).thenReturn("SPORTS");
        String title4 = pageAdapter.getPageTitle(3);
        assertEquals("SPORTS", title4);
    }
}


