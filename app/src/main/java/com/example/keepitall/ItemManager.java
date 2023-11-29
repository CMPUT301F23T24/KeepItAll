package com.example.keepitall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import java.util.Date;
import java.util.Map;

/**
 * Class used to Manage all of a user's items.
 * Contains methods for adding, deleting, and editing items. both locally and in the database.
 * This class will link up to firestore to be able to add, delete, and edit items from the database.
 * and will be called from the HomePageActivity.
 */
public class ItemManager{
    // Private variables
    private final ArrayList<Item> itemList;
    private final FirebaseFirestore Database = FirebaseFirestore.getInstance();
    private final CollectionReference userCollection;

    /**
     * Constructor for the User Object
     * default constructor
     */
    public ItemManager() {
        this.itemList = new ArrayList<>();
        this.userCollection = Database.collection("users");
    }

    /**
     * Adds an item to the the item list and syncs it with the database (fireStore)
     * @param item - the item to be added
     * @param user - the user to add the item to
     */
    public void addItem_DataSync(Item item, User user) {
        // Ensure that the item and user are not null
        if (item == null || user == null || user.getUserName() == null) { return; }
        // Create a reference to the Firestore collection
        CollectionReference itemsCollection = userCollection.document(user.getUserName()).collection("items");
        // Use the item's name as the document ID
        DocumentReference itemDocument = itemsCollection.document(item.getName());
        // set the items
        itemDocument.set(item);
        itemList.add(item);
    }

    /**
     * Edits an item from the item list and syncs with the database (fireStore)
     * @param item - the item to be edited
     * @param user - the user to edit the item from
     */
    public void editItem_DataSync(Item item, User user) {
        // Ensure that the item and user are not null
        if (item == null || user == null || user.getUserName() == null) { return; }
        //TODO: Ensure the item is properly updated in the database

        CollectionReference itemsCollection = userCollection.document(user.getUserName()).collection("items");

        // First, query the item by name to get its Firestore document ID
        itemsCollection.whereEqualTo("name", item.getName())
                .get()
                .addOnCompleteListener(queryTask -> {
                    if (queryTask.isSuccessful()) {
                        for (QueryDocumentSnapshot document : queryTask.getResult()) {
                            // Get the Firestore document ID of the item
                            String itemId = document.getId();

                            // Update the item data
                            Map<String, Object> updatedData = new HashMap<>();
                            updatedData.put("purchaseDate", item.getPurchaseDate());
                            updatedData.put("description", item.getDescription());
                            updatedData.put("make", item.getMake());
                            updatedData.put("model", item.getModel());
                            updatedData.put("serialNumber", item.getSerialNumber());
                            updatedData.put("value", item.getValue());

                            // Update the item in Firestore
                            itemsCollection.document(itemId)
                                    .set(updatedData, SetOptions.merge()) // Merge to update only specified fields
                                    .addOnCompleteListener(updateTask -> {
                                        if (updateTask.isSuccessful()) {
                                            // Item has been successfully updated
                                            // You can add any additional handling here
                                        } else {
                                            // Handle the update failure if necessary
                                        }
                                    });
                        }
                    } else {
                        // Handle the query failure if necessary
                    }
                });
    }

    /**
     * Deletes an item from the item list and syncs with the database (fireStore)
     * @param item - the item to be deleted
     * @param user - the user to delete the item from
     */
    public void deleteItem_DataSync(Item item, User user) {
        if (item == null){
            return;
        }
        CollectionReference itemsCollection = userCollection.document(user.getUserName()).collection("items");
        itemsCollection.
                whereEqualTo("name", item.getName())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            itemsCollection.document(document.getId()).delete();
                        }
                    }
                });
    }

    /**
     * adds an item to the the item list (locally)
     * @param item to be added
     */
    public void addItem(Item item) {
        itemList.add(item);
    }

    /**
     * deletes  item to the the item list (locally)
     * @param item to be deleted
     */

    public void deleteItem(Item item) {
        itemList.remove(item);
    }
    /**
     *  used to edit the given properties of the item
     *
     * @param item           The item to be edited. Must not be null.
     * @param newPurchaseDate The new purchase date of the item.
     * @param newDescription  The new description of the item.
     * @param newMake         The new make of the item.
     * @param newModel        The new model of the item.
     * @param newSerialNumber The new serial number of the item.
     * @param newValue        The new value of the item.
     *
     */

    public void editItem(Item item, Date newPurchaseDate, String newDescription, String newMake, String newModel, Integer newSerialNumber, Float newValue) {
        item.setPurchaseDate(newPurchaseDate);
        item.setDescription(newDescription);
        item.setMake(newMake);
        item.setModel(newModel);
        item.setSerialNumber(newSerialNumber);
        item.setValue(newValue);
    }
    /**
     * the output is the  item you are searching for while comparing with name
     * @param position to be searched
     * @return item with the spec name or else null
     */
    public Item getItem(int position) {
        return itemList.get(position);
    }

    /**
     * the output is the list containg all the items
     * @return a new array list containng all the items by itemManager
     */
    public ArrayList<Item> getAllItems() {
        return new ArrayList<>(itemList);
    }

    /**
     * Checks if the item is in the list
     * @param position - the position of the item to be checked (index)
     * @return true if the item is in the list, false otherwise
     */
    public boolean itemInList(int position) {
        return itemList.contains(position);
    }

    /**
     *
     * @param sortBy: what the itemManager will be sorted by (tag, description, date, etc.)
     * @param sortOrder: order of sort, either descending or ascending
     */
    public void sortItems(String sortBy, String sortOrder) {
        Comparator<Item> comparator = null;

        switch (sortBy) {
            case "DATE":
                comparator = Comparator.comparing(Item::getPurchaseDate);
                break;
            case "DESCRIPTION":
                comparator = Comparator.comparing(Item::getDescription);
                break;
            case "MAKE":
                comparator = Comparator.comparing(Item::getMake);
                break;
            case "MODEL": // replace this with tag
                comparator = Comparator.comparing(Item::getModel);
                break;
            case "VALUE":
                comparator = Comparator.comparing(Item::getValue);
                break;
            default:
                throw new IllegalArgumentException("Invalid sortBy parameter");
        }

        if (sortOrder.equalsIgnoreCase("DESCENDING")) {
            comparator = comparator.reversed();
        }

        Collections.sort(this.itemList, comparator);
    }

    public Item getItemByName(String name) {
        for (Item item : itemList) {
            if (item.getName().equals(name)) {
                return item;
            }
        }
        return null;
    }
}