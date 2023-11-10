package com.example.keepitall;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

/**
 * Test to login / registration functionality
 */
public class LoginActivityTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule = new ActivityScenarioRule<>(MainActivity.class);


    /**
     * Test the Login functionality that should all not work
     */
    @Test
    public void testLogin_Invalid(){
        // Try to login with an empty username and password
        onView(withId(R.id.userName_Input)).perform(ViewActions.clearText());
        onView(withId(R.id.password_Input)).perform(ViewActions.clearText());
        onView(withId(R.id.login_Button)).perform(click());
        // make sure that the activity doesn't change
        onView(withId(R.id.loginWindow)).check(matches(isDisplayed()));

        // Try to login with an empty username but a password
        onView(withId(R.id.userName_Input)).perform(ViewActions.clearText());
        onView(withId(R.id.password_Input)).perform(ViewActions.typeText("password"));
        onView(withId(R.id.login_Button)).perform(click());
        // make sure that the activity doesn't change
        onView(withId(R.id.loginWindow)).check(matches(isDisplayed()));

        // Try to login with an empty password but a username
        onView(withId(R.id.userName_Input)).perform(ViewActions.typeText("username"));
        onView(withId(R.id.password_Input)).perform(ViewActions.clearText());
        onView(withId(R.id.login_Button)).perform(click());
        // make sure that the activity doesn't change
        onView(withId(R.id.loginWindow)).check(matches(isDisplayed()));
    }

    /**
     * Performs a test login with valid information, however the user doesn't exist
     */
    @Test
    public void testLogin_ValidNoUser(){
        // The current only account is the developer account
        //username: dev
        //password: pass

        // try to login with both the username and password
        // that do not exist
        onView(withId(R.id.userName_Input)).perform(ViewActions.typeText("NotAUser"));
        onView(withId(R.id.password_Input)).perform(ViewActions.typeText("NotAPassword"));
        onView(withId(R.id.login_Button)).perform(click());
        //make sure that the activity doesn't change
        onView(withId(R.id.loginWindow)).check(matches(isDisplayed()));
    }

    /**
     * Performs a Test with a valid account and transitions to the next activity (HomePageActivity)
     */
    @Test
    public void testLogin(){
        // Login with the developer account
        onView(withId(R.id.userName_Input)).perform(ViewActions.typeText("dev"));
        onView(withId(R.id.password_Input)).perform(ViewActions.typeText("pass"));
        onView(withId(R.id.login_Button)).perform(click());
        // make sure that we have changed activities
        onView(withId(R.id.loginWindow)).check(doesNotExist());
        // Something from the next activity should be displayed
        //TODO: Swap this to a check of the home page
        onView(withId(R.id.searchText)).check(matches(isDisplayed()));
    }

    /**
     * Performs a Test to make sure that we can open and close the register account activity
     */
    @Test
    public void testRegisterAccount_Open_Close(){
        // click the register button
        onView(withId(R.id.signUpText)).perform(click());
        // make sure that the register account activity is displayed
        onView(withId(R.id.RegisterWindow)).check(matches(isDisplayed()));
        // click the cancel button (which is the negative button)
        onView(withId(android.R.id.button2)).perform(click());
        // make sure that the register account activity is no longer displayed
        onView(withId(R.id.RegisterWindow)).check(doesNotExist());
    }

    /**
     * Performs a Test to make sure that only valid usernames can be registered
     * longer than 3 characters
     * shorter than 15 characters
     * not already taken
     */
    @Test
    public void testRegisterAccount_InvalidUsername(){
        // click the register button
        onView(withId(R.id.signUpText)).perform(click());
        // we will have valid password and email to confirm we are only testing the username
        onView(withId(R.id.Register_Email_Input)).perform(ViewActions.typeText("email@gmail.com"));
        onView(withId(R.id.Register_Password_Input)).perform(ViewActions.typeText("password"));
        onView(withId(R.id.Register_Confirm_Password_Input)).perform(ViewActions.typeText("password"));
        // enter an invalid username (to short)
        onView(withId(R.id.Register_Username_Input)).perform(ViewActions.typeText("a"));
        //click the confirm button
        onView(withId(android.R.id.button1)).perform(click());
        // The window should STILL BE OPEN, but not allow us to register
        onView(withId(R.id.RegisterWindow)).check(matches(isDisplayed()));
        // enter an invalid username (to long)
        onView(withId(R.id.Register_Username_Input)).perform(ViewActions.clearText());
        onView(withId(R.id.Register_Username_Input)).perform(ViewActions.typeText("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
        //click the confirm button
        onView(withId(android.R.id.button1)).perform(click());
        // The window should STILL BE OPEN, but not allow us to register
        onView(withId(R.id.RegisterWindow)).check(matches(isDisplayed()));
        // enter an invalid username (already taken) - dev is the only account
        onView(withId(R.id.Register_Username_Input)).perform(ViewActions.clearText());
        onView(withId(R.id.Register_Username_Input)).perform(ViewActions.typeText("dev"));
        //click the confirm button
        onView(withId(android.R.id.button1)).perform(click());
        // The window should STILL BE OPEN, but not allow us to register
        onView(withId(R.id.RegisterWindow)).check(matches(isDisplayed()));
    }

    /**
     * Performs a Test to make sure that only valid passwords can be registered
     * Confirm password must match password
     */
    @Test
    public void testRegisterAccount_InvalidPassword(){
        // click the register button
        onView(withId(R.id.signUpText)).perform(click());
        // we will have valid username and email to confirm we are only testing the password
        onView(withId(R.id.Register_Username_Input)).perform(ViewActions.typeText("username"));
        onView(withId(R.id.Register_Email_Input)).perform(ViewActions.typeText("email@gmail.com"));
        // enter a password
        onView(withId(R.id.Register_Password_Input)).perform(ViewActions.typeText("password"));
        // enter a confirm password that doesn't match
        onView(withId(R.id.Register_Confirm_Password_Input)).perform(ViewActions.typeText("password1"));
        //click the confirm button
        onView(withId(android.R.id.button1)).perform(click());
        // The window should STILL BE OPEN, but not allow us to register
        onView(withId(R.id.RegisterWindow)).check(matches(isDisplayed()));
    }

    /**
     * Performs a Test to make sure that the clear button properly clears all of the fields
     */
    @Test
    public void testRegisterAccount_Clear(){
        // click the register button
        onView(withId(R.id.signUpText)).perform(click());
        // Set arbitrary values for the username, email, and password
        onView(withId(R.id.Register_Username_Input)).perform(ViewActions.typeText("username"));
        onView(withId(R.id.Register_Email_Input)).perform(ViewActions.typeText("email@gmail.com"));
        onView(withId(R.id.Register_Password_Input)).perform(ViewActions.typeText("password"));
        onView(withId(R.id.Register_Confirm_Password_Input)).perform(ViewActions.typeText("password"));
        // click the clear button
        onView(withId(android.R.id.button3)).perform(click());
        // make sure that all of the fields are empty
        onView(withId(R.id.Register_Username_Input)).check(matches(withText("")));
        onView(withId(R.id.Register_Email_Input)).check(matches(withText("")));
        onView(withId(R.id.Register_Password_Input)).check(matches(withText("")));
        onView(withId(R.id.Register_Confirm_Password_Input)).check(matches(withText("")));
    }

    /**
     * Perform a test to ensure that we can register a valid account
     * and that we can login with THAT account
     */
    @Test
    public void testRegisterAccount_Valid(){
        // First we will try to login with the account we are about to register
        onView(withId(R.id.userName_Input)).perform(ViewActions.typeText("username"));
        onView(withId(R.id.password_Input)).perform(ViewActions.typeText("password"));
        onView(withId(R.id.login_Button)).perform(click());
        // make sure that the activity doesn't change
        onView(withId(R.id.loginWindow)).check(matches(isDisplayed()));

        // click the register button to create a new account
        onView(withId(R.id.signUpText)).perform(click());
        // Set  values for the username, email, and password
        onView(withId(R.id.Register_Username_Input)).perform(ViewActions.typeText("username"));
        onView(withId(R.id.Register_Email_Input)).perform(ViewActions.typeText("email@gmail.com"));
        onView(withId(R.id.Register_Password_Input)).perform(ViewActions.typeText("password"));
        onView(withId(R.id.Register_Confirm_Password_Input)).perform(ViewActions.typeText("password"));
        // click the confirm button
        onView(withId(android.R.id.button1)).perform(click());

        // Now we will try to login with the account we just registered (that failed to login originally)
        // Clear the username and password fields
        onView(withId(R.id.userName_Input)).perform(ViewActions.clearText());
        onView(withId(R.id.password_Input)).perform(ViewActions.clearText());
        onView(withId(R.id.userName_Input)).perform(ViewActions.typeText("username"));
        onView(withId(R.id.password_Input)).perform(ViewActions.typeText("password"));
        onView(withId(R.id.login_Button)).perform(click());
        // make sure that we have changed activities
        onView(withId(R.id.loginWindow)).check(doesNotExist());
        // Now we want to delete the account we just created
        KeepItAll.getInstance().deleteUser(KeepItAll.getInstance().getUserByName("username"));
    }


}
