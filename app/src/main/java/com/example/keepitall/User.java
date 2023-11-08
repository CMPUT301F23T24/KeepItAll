package com.example.keepitall;
/**
 * Used for storing all of a users information, including their name and item manager
 */
public class User{

    // Primary Variables used and help by the User
    private String userName;
    private String password;
    private String emailAddress ;
    private ItemManager itemManager;

    /**
     * Default Constructor for the User Object
     */
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
    // Getters and Setters
    public String getUserName() {return userName;}
    public String getPassword() {return password;}
    public String getEmailAddress() {return emailAddress;}
    public ItemManager getItemManager() {return itemManager;}
    public void setUserName(String userName) {this.userName = userName;}
    public void setItemManager(ItemManager itemManager) {this.itemManager = itemManager;}
}
