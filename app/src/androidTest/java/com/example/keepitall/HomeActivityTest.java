package com.example.keepitall;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import static org.hamcrest.CoreMatchers.anything;

import android.content.Intent;
import android.content.res.Resources;
import android.view.KeyEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.GridView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * HomePage tests
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeActivityTest {
    private GridView gridView;

    @Rule
    public IntentsTestRule<MainActivity> intentsTestRule = new IntentsTestRule<MainActivity>(MainActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra("userName", "TestUser");
            return intent;
        }
    };

    /**
     * Login using test account to be able to test homePage functionalities
     */
    public void setup() {
        onView(withId(R.id.userName_Input)).perform(typeText("test"));
        onView(withId(R.id.password_Input)).perform(typeText("test"));
        onView(withId(R.id.login_Button)).perform(click());
    }

    /**
     * Checks if itemView displays 2 items, always maintain 2 items in test user
     */
    @Test
    public void testCorrectItemAmount() {
        setup();
        onView(withId(R.id.gridView)).check((view, noViewFoundException) -> gridView = (GridView) view);
        HomePageAdapter gridViewAdapter = (HomePageAdapter) gridView.getAdapter();
        assertEquals(3, gridViewAdapter.getCount());
    }

    /**
     * AddButton test, press add button -> go back to homepage
     */
    @Test
    public void testAddButton() {
        setup();
        onView(withId(R.id.addButton)).perform(click());
        intended(hasComponent(AddItemActivity.class.getName()));
        onView(isRoot()).perform(waitIdling(3000));
        onView(withId(R.id.viewBackButton)).perform(click());
        onView(withId(R.id.addButton)).check(matches(isDisplayed()));
    }


    /**
     * ViewItem test, clicks on an item -> return back to homepage after
     */
    @Test
    public void testViewItem() {
        setup();
        onData(anything()).inAdapterView(withId(R.id.gridView)).atPosition(0).perform(click());
        intended(hasComponent(ViewItemActivity.class.getName()));
        onView(isRoot()).perform(waitIdling(3000));
        onView(withId(R.id.viewBackButton)).perform(click());
        onView(withId(R.id.addButton)).check(matches(isDisplayed()));
    }

    /**
     * Tests logout button, would go back to login page
     */
    @Test
    public void testLogoutButton() {
        setup();
        onView(withId(R.id.logoutButton)).perform(click());
        onView(withId(R.id.login_Button)).check(matches(isDisplayed()));
    }

    /**
     * Tests out the sorting mechanism of homepage
     */
    @Test
    public void testSort() {
        setup();
        onView(withId(R.id.sortButton)).perform(click());
        onView(withId(R.id.sortValueButton)).perform(click());
        onView(withText("ASCENDING")).perform(click());
        waitIdling(3000);
        onData(anything()).inAdapterView(withId(R.id.gridView)).atPosition(0).perform(click());
        onView(withId(R.id.itemNameText)).check(matches(withText("test23")));
    }

    /**
     * Adds a delay for testing
     * @param millis: milliseconds
     * @return isRoot() or message
     */
    public static ViewAction waitIdling(final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "wait for " + millis + "milliseconds";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }
}
