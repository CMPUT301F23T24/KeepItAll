package com.example.keepitall;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static org.junit.Assert.assertEquals;

import static org.hamcrest.CoreMatchers.anything;

import android.content.Intent;
import android.view.View;
import android.widget.GridView;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
public class ImageGalleryTesting {

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
        onView(withId(R.id.userName_Input)).perform(ViewActions.typeText("TEMP"));
        onView(withId(R.id.password_Input)).perform(ViewActions.typeText("123"));
        onView(withId(R.id.login_Button)).perform(click());

        // click on the first element in the grid view
        onData(anything()).inAdapterView(withId(R.id.gridView)).atPosition(0).perform(click());

        // click the image Gallery button
        onView(withId(R.id.galleryButton)).perform(click());
    }

    /**
     * Checks for if we click an image, it will open the fullscreen view
     */
    @Test
    public void testViewFullscreen() {
        setup();
        // click the first image in the grid view
        onData(anything()).inAdapterView(withId(R.id.imageGridView)).atPosition(0).perform(click());
        // look for the fullscreen view (fullscreen_imageview)
        onView(withId(R.id.fullscreen_imageview)).check(matches(isDisplayed()));

    }

    @Test
    public void testBackButton() {
        setup();
        // click the first image in the grid view
        onData(anything()).inAdapterView(withId(R.id.imageGridView)).atPosition(0).perform(click());
        // look for the fullscreen view (fullscreen_imageview)
        onView(withId(R.id.fullscreen_imageview)).check(matches(isDisplayed()));
        // click the back button
        onView(withId(R.id.fullscreenimage_backbutton)).perform(click());

    }

}