package com.example.keepitall;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Main class uses by the App to organize users and Items
 */
public class KeepItAll{
    private static volatile KeepItAll INSTANCE = null;
    private FirebaseFirestore Database = FirebaseFirestore.getInstance();
    private CollectionReference userCollection;
    private ArrayList<User> users;

    /**
     * Constructor used for the main data class
     * Holds an ArrayList of User classes
     */
    private KeepItAll() {
        this.users = new ArrayList<User>();
        this.userCollection = Database.collection("users");
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

            // Create a HashMap to store the users information
            // including the User object, and the itemManager
            HashMap<String, Object> data = new HashMap<>();
            data.put("User", user);
            userCollection.document(user.getUserName()).set(data);


            //HashMap<String, User> data = new HashMap<>();
            //data.put("User", user);
            //userCollection.document(user.getUserName()).set(data);
            //users.add(user);
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
     * @return the found User object, or returns null if the user is not found
     */
    public User getUserByName(String userName){
        return users.stream()
                .filter(u -> u.getUserName().equals(userName))
                .findFirst().orElse(null);
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
