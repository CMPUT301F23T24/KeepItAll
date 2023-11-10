package com.example.keepitall;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class uses by the App to organize users and Items. This class is a singleton.
 * This class will link up to firestore to be able to add, delete, and edit items from the database,
 * by calling the methods in the ItemManager class.
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
     * down to fit our desired needs.
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
     * down to fit our desired needs.
     */
    public void retrieveUsers() {
        userCollection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot userDoc : queryDocumentSnapshots.getDocuments()) {
                            User user = userDoc.toObject(User.class);
                            // Retrieve the associated items for the user
                            userDoc.getReference().collection("items").get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot itemSnapshots) {
                                            // Create a new ItemManager for the user
                                            ItemManager itemManager = new ItemManager();
                                            // Loop through all the items and add them to the ItemManager
                                            for (DocumentSnapshot itemDoc : itemSnapshots.getDocuments()) {
                                                Item item = itemDoc.toObject(Item.class);
                                                // Fill up Item with data using the setter methods
                                                item.setName(itemDoc.getString("name"));
                                                item.setPurchaseDate(itemDoc.getDate("purchaseDate"));
                                                item.setDescription(itemDoc.getString("description"));
                                                item.setMake(itemDoc.getString("make"));
                                                item.setModel(itemDoc.getString("model"));
                                                item.setSerialNumber(itemDoc.getLong("serialNumber").intValue());
                                                item.setValue(itemDoc.getDouble("value").floatValue());
                                                ///TODO: Add tags to the item
                                                itemManager.addItem(item);
                                            }
                                            // Add the user to the list after all items are fetched
                                            user.setItemManager(itemManager);
                                            // update the local list of users
                                            users.add(user);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            // Handle the failure to retrieve the items
                                        }
                                    });
                        }
                    }
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
