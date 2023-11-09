package com.example.keepitall;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class KeepItAllTest {
    /**
     * Test to ensure that the Usernames are unique
     */
    @Test
    public void testIsUsernameUnique() {
        KeepItAll.getInstance().addUser_local(new User("User One", "Password", "Email"));
        assertTrue(KeepItAll.getInstance().isUsernameUnique("User Two"));
        assertFalse(KeepItAll.getInstance().isUsernameUnique("User One"));
    }


    @Test
    public void testAddUser() {

    }


    @Test
    public void testDeleteUser() {

    }

    // Helper method to return a list of mock users
    private List<User> someListOfUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User("User One", "Password", "Email"));
        users.add(new User("User Two", "Password", "Email"));
        return users;
    }
}
