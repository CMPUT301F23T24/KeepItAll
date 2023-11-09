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
        // add a user to the database (with a random name to ensure it is unique)
        assertTrue(KeepItAll.getInstance().isUsernameUnique("d91xka83ndiwoe"));
        KeepItAll.getInstance().addUser(new User("d91xka83ndiwoe", "Password", "Email"));
        // Now that it is added, it should no longer be unique
        assertFalse(KeepItAll.getInstance().isUsernameUnique("d91xka83ndiwoe"));

        // REMOVE USER FROM DATABASE AFTER TEST
        KeepItAll.getInstance().deleteUser(KeepItAll.getInstance().getUserByName("d91xka83ndiwoe"));
    }

    @Test
    public void testAddUser() {
        // add a user to the database (with a random name to ensure it is unique)
        assertTrue(KeepItAll.getInstance().isUsernameUnique("d91xka83ndiwoe"));
        // Get the size of the list of users before adding a new user
        int sizeBefore = KeepItAll.getInstance().getUsers().size();
        KeepItAll.getInstance().addUser(new User("d91xka83ndiwoe", "Password", "Email"));
        // Now that it is added, it should no longer be unique (since its already in the database)
        assertFalse(KeepItAll.getInstance().isUsernameUnique("d91xka83ndiwoe"));
        // Get the size of the list of users after adding a new user
        int sizeAfter = KeepItAll.getInstance().getUsers().size();
        // second confirmation, size should have increased by 1
        assertEquals(sizeBefore + 1, sizeAfter);

        // REMOVE USER FROM DATABASE AFTER TEST
        KeepItAll.getInstance().deleteUser(KeepItAll.getInstance().getUserByName("d91xka83ndiwoe"));

    }


    @Test
    public void testDeleteUser() {
        // add a user to the database (with a random name to ensure it is unique)
        assertTrue(KeepItAll.getInstance().isUsernameUnique("d91xka83ndiwoe"));
        KeepItAll.getInstance().addUser(new User("d91xka83ndiwoe", "Password", "Email"));
        // Now that it is added, it should no longer be unique
        assertFalse(KeepItAll.getInstance().isUsernameUnique("d91xka83ndiwoe"));
        // Get the size of the list of users before deleting a user
        int sizeBefore = KeepItAll.getInstance().getUsers().size();
        KeepItAll.getInstance().deleteUser(KeepItAll.getInstance().getUserByName("d91xka83ndiwoe"));
        // Get the size of the list of users after deleting a user
        int sizeAfter = KeepItAll.getInstance().getUsers().size();
        // second confirmation, size should have decreased by 1
        assertEquals(sizeBefore - 1, sizeAfter);

        // No need to remove user from database, it was already deleted
    }
    @Test
    public void TestGetUserByName(){
        // add a user to the database (with a random name to ensure it is unique)
        assertTrue(KeepItAll.getInstance().isUsernameUnique("d91xka83ndiwoe"));
        KeepItAll.getInstance().addUser(new User("d91xka83ndiwoe", "Password", "Email"));
        // now get user by name
        User user = KeepItAll.getInstance().getUserByName("d91xka83ndiwoe");
        // check if the user is not null
        assertTrue(user != null);

        // DELETE USER FROM DATABASE AFTER TEST
        KeepItAll.getInstance().deleteUser(KeepItAll.getInstance().getUserByName("d91xka83ndiwoe"));
    }
    /**
     * Retrieve Users from the database does not need to be tested, as it
     * entirely relies on Firebase, which is already tested through functional testing
     * The database information varries based on user accounts
     */

}