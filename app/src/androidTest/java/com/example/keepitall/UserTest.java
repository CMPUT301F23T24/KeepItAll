package com.example.keepitall;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test Cases for the User Class for all of the methods
 * (we only have the constructors, getters, and setters)
 */
public class UserTest {

    /**
     * Test Case for the Constructor which takes in no variables
     */
    @Test
    public void testDefaultConstructor(){
        User user = new User();
        assertEquals(null, user.getUserName());
        assertEquals(null, user.getPassword());
        assertEquals(null, user.getEmailAddress());
    }

    /**
     * Test Case for the Constructor which takes in all of the variables
     */
    @Test
    public void testConstructor(){
        User user = new User("username", "password", "email");
        assertEquals("username", user.getUserName());
        assertEquals("password", user.getPassword());
        assertEquals("email", user.getEmailAddress());
    }

}
