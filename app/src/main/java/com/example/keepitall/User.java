package com.example.keepitall;

import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Used for storing all of a users information, including their name and item manager
 */
public class User{

    // Primary Variables used and help by the User
    private String userName;
    private String password;
    private String emailAddress ;
    private ItemManager itemManager;
    private Item selectedItem;

    /**
     * Constructor for the User Object
     * @param userName
     */
    public User(String userName) {
        this.userName = userName;
        itemManager = new ItemManager();
    }
    public User(){
        itemManager = new ItemManager();
    }

    /**
     * Constructor for the User Object which takes in all of the variables
     * @param userName
     * @param password
     * @param emailAddress
     */
    public User(String userName, String password, String emailAddress) {
        this.userName = userName;
        this.password = password;
        this.emailAddress = emailAddress;
        this.itemManager = new ItemManager();
        //Toast.makeText(null, "User Created", Toast.LENGTH_SHORT).show();
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

    // Getters and Setters
    public String getUserName() {return userName;}
    public String getPassword() {return password;}
    public void setUserName(String userName) {this.userName = userName;}
    public Item getSelectedItem() {return selectedItem;}
    public ItemManager getItemManager() {return itemManager;}
    public void setItemManager(ItemManager itemManager) {this.itemManager = itemManager;}

    public String getEmailAddress() {
        return emailAddress;
    }
}
