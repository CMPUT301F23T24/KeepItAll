package com.example.keepitall;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test Cases for the User Class
 */
public class UserTest {

    /**
     *
     */
    @Test
    public void testCompareTo() {
        User user1 = new User("Cohen");
        User user2 = new User("John Smith");
        User user3 = new User("Cohen");

        assertEquals(0, user1.compareTo(user3)); // user1 == user3 (they have the same name)
        assertEquals(-1, user1.compareTo(user2)); // user1 != user2 (they have different names)
    }
}
