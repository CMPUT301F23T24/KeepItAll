package com.example.keepitall;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;

import org.junit.Test;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * Tests for the KeepItAll class
 * These tests dont include the database
 */
public class KeepItAllTest {

    @NonNull
    private KeepItAll createMocData(){
        // Creates a moc KeepItAll class, with a user in it
        KeepItAll Data = KeepItAll.getInstance();
        User user1 = new User("User One", "Password", "Email");
        User user2 = new User("User Two", "Password", "Email");
        Data.addUser_local(user1);
        Data.addUser_local(user2);
        return Data;
    }


    @Test
    public void testIsUsernameUnique(){
        KeepItAll Data = createMocData();
        assertEquals(true, Data.isUsernameUnique("User Three")); // checks for a unique username
        assertEquals(false, Data.isUsernameUnique("User One")); // checks for a non-unique username

    }
    @Test
    public void testAddUser(){
        KeepItAll Data = createMocData();
        assertEquals(2, Data.getUsers().size());
        User user3 = new User("User Three", "Password", "Email");
        Data.addUser_local(user3);
        assertEquals(3, Data.getUsers().size());
    }
    @Test
    public void testDeleteUser(){
        KeepItAll Data = createMocData();
        assertEquals(2, Data.getUsers().size());
        User tempUser = new User("Temp User", "Password", "Email");
        Data.addUser_local(tempUser);
        assertEquals(3, Data.getUsers().size());
        // Tests removing by class type
        Data.deleteUser(tempUser);
        assertEquals(2, Data.getUsers().size());
    }


}
