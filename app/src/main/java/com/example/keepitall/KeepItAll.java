package com.example.keepitall;

import java.util.ArrayList;

/**
 * Main class uses by the App to organize users and Items
 */
public class KeepItAll {
    private ArrayList<User> users;

    /**
     * Constructor used for the main data class
     * Holds an ArrayList of User classes
     */
    public KeepItAll() {
        this.users = new ArrayList<User>();
    }

    /**
     * Adds a user to the list of users if it isn't already present
     * @param user - remove
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
        if(users.contains(user)){
            users.remove(user);
        }
    }

    /**
     * Removes a user from a list of users, if it is already present
     * @param username - the username of the user we wish to remove
     */
    public void removeUser(String username){
        for (User user: users) {
            if(user.getUserName() == username){
                users.remove(user);
            }
        }
    }
    /**
     * Checks an input string against all active userNames, to see if its avaliable
     * (not currently the same username of another user)
     * @param userName
     * @return
     */
    public boolean isUsernameUnique(String userName){
        // Loop through the list of Users and make sure the user is
        // not already in the system
        for (User user: users) {
            // the users are the same, return false
            if(user.getUserName() == userName){
                return false;
            }
        }
        // otherwise, return true (its a unique userName)
        return true;
    }

    /**
     * Searches our list of users for a specific user, based off a given name input
     * @param userName - the name of the user we are looking for
     * @return
     */
    public User getUserByName(String userName){
        // Checks to see if that particular user is within our list
        // if so, we return the user. if not we throw an exception
        for (User user :users) {
            // Check for the name of the user
            if(user.getUserName() == userName){
                return user;
            }
        }
        // if we dont find a user, we throw an exception
        throw new RuntimeException();
    }


    /*
        Getters and Setters
     */
    public ArrayList<User> getUsers(){ return users;}

}
