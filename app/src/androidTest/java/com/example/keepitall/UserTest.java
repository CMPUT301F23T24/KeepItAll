package com.example.keepitall;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Test Cases for the User Class
 */
public class UserTest {

    private User user;
    private final String testUserName = "testUser";
    private final String testPassword = "testPass";
    private final String testEmail = "test@example.com";

    @Before
    public void setUp() {
        user = new User(testUserName, testPassword, testEmail);
    }

    @Test
    public void testUserCreation() {
        assertEquals(testUserName, user.getUserName());
        assertEquals(testPassword, user.getPassword());
        assertEquals(testEmail, user.getEmailAddress());
        assertNotNull(user.getItemManager());
    }

    @Test
    public void testSetUserName() {
        String newUserName = "newTestUser";
        user.setUserName(newUserName);
        assertEquals(newUserName, user.getUserName());
    }
}

