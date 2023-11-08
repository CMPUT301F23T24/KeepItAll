package com.example.keepitall;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.action.ViewActions.click;

import static org.hamcrest.CoreMatchers.anything;

import android.app.Activity;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import androidx.test.runner.lifecycle.Stage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collection;


/**
 * HomePage tests
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeActivityTest {
    private Intent intent;

    @Before
    public void setup() {
        intent = new Intent(ApplicationProvider.getApplicationContext(), HomePageActivity.class);
        intent.putExtra("userName", "TestUser");
    }


    /**
     * AddButton test
     */
    @Test
    public void testAddButton() {
        ActivityScenario.launch(intent);
        onView(withId(R.id.addButton)).perform(click());
        Assert.assertEquals(getActivityInstance().getClass(), AddItemActivity.class);
    }


    /**
     * ViewItem test
     */
    @Test
    public void testViewItem() {
        ActivityScenario.launch(intent);
        onData(anything()).inAdapterView(withId(R.id.gridView)).atPosition(0).perform(click());
        Assert.assertEquals(getActivityInstance().getClass(), ViewItemActivity.class);
    }




    private Activity getActivityInstance() {
        final Activity[] currentActivity = {null};
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            public void run() {
                Collection resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED);
                if (resumedActivities.iterator().hasNext()) {
                    currentActivity[0] = (Activity) resumedActivities.iterator().next();
                }
            }
        });

        return currentActivity[0];
    }
}
