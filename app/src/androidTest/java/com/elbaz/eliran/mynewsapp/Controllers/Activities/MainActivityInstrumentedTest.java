package com.elbaz.eliran.mynewsapp.Controllers.Activities;

import android.content.Context;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.elbaz.eliran.mynewsapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
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
        onView(ViewMatchers.withId(R.id.over_flow_item_1))
                .perform(click());

        // Click on the icon - we can find it by the r.Id.
        onView(withId(R.id.over_flow_item_1))
                .perform(click());

        // Verify that we have really clicked on the icon
        // by checking the TextView content.
        onView(withId(R.string.itme_1))
                .check(matches(withText("Notifications")));
    }
}
