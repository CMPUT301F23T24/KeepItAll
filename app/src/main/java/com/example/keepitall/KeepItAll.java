package com.example.keepitall;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    public void addUserX(User user){
        if(!users.contains(user)){
            // Add the User to Firestore
            userCollection.add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference userDocRef) {
                            // Get the auto-generated document ID for the User
                            String userId = userDocRef.getId();
                            // Once the User is added, add the associated ItemManager
                            ItemManager itemManager = user.getItemManager();
                            userDocRef.collection("itemManager").add(itemManager)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference itemManagerDocRef) {
                                            // Once the ItemManager is added, add associated items
                                            for (Item item : itemManager.getAllItems()) {
                                                itemManagerDocRef.collection("items").add(item);
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            // Handle the failure to add the ItemManager
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            // Handle the failure to add the User
                        }
                    });
            users.add(user);
        }
    }
    public void addUser(User user) {
        // Check if the user already exists in the local list (optional)
        if (!users.contains(user)) {
            // Add the User to Firestore
            userCollection.add(user)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference userDocRef) {
                            // Get the auto-generated document ID for the User
                            String userId = userDocRef.getId();

                            // Add the associated ItemManager to the user's document
                            ItemManager itemManager = user.getItemManager();
                            userDocRef.collection("itemManagers").document("itemManager").set(itemManager)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // ItemManager successfully saved
                                            // We want to now save all the items
                                            for (Item item : itemManager.getAllItems()) {
                                                userDocRef.collection("itemManagers").document("itemManager").collection("items").add(item);
                                            }
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(Exception e) {
                                            // Handle the failure to add the ItemManager
                                        }
                                    });
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


    public void retrieveUsers() {
        userCollection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot userDoc : queryDocumentSnapshots.getDocuments()) {
                            User user = userDoc.toObject(User.class);
                            String userId = userDoc.getId();

                            // Retrieve the associated ItemManager reference
                            DocumentReference itemManagerDocRef = userDoc.getReference().collection("itemManagers").document("itemManager");

                            itemManagerDocRef.get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot itemManagerDoc) {
                                            if (itemManagerDoc.exists()) {
                                                ItemManager itemManager = itemManagerDoc.toObject(ItemManager.class);

                                                // Set the ItemManager reference to the User
                                                user.setItemManager(itemManager);

                                                // Retrieve the associated items
                                                itemManagerDocRef.collection("items").get()
                                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onSuccess(QuerySnapshot itemSnapshots) {
                                                                List<Item> items = new ArrayList<>();

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
                                                                    ///TODO: One day the tags will be added
                                                                    itemManager.addItem(item);
                                                                }
                                                                // Add the user to the list after all data is fetched
                                                                users.add(user);
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(Exception e) {
                                                                // Handle the failure to retrieve the items
                                                            }
                                                        });
                                            } else {
                                                // Handle the case where the ItemManager document does not exist
                                            }
                                        }
                                    });
                        }
                    }
                });
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
