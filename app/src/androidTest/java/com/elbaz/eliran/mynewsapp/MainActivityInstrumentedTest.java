package com.elbaz.eliran.mynewsapp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import androidx.test.filters.LargeTest;

import com.elbaz.eliran.mynewsapp.Controllers.Activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;



/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder (MethodSorters.NAME_ASCENDING)
public class MainActivityInstrumentedTest {
    // -------------------------------------------------------------------------------------------
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    // Configurations and actions to be taken before each test
    @Before
    public void setUp() throws Exception{ }

    // Configurations and actions to be taken after each test (ex: close the app)
    @After
    public void  tearDown() throws Exception{}

    //---------------------------------------------------------------------------------------------

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.elbaz.eliran.mynewsapp", appContext.getPackageName());
    }

    @Test
    public void testClickActionBarItem() {
        // We make sure the contextual action bar is hidden.
        onView(withId(R.id.over_flow_item_1))
                .perform(click());

        // Click on the icon - we can find it by the r.Id.
        onView(withId(R.id.over_flow_item_1))
                .perform(click());

        // Verify that we have really clicked on the icon
        // by checking the TextView content.
        onView(withId(R.string.itme_1))
                .check(matches(withText("action 1")));
    }
}
