package com.example.keepitall;

import org.junit.Test;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;


/**
 * Test class for RegisterAccount
 * This is located in the androidTest folder because it potentially talks with the database
 */
public class RegisterAccountTest {
    /**
     * This test will test the registration of a new account with no username
     */
    @Test
    public void testValidRegistration_Invalid_NoUsername() {
        RegisterAccount registerAccount = new RegisterAccount();
        registerAccount.setUsername("");
        registerAccount.setPassword("Password");
        registerAccount.setConfirmPassword("ConfirmPassword");
        registerAccount.setEmail("Email");
        // Check if each input is invalid (empty) to start
        // Check if the username is empty
        // Disable Toasts
        assertFalse(registerAccount.ValidRegistration());
    }

    /**
     * This test will test the registration of a new account with no password
     */
    @Test
    public void testValidRegistration_Invalid_NoPassword() {
        RegisterAccount registerAccount = new RegisterAccount();
        registerAccount.setUsername("Username");
        registerAccount.setPassword("");
        registerAccount.setConfirmPassword("ConfirmPassword");
        registerAccount.setEmail("Email");
        assertFalse(registerAccount.ValidRegistration());
    }
    /**
     * This test will test the registration of a new account with no confirm password
     */
    @Test
    public void testValidRegistration_Invalid_NoConfirmPassword() {
        RegisterAccount registerAccount = new RegisterAccount();
        registerAccount.setUsername("Username");
        registerAccount.setPassword("Password");
        registerAccount.setConfirmPassword("");
        registerAccount.setEmail("Email");
        assertFalse(registerAccount.ValidRegistration());
    }
    /**
     * This test will test the registration of a new account with no email
     */
    @Test
    public void testValidRegistration_Invalid_NoEmail() {
        RegisterAccount registerAccount = new RegisterAccount();
        registerAccount.setUsername("Username");
        registerAccount.setPassword("Password");
        registerAccount.setConfirmPassword("ConfirmPassword");
        registerAccount.setEmail("");
        assertFalse(registerAccount.ValidRegistration());
    }
    /**
     * This test will test the registration of a new account with passwords that do not match
     * but has all other fields filled out
     */
    @Test
    public void testValidRegistration_Invalid_PasswordsDoNotMatch() {
        RegisterAccount registerAccount = new RegisterAccount();
        registerAccount.setUsername("Username");
        registerAccount.setPassword("Password");
        registerAccount.setConfirmPassword("ConfirmPassword");
        registerAccount.setEmail("Email");
        assertFalse(registerAccount.ValidRegistration());
    }
    /**
     * This test will test the registration of a new account with all fields filled out
     */
    @Test
    public void testValidRegistration_Valid() {
        RegisterAccount registerAccount = new RegisterAccount();
        registerAccount.setUsername("sdwqscvfascad");
        registerAccount.setPassword("Password");
        registerAccount.setConfirmPassword("Password");
        registerAccount.setEmail("Email");
        assertTrue(registerAccount.ValidRegistration());

    }
}
