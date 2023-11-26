package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;

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
        // Check for the correct password
        if(!userToLogin.getPassword().equals(password)){
            return;
        }
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
        // create an intent to launch the home page activity
        Intent i = new Intent(this, HomePageActivity.class);
        i.putExtra("username", username);
        // wait for 1.5 seconds before launching the activity
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

}