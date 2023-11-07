package com.example.keepitall;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.assertEquals;

import static org.hamcrest.CoreMatchers.anything;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;


/**
 * HomePage tests
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeActivityTest {
    private GridView gridView;
    private ListAdapter gridViewAdapter;

    @Rule
    public IntentsTestRule<HomePageActivity> intentsTestRule = new IntentsTestRule<HomePageActivity>(HomePageActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent();
            intent.putExtra("userName", "TestUser");
            return intent;
        }
    };

    /**
     * AddButton test
     */
    @Test
    public void testAddButton() {
        // IntentsTestRule starts the activity for you
        onView(withId(R.id.addButton)).perform(click());
        intended(hasComponent(AddItemActivity.class.getName()));
        onView(isRoot()).perform(waitIdling(3000));
        onView(withId(R.id.viewBackButton)).perform(click());
        onView(withId(R.id.addButton)).check(matches(isDisplayed()));
    }

    /**
     * ViewItem test
     */
    @Test
    public void testViewItem() {
        onData(anything()).inAdapterView(withId(R.id.gridView)).atPosition(0).perform(click());
        intended(hasComponent(ViewItemActivity.class.getName()));
        onView(isRoot()).perform(waitIdling(3000));
        onView(withId(R.id.viewBackButton)).perform(click());
        onView(withId(R.id.addButton)).check(matches(isDisplayed()));
    }

    /**
     * Delete one item test
     */
    @Test
    public void testDeleteItem() {
        onView(withId(R.id.gridView)).check((view, noViewFoundException) -> {
            gridView = (GridView) view;
        });
        onView(withId(R.id.deleteButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.gridView)).atPosition(0).perform(click());
        onView(withId(R.id.deleteButton)).perform(click());
        gridViewAdapter = gridView.getAdapter();
        assertEquals(1, gridViewAdapter.getCount());
    }

    /**
     * Delete multiple items test
     */
    @Test
    public void testDeleteItems() {
        onView(withId(R.id.gridView)).check((view, noViewFoundException) -> {
            gridView = (GridView) view;
        });
        onView(withId(R.id.deleteButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.gridView)).atPosition(1).perform(click());
        onData(anything()).inAdapterView(withId(R.id.gridView)).atPosition(0).perform(click());
        onView(withId(R.id.deleteButton)).perform(click());
        gridViewAdapter = gridView.getAdapter();
        assertEquals(0, gridViewAdapter.getCount());
    }


    /**
     * Adds a delay for testing
     * @param millis
     * @return
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

