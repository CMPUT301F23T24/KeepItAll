package com.example.keepitall;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test Cases for the User Class for all of the methods
 * (we only have the constructors, getters, and setters)
 */
public class UserTest {

    @Test
    public void testDefaultConstructor(){
        User user = new User();
        assertEquals("", user.getUserName());
        assertEquals("", user.getPassword());
        assertEquals("", user.getEmailAddress());
    }

}
