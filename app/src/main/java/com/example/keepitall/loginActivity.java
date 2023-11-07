package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * The class that holds the main functionality of the login page
 * We either login or register from this page
 * Registering will open a dialog box
 # login will send us to the home page
 */
public class loginActivity extends AppCompatActivity {

    // ---------- Global Variables ---------- //
    private String username; // The username of the user
    private String password; // The password of the user
    private EditText usernameInput; // The username input field
    private EditText passwordInput;  // The password input field
    private Button loginButton; // The login button
    private final KeepItAll keepItAll = KeepItAll.getInstance(); // access to the KeepItAll singleton
    private TextView signUpText; // the text the user clicks to register
    private FirebaseFirestore Database = FirebaseFirestore.getInstance(); // access to the database singleton
    private CollectionReference userCollection; // access to the user collection in the database
    // ---------- Global Variables ---------- //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // ---------- Init Variables ---------- //
        usernameInput = findViewById(R.id.userName_Input);
        passwordInput = findViewById(R.id.password_Input);
        loginButton = findViewById(R.id.login_Button);
        signUpText = findViewById(R.id.signUpText);
        userCollection = Database.collection("users");
        keepItAll.retrieveUsers(); // retrieve the users from the database (fireStore)
        // ---------- Init Variables ---------- //

        // ---------- Button Listeners ---------- //
        loginButton.setOnClickListener(v -> Login());
        signUpText.setOnClickListener(v -> openRegisterAccount());
        // ---------- Button Listeners ---------- //




    }
    /**
     * Various messages that will be displayed if the user
     * badly inputs their username or password.
     * Cases:
     * 1. Both username and password are empty
     * 2. Only the username is empty
     * 3. Only the password is empty
     * 4. The user does not exist
     * 5. The password is incorrect
     * 6. The login is successful
     * 7. Edge case
     */
    private void LoginMessages(){
        // Stores the logged in user to a local variable
        User userToLogin = keepItAll.getUserByName(username);
        // Display a toast message if the username or password is empty
        if(usernameInput.getText().toString().isEmpty() && passwordInput.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show();
        }
        // Display a toast message if only the username is empty
        else if(usernameInput.getText().toString().isEmpty() && !passwordInput.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
        }
        // Display a toast message if only the password is empty
        else if(!usernameInput.getText().toString().isEmpty() && passwordInput.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
        }
        // Display a toast if the user does not exist
        else if(userToLogin == null){
            Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show();
        }
        // Display a toast if the password is incorrect
        else if(!userToLogin.getPassword().equals(password)){
            Toast.makeText(this, "Username or password is incorrect", Toast.LENGTH_SHORT).show();
            return;
        }
        // Display a toast if the login is successful
        else if(userToLogin.getPassword().equals(password)){
            Toast.makeText(this, "Login Successful, Welcome " + username, Toast.LENGTH_LONG).show();
        }
        // Edge case
        else{
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * This method will check if the username and password are correct
     * and if they are, it will go to the next activity.
     */
    private void Login(){
        // Get the username and password from the input fields and store them in the global variables
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();
        LoginMessages();
        // Stores the logged in user to a local variable
        User userToLogin = keepItAll.getUserByName(username);
        if(userToLogin == null){ return;}
        // Launch the home page activity
        launchHomePage();
    }

    /**
     * This method will open the register account dialog box
     */
    private void openRegisterAccount(){
        RegisterAccount registerAccount = new RegisterAccount();
        registerAccount.show(getSupportFragmentManager(), "Register Account");
    }

    /**
     * This method will launch the home page activity
     * Called by the sign up button (text). Passes the username
     * to the next activity
     */
    private void launchHomePage(){
        Intent i = new Intent(this, HomePageActivity.class);
        i.putExtra("username", username);
        waitForSeconds(1.5f);
        startActivity(i);
    }

    /**
     * This method will wait for a certain amount of seconds
     * (pause the program)
     * @param seconds the amount of seconds to wait
     */
    private void waitForSeconds(float seconds){
        try {
            Thread.sleep((long)seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will create a mock KeepItAll object
     * with a few users and items. Mostly for testing purposes
     */
    private void createMocKeepItAll(){
        User dev = new User("dev", "pass", "email");
        keepItAll.addUser(dev);
        // Create a few items
        Item item1 = new Item(new Date(2002-20-02), "Test Description 1", "Test Location 1", "Test Category 1", 1231, 10.f, "Developer Item 1");
        // Add the items to the user
        dev.getItemManager().addItem(item1);
    }
}