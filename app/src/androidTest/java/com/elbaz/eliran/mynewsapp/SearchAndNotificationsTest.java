package com.elbaz.eliran.mynewsapp;


import android.content.Context;
import android.view.View;
import android.widget.Checkable;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.elbaz.eliran.mynewsapp.controllers.activities.MainActivity;
import com.google.common.util.concurrent.ListenableFuture;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.Is.isA;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchAndNotificationsTest {
    private Context appContext= getInstrumentation().getTargetContext();

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void SearchActivity_runSearchWithSelectedCategory_returnTenResults() throws Exception{
        onView(withId(R.id.menu_activity_main_search)).perform(click()); // Click on magnifying glass icon
        onView(withId(R.id.checkbox_politics)).perform(click()); // Then select a category and run a search
        onView(withId(R.id.searchButton)).perform(click());
        Thread.sleep(2000); // wait for stream results
        onView(withId(R.id.search_results_recyclerView)).check(new RecyclerViewItemCountAssertion(10));
    }

    @Test
    public void searchActivity_runSearchWithoutSelectedCategory_returnErrorMessage() {
        onView(withId(R.id.menu_activity_main_search)).perform(click()); // Click on magnifying glass icon
        onView(withId(R.id.searchButton)).perform(click()); // Click on Search button while no category is selected
        onView(withText(R.string.Your_filter_field_is_empty))
                .check(matches(isDisplayed())); // Verify that a snackBar Message with correct text appears
    }

    @Test
    public void searchActivity_runSearchWithEndDateAndAllCategories_returnTenResults () throws Exception {
        onView(withId(R.id.menu_activity_main_search)).perform(click());
        onView(withId(R.id.search_endDate)).perform(click());
        onView(withId(android.R.id.button1)).perform(click()); // OK button
        onView(withId(R.id.checkbox_politics)).perform(click());
        onView(withId(R.id.checkbox_travel)).perform(click());
        onView(withId(R.id.checkbox_sports)).perform(click());
        onView(withId(R.id.checkbox_entrepreneurs)).perform(click());
        onView(withId(R.id.checkbox_business)).perform(click());
        onView(withId(R.id.checkbox_arts)).perform(click());
        onView(withId(R.id.searchButton)).perform(click());
        onView(withText("Search Results"))
                .check(matches(isDisplayed()));
        Thread.sleep(2000); // wait for stream results
        onView(withId(R.id.search_results_recyclerView)).check(new RecyclerViewItemCountAssertion(10));
    }

    @Test
    public void notificationActivity_CheckTheBoxAndEnableNotifications_returnTrueIfWorkManagerIsON() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());
        onView(anyOf(withText("Notifications"), withId(R.id.over_flow_item_1))).perform(click());
        // check first if notifications are already ON
        if(isWorkScheduled("periodic_notifications")){
            onView(withId(R.id.notifications_switchButton)).perform(click()); // Turn off notifications
        }
            onView(withId(R.id.checkbox_politics)).perform(setChecked(true)); // check the checkbox (with helper method)
            onView(withId(R.id.notifications_switchButton)).perform(click()); // Turn on notifications
            boolean isWorking = isWorkScheduled("periodic_notifications");
            Assert.assertTrue(isWorking);
            onView(withId(R.id.notifications_switchButton)).perform(click()); // Turn off notifications
    }

    //----------------------------------------------------------------------------------------------
    //WorkManager Helper method
    //----------------------------------------------------------------------------------------------
    public boolean isWorkScheduled( String tag) {
        WorkManager instance = WorkManager.getInstance(appContext);
        ListenableFuture<List<WorkInfo>> statuses = instance.getWorkInfosByTag(tag);
        try {
            boolean running = false;
            List<WorkInfo> workInfoList = statuses.get();
            for (WorkInfo workInfo : workInfoList) {
                WorkInfo.State state = workInfo.getState();
                running = state == WorkInfo.State.RUNNING | state == WorkInfo.State.ENQUEUED;
            }
            return running;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    //----------------------------------------------------------------------------------------------
    //Notification checkbox verifier - Helper method
    //----------------------------------------------------------------------------------------------
    public static ViewAction setChecked(final boolean checked) {
        return new ViewAction() {
            @Override
            public BaseMatcher<View> getConstraints() {
                return new BaseMatcher<View>() {
                    @Override
                    public boolean matches(Object item) {
                        return isA(Checkable.class).matches(item);
                    }

                    @Override
                    public void describeMismatch(Object item, Description mismatchDescription) {}

                    @Override
                    public void describeTo(Description description) {}
                };
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                Checkable checkableView = (Checkable) view;
                checkableView.setChecked(checked);
            }
        };
    }

    //----------------------------------------------------------------------------------------------
    //RecyclerView count - Helper method
    //----------------------------------------------------------------------------------------------

    public class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            Assert.assertThat(adapter.getItemCount(), is(expectedCount));
        }
    }

}
