package com.elbaz.eliran.mynewsapp;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;

import com.elbaz.eliran.mynewsapp.controllers.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void searchActivity_runSearchWithoutSelectedCategory_returnErrorMessage() {
        // Click on magnifying glass icon
        onView(withId(R.id.menu_activity_main_search)).perform(click());
        // Click on Search button while no category is selected
        onView(withId(R.id.searchButton)).perform(click());
        // Verify that a snackBar Message with correct text appears
        onView(withText(R.string.Your_filter_field_is_empty))
                .check(matches(isDisplayed()));
    }

    @Test
    public void searchActivity_runSearchWithSelectedCategory_returnResults() {
        // Click on magnifying glass icon
        onView(withId(R.id.menu_activity_main_search)).perform(click());
        // Then select a category and run a search
        onView(withId(R.id.checkbox_politics)).perform(click());
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
