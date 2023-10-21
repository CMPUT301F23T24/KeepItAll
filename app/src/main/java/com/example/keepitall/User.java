package com.example.keepitall;

/**
 * Used for storing all of a users information, including their name and item manager
 */
public class User {

    // Primary Variables used and help by the User
    private String userName;
    private ItemManager itemManager;

    /**
     * Constructor for the User Object
     * @param userName
     */
    public User(String userName) {
        this.userName = userName;
        itemManager = new ItemManager();
    }
}
