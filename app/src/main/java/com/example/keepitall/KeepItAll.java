package com.example.keepitall;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
/**
 * Main class uses by the App to organize users and Items. This class is a singleton.
 * This class will link up to firestore to be able to add, delete, and edit items from the database,
 * by calling the methods in the ItemManager class.
 *
 * Functions with AI aid: The add User and retrieveUsers methods were created with the help of the
 * ChatGPT AI. The AI was used to help generate the base function of these methods. It was then
 * modified down to fit our desired needs. This was due to the reasoning of firestore being a new
 * technology for us to learn and implement. The AI was used to help us understand how it actually worked.
 * We went through many variations of the code, asking for prompts of how to properly go about adding and
 * retrieving custom classes from the database. Once the process was much better understood, we took
 * the code provided by the AI, and redesigned it to create the current Collection path and retrieval
 * system currently implemented. After that point, there was no further need to rsearch using this method,
 * as we had a much better understanding of how to properly implement firestore.
 */
public class KeepItAll{
    // Private variables
    private static volatile KeepItAll INSTANCE = null;
    private final FirebaseFirestore Database = FirebaseFirestore.getInstance();
    private final CollectionReference userCollection;
    private final ArrayList<User> users;

    /**
     * Constructor used for the main data class
     * Holds an ArrayList of User classes
     */
    private KeepItAll() {
        this.users = new ArrayList<User>();
        this.userCollection = Database.collection("users");
    }

    /**
     * Adds a user to the list of users if it isn't already present
     * Also adds the user to the database
     * ChatGPT AI - was used to help generate the base function of this method. It was then modified
     * down to fit our desired needs. View top of class for more information.
     * @param user - user to add
     */
    public void addUser(User user) {
        // Check if the user already exists in the local list (optional)
        if (!users.contains(user)) {
            // Add the User to Firestore with the document ID set to user.getUserName()
            userCollection.document(user.getUserName()).set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // User successfully saved
                            // We want to now save all the items
                            CollectionReference itemsCollection = userCollection.document(user.getUserName()).collection("items");
                            for (Item item : user.getItemManager().getAllItems()) {
                                itemsCollection.add(item);
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            // Handle the failure to add the User
                        }
                    });
            // Add the user to the local list (optional)
            users.add(user);
        }
    }

    /**
     * Removes a user from the list of users if it is present
     * Also removes the user from the database
     * @param user - user to remove
     */

    public void deleteUser(User user){
        // Remove the user from Firestore
        userCollection.document(user.getUserName()).delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // User successfully deleted
                        // We want to now delete all the items
                        CollectionReference itemsCollection = userCollection.document(user.getUserName()).collection("items");
                        for (Item item : user.getItemManager().getAllItems()) {
                            itemsCollection.document(item.getName()).delete();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Handle the failure to delete the User
                    }
                });
        // Remove the user from the local list (optional)
        users.remove(user);
    }

    /**
     * Retrieves all the users from the database and fills the local list
     * ChatGPT AI - was used to help generate the base function of this method. It was then modified
     * down to fit our desired needs.View top of class for more information.
     */
    public void retrieveUsers() {
        if (userCollection == null) {
            return;
        }
        userCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot userDoc : queryDocumentSnapshots.getDocuments()) {
                        User user = userDoc.toObject(User.class);
                        // Retrieve the associated items for the user
                        userDoc.getReference().collection("items").get()
                                .addOnSuccessListener(itemSnapshots -> {
                                    ItemManager itemManager = new ItemManager();
                                    for (DocumentSnapshot itemDoc : itemSnapshots.getDocuments()) {
                                        Item item = itemDoc.toObject(Item.class);
                                        // Fill up Item with data using the setter methods
                                        item.setName(itemDoc.getString("name"));
                                        item.setPurchaseDate(itemDoc.getDate("purchaseDate"));
                                        item.setDescription(itemDoc.getString("description"));
                                        item.setMake(itemDoc.getString("make"));
                                        item.setModel(itemDoc.getString("model"));
                                        // null check serial number
                                        if (itemDoc.getLong("serialNumber") != null) {
                                            item.setSerialNumber(itemDoc.getLong("serialNumber").intValue());
                                        }
                                        if (itemDoc.getDouble("value") != null) {
                                            item.setValue(itemDoc.getDouble("value").floatValue());
                                        }else {

                                            item.setValue(0.0f);
                                        }
                                        // Retrieve the photoList for each item
                                        retrievePhotoList(user, item, itemManager, itemDoc);
                                    }
                                    // Add the user to the list after all items are fetched
                                    user.setItemManager(itemManager);
                                    // update the local list of users
                                    users.add(user);
                                })
                                .addOnFailureListener(e -> {
                                    // Handle the failure to retrieve the items
                                });
                    }
                });
    }

    /**
     * Retrieves the photoList for an item from the database
     * @param user
     * @param item
     * @param itemManager
     * @param itemDoc
     */
    private void retrievePhotoList(User user, Item item, ItemManager itemManager, DocumentSnapshot itemDoc) {
        // Retrieve the photoList for the item
        itemDoc.getReference().collection("images").get()
                .addOnSuccessListener(imageSnapshots -> {
                    ArrayList<String> photoList = new ArrayList<>();
                    for (DocumentSnapshot imageDoc : imageSnapshots.getDocuments()) {
                        String uriPath = imageDoc.getString("path");
                        photoList.add(uriPath);
                        item.addPhoto(uriPath);
                    }
                    // Set the photoList for the item
                    //item.setPhotoList(photoList);
                    // Add the item to the ItemManager
                    itemManager.addItem(item);
                })
                .addOnFailureListener(e -> {
                    // Handle the failure to retrieve the photoList
                });
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

    /**
     * Getter function for the singleton instance
     * @return - the singleton instance of KeepItAll
     */
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
    /**
     * Gets the list of users
     * @return list of users
     */
    public ArrayList<User> getUsers(){
        return users;
    }
}
