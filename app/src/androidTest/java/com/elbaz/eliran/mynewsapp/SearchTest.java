package com.elbaz.eliran.mynewsapp;


import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.elbaz.eliran.mynewsapp.controllers.activities.MainActivity;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.Is.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchTest {

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

    @Test
    public void searchActivity_runSearchWithoutSelectedCategory_returnErrorMessage() {
        onView(withId(R.id.menu_activity_main_search)).perform(click()); // Click on magnifying glass icon
        onView(withId(R.id.searchButton)).perform(click()); // Click on Search button while no category is selected
        onView(withText(R.string.Your_filter_field_is_empty))
                .check(matches(isDisplayed())); // Verify that a snackBar Message with correct text appears
    }

    @Test
    public void searchActivity_runSearchWithSelectedCategory_returnResults() {
        onView(withId(R.id.menu_activity_main_search)).perform(click()); // Click on magnifying glass icon
        onView(withId(R.id.checkbox_politics)).perform(click()); // Then select a category and run a search
        onView(withId(R.id.searchButton)).perform(click());
        onView(withText("Search Results"))
                .check(matches(isDisplayed()));
    }

    @Test
    public void searchActivity_runSearchWithEndDateAndAllCategories_ () {
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
    }
}
