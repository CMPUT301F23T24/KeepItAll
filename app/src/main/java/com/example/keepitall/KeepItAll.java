package com.example.keepitall;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Main class uses by the App to organize users and Items
 */
public class KeepItAll{
    private static volatile KeepItAll INSTANCE = null;
    private ArrayList<User> users;

    /**
     * Constructor used for the main data class
     * Holds an ArrayList of User classes
     */
    private KeepItAll() {
        this.users = new ArrayList<User>();
    }

    /**
     * Gets the list of users
     * @return list of users
     */
    public ArrayList<User> getUsers(){
        return users;
    }

    /**
     * Adds a user to the list of users if it isn't already present
     * @param user - user to add
     */
    public void addUser(User user){
        if(!users.contains(user)){
            users.add(user);
        }
    }
    /**
     * Removes a user from the list of users if it is already present
     * @param user - user to remove
     */
    public void removeUser(User user){
        users.remove(user);
    }
    /**
     * Removes a user from a list of users, if it is already present
     * @param username - the username of the user we wish to remove
     */
    public void removeUser(String username){
        users.removeIf(u -> u.getUserName().equals(username));
    }

    /**
     * Checks an input string against all active usernames, to see if it's available
     * @param userName - the username to check
     * @return true if the username is unique, false otherwise
     */
    public boolean isUsernameUnique(String userName){
        return users.stream().noneMatch(u -> u.getUserName().equals(userName));
    }

    /**
     * Searches our list of users for a specific user, based off a given name input
     * @param userName - the name of the user we are looking for
     * @return the found User object, or throws an exception if not found
     */
    public User getUserByName(String userName){
        return users.stream()
                .filter(u -> u.getUserName().equals(userName))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }
    // SMALL CHANGE
    // public static method to retrieve the singleton instance
    public static KeepItAll getInstance() {
        // Check if the instance is already created
        if(INSTANCE == null) {
            // synchronize the block to ensure only one thread can execute at a time
            synchronized (KeepItAll.class) {
                // check again if the instance is already created
                if (INSTANCE == null) {
                    // create the singleton instance
                    INSTANCE = new KeepItAll();
                }
            }
        }
        // return the singleton instance
        return INSTANCE;
    }
}
