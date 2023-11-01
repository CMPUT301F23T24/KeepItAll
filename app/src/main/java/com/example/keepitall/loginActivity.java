package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity {

    /**
     * The username of the user who currently logged in.
     * This will get passed to the next activity.
     */
    private String username;
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;

    private KeepItAll keepItAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // initialize variables
        usernameInput = findViewById(R.id.userName_Input);
        passwordInput = findViewById(R.id.password_Input);
        loginButton = findViewById(R.id.login_Button);

        // Login button listener that calls the login method
        loginButton.setOnClickListener(v -> Login());

    }

    /**
     * Various messages that will be displayed if the user
     * badly inputs their username or password.
     * Cases:
     * 1. Both username and password are empty
     * 2. Only the username is empty
     * 3. Only the password is empty
     * 4. The username or password is incorrect
     */
    private void LoginMessages(){
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
        // if neither are empty
        else if(!usernameInput.getText().toString().isEmpty() && !passwordInput.getText().toString().isEmpty()){
            Toast.makeText(this, "Welcome " + username, Toast.LENGTH_SHORT).show();
            // Go to the next activity
            waitForSeconds(1.5f);
            launchHomePage();
        }
        // Display a toast message if the username or password is incorrect
        ///TODO: make this check the database for the username and password
        else{
            Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * This method will check if the username and password are correct
     * and if they are, it will go to the next activity.
     */
    private void Login(){
        username = usernameInput.getText().toString();
        LoginMessages();
        // Temporary code just to get to the next activity
        //Toast.makeText(this, "Welcome " + username, Toast.LENGTH_SHORT).show();
        // Go to the next activity
        //waitForSeconds(1.5f);
        //launchHomePage();
    }
    /**
     * This method will launch the home page activity
     */
    private void launchHomePage(){
        Intent i = new Intent(this, HomePageActivity.class);
        i.putExtra("username", username);
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
     * This method will check if the username is valid for a few cases:
     * 1. the username is not taken
     * 2. the username is greater than 3 characters
     * 3. the username is less than 15 characters
     * @param username the username to check
     * @return true if the username is valid, false otherwise
     */
    private boolean checkValidUsername(String username){
        // Case 1: the username is not taken
        if(!keepItAll.isUsernameUnique(username)){
            Toast.makeText(this, "Username is already taken", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Case 2: the username is greater than 3 characters
        if(username.length() < 3){
            Toast.makeText(this, "Username must be greater than 3 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        // Case 3: the username is less than 15 characters
        if(username.length() > 15){
            Toast.makeText(this, "Username must be less than 15 characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        // If all of the tests 'fail', then its a valid username
        return true;
    }


    void createMocKeepItAll(){
        keepItAll = new KeepItAll();
        User user1 = new User("user1");
        User user2 = new User("user2");
        keepItAll.addUser(user1);
        keepItAll.addUser(user2);
    }
}