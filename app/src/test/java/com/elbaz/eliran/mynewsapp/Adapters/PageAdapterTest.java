package com.elbaz.eliran.mynewsapp.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;

import org.junit.Test;
import org.mockito.Mockito;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
    public void TestNumberOfTabs () throws Exception{
            // Create a new PageAdapter instance
            PageAdapter pageAdapter = new PageAdapter(mContext, mFragmentManager );
            // Check that the method returns 4 as the number of tabs to show
            assertEquals(4, pageAdapter.getCount(), 0.001);
        }

    // Testing the titles, based on their position on the tab-menu
    @Test
    public void TestNameOfTabs () throws Exception{
        // Create a new PageAdapter instance with Mockito
        PageAdapter pageAdapter = Mockito.mock(PageAdapter.class);
        // Set the condition
        Mockito.when(pageAdapter.getPageTitle(1)).thenReturn("MOST POPULAR");
        // Load the title into a variable
        String title = pageAdapter.getPageTitle(1);
        // Check that the method returns the correct tab title
        assertEquals("MOST POPULAR", title);
    }



    @Test
    public void tabScroll() {
//            // 1
//            PageAdapter pageAdapter = mock(PageAdapter.class);
//
//            // 2
//            When(PageAdapter.getItem())
//
//
//
//
    }

    @Test
    public void tabTitleTest() throws Exception{
        // ask
        PageAdapter pageAdapter = mock (PageAdapter.class);

        // 2
//        pageAdapter.getItem(2);
//        when(pageAdapter.getItem(2)).thenReturn();

        // 3
//        assertEquals(2, pageAdapter.getItem(2), 0.001);


    }



}


