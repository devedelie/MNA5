package com.elbaz.eliran.mynewsapp.controllers.activities;

import android.content.Context;

import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.elbaz.eliran.mynewsapp.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
//@FixMethodOrder (MethodSorters.NAME_ASCENDING)
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
        Context appContext = getInstrumentation().getTargetContext();
        assertEquals("com.elbaz.eliran.mynewsapp", appContext.getPackageName());
    }

    @Test
    public void MainActivity_checkMainViewElementsVisibility_returnMatch() {
        onView(withText("TOP STORIES")).check(matches(isCompletelyDisplayed()));
        onView(withText("MOST POPULAR")).check(matches(isCompletelyDisplayed()));
        onView(withText("TECH")).check(matches(isCompletelyDisplayed()));
        onView(withText("SPORTS")).check(matches(isCompletelyDisplayed()));
        onView(withText("My News")).check(matches(isCompletelyDisplayed()));
        // Open the overflow menu OR open the options menu,
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Notifications")).check(matches(isCompletelyDisplayed()));
        onView(withText("Help")).check(matches(isCompletelyDisplayed()));
        onView(withText("About")).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void MainActivity_testClickInsertItem_click() {
        // Performs click test by opening the menu and clicking on each item(revert with pressBack())
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(anyOf(withText("Notifications"), withId(R.id.over_flow_item_1))).perform(click());
        Espresso.pressBack();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(anyOf(withText("Help"), withId(R.id.over_flow_item_2))).perform(click());
        onView(withId(R.id.btn_dialog)).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(anyOf(withText("About"), withId(R.id.over_flow_item_3))).perform(click());

    }

//    @Test
//    public void checkNavigationSearchArticle(){
//        onView(withId(R.id.searchButton)).perform(click());
//        intended(hasComponent(SearchAndNotificationsActivity.class.getName()));
//    }
}
