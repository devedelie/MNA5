package com.elbaz.eliran.mynewsapp;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.elbaz.eliran.mynewsapp.controllers.activities.MainActivity;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.repeatedlyUntil;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainActivityInstrumentedTest {
    private Context appContext= getInstrumentation().getTargetContext();
    // -------------------------------------------------------------------------------------------
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    // Configurations and actions to be taken before each test
    @Before
    public void setUp() throws Exception{

    }

    // Configurations and actions to be taken after each test (ex: close the app)
    @After
    public void  tearDown() throws Exception{}

    //---------------------------------------------------------------------------------------------

    @Test
    public void useAppContext() {
        // Context of the app under test.
        assertEquals("com.elbaz.eliran.mynewsapp", appContext.getPackageName());
    }

    @Test
    public void MainActivity_checkTabsRecyclerViewSizeIsBiggerThanZero_returnTrue() throws Exception{
        Thread.sleep(2000); // wait for stream results
        onView(withId(R.id.fragment_1_recyclerView)).check(new RecyclerViewItemCountAssertion(true)); // check if true
        Espresso.onView(ViewMatchers.withId(R.id.activity_main_viewpager)).perform(ViewActions.swipeLeft());       // Swipe left
        Thread.sleep(2000); // wait for stream results
        onView(withId(R.id.fragment_2_recyclerView)).check(new RecyclerViewItemCountAssertion(true));
        Espresso.onView(ViewMatchers.withId(R.id.activity_main_viewpager)).perform(ViewActions.swipeLeft());
        Thread.sleep(2000); // wait for stream results
        onView(withId(R.id.fragment_3_recyclerView)).check(new RecyclerViewItemCountAssertion(true));
        Espresso.onView(ViewMatchers.withId(R.id.activity_main_viewpager)).perform(ViewActions.swipeLeft());
        Thread.sleep(2000); // wait for stream results
        onView(withId(R.id.fragment_4_recyclerView)).check(new RecyclerViewItemCountAssertion(true));
    }

    public class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final boolean expectedCount;

        public RecyclerViewItemCountAssertion(boolean expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            Assert.assertThat(adapter.getItemCount() > 0 , is(expectedCount));
        }
    }

    @Test
    public void MainActivity_checkMainViewElementsVisibility_returnMatch() throws Exception {
        onView(withId(R.id.menu_activity_main_search)).check(matches(isCompletelyDisplayed())); // magnifying glass icon
        onView(withId(R.id.activity_main_root)).check(matches(isCompletelyDisplayed())); // main layout
        onView(withId(R.id.activity_main_drawer_layout)).check(matches(isCompletelyDisplayed())); // drawer layout
        onView(withId(R.id.fragment_page_1_rootview)).check(matches(isCompletelyDisplayed())); // fragment 1 layout
        onView(withText("TOP STORIES")).check(matches(isCompletelyDisplayed()));
        onView(withText("MOST POPULAR")).check(matches(isCompletelyDisplayed()));
        onView(withText("TECH")).check(matches(isCompletelyDisplayed()));
        onView(withText("SPORTS")).check(matches(isCompletelyDisplayed()));
        onView(withText("My News")).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void ViewPager_viewPagerSwipeLeft_returnFragmentsLayoutIDMatching() throws Exception{
        int maxAttempts=3;
        onView(withId(R.id.activity_main_viewpager)).perform(repeatedlyUntil(swipeLeft(), hasDescendant(withId(R.id.fragment_1_recyclerView)), maxAttempts));
        onView(withId(R.id.activity_main_viewpager)).perform(repeatedlyUntil(swipeLeft(), hasDescendant(withId(R.id.fragment_2_recyclerView)), maxAttempts));
        onView(withId(R.id.activity_main_viewpager)).perform(repeatedlyUntil(swipeLeft(), hasDescendant(withId(R.id.fragment_3_recyclerView)), maxAttempts));
        onView(withId(R.id.activity_main_viewpager)).perform(repeatedlyUntil(swipeLeft(), hasDescendant(withId(R.id.fragment_4_recyclerView)), maxAttempts));
    }

    @Test
    public void MainActivity_checkOverFlowMenuItems_returnMatch(){
        // Open the overflow menu OR open the options menu,
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(withText("Notifications")).check(matches(isCompletelyDisplayed()));
        onView(withText("Help")).check(matches(isCompletelyDisplayed()));
        onView(withText("About")).check(matches(isCompletelyDisplayed()));
    }

    @Test
    public void MainActivity_testClickInsertItem_returnCorrectActivity() throws Exception{
        onView(anyOf(withId(R.id.menu_activity_main_search))).perform(click());
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText("Search")));
        Espresso.pressBack();
        // Performs click test by opening the menu and clicking on each item(revert with pressBack())
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(anyOf(withText("Notifications"), withId(R.id.over_flow_item_1))).perform(click());
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.toolbar))))
                .check(matches(withText("Notifications")));
        Espresso.pressBack();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(anyOf(withText("Help"), withId(R.id.over_flow_item_2))).perform(click());
        onView(withId(R.id.btn_dialog)).perform(click());
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(anyOf(withText("About"), withId(R.id.over_flow_item_3))).perform(click());
        Espresso.pressBack();

    }

    @Test
    public void navigationDrawer_openAndCloseDrawer() throws Exception {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withContentDescription(R.string.navigation_drawer_close)).perform(click());
    }

    @Test
    public void navigationDrawer_openDrawerAndPerformClick() throws Exception {
        onView(withContentDescription(R.string.navigation_drawer_open)).perform(click());
        onView(withText(R.string.food)).perform(click());
    }
}
