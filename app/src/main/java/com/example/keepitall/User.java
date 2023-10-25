package com.example.keepitall;

import java.util.ArrayList;

/**
 * Used for storing all of a users information, including their name and item manager
 */
public class User  implements Comparable<User>{

    // Primary Variables used and help by the User
    private String userName;
    private String password;
    private String emailAdress;
    //private ItemManager itemManager;
    private Item selectedItem;      

    /**
     * Constructor for the User Object
     * @param userName
     */
    public User(String userName) {
        this.userName = userName;
        //itemManager = new ItemManager();
    }

    /**
     * View the details of a given item, based on its name
     * @param itemName
     * @return
     */
    public Item viewItem(String itemName){
        // Get the desired item from the itemManager, based on the given name
        //selectedItem = ItemManager.getItem(itemName);
        // we can now do funcionality with the item we have selected
        return selectedItem;
    }

    /**
     * Get access to the arraylist of items from the ItemManager
     * @return
     */
    public ArrayList<Item> viewAllItems(){
        // Returns the list of all of the items within the ItemManager
        // this is temporary, as the main one will need to be attached to the UI
        return new ArrayList<Item>();
    }

    /**
     * Compares the Username of two different Users, returns 0 if they are equal
     * This is primarily used if we ever need to sort users
     * @param userToCompare
     * @return
     * 0 -> if they are equal
     * -1 -> if they are not
     */
    @Override public int compareTo(User userToCompare) {
        if(this.userName == userToCompare.getUserName()) {
            return 0;
        } else {
            return -1;
        }
    }

    // Getters and Setters
    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}
    public Item getSelectedItem() {return selectedItem;}
}
