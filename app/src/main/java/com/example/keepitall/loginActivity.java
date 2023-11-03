package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * The class that holds the main functionality of the login page
 * We either login or register from this page
 * Registering will open a dialog box
 # login will send us to the home page
 */
public class loginActivity extends AppCompatActivity {

    private String username;
    private String password;
    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private KeepItAll keepItAll = KeepItAll.getInstance();
    private TextView signUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // initialize variables
        usernameInput = findViewById(R.id.userName_Input);
        passwordInput = findViewById(R.id.password_Input);
        loginButton = findViewById(R.id.login_Button);
        signUpText = findViewById(R.id.signUpText);
        // Login button listener that calls the login method
        loginButton.setOnClickListener(v -> Login());
        signUpText.setOnClickListener(v -> openRegisterAccount());
        ///TODO: Make this part of the database
        createMocKeepItAll();


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
    }
    /**
     * This method will check if the username and password are correct
     * and if they are, it will go to the next activity.
     */
    private void Login(){
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();
        LoginMessages();
        User userToLogin = keepItAll.getUserByName(username);
        // Check if the User is null
        if(userToLogin == null){
            Toast.makeText(this, "Username or password is incorrect", Toast.LENGTH_SHORT).show();
            return;
        }
        if(userToLogin.getPassword().equals(password)){
            Toast.makeText(this, "Login Successful, Welcome " + username, Toast.LENGTH_LONG).show();
            waitForSeconds(1.5f);
            launchHomePage();
        }
        else{
            Toast.makeText(this, "Username or password is incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    private void openRegisterAccount(){
        RegisterAccount registerAccount = new RegisterAccount();
        registerAccount.show(getSupportFragmentManager(), "Register Account");
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

    private void createMocKeepItAll(){
        User dev = new User("dev", "pass", "email");
        keepItAll.addUser(dev);
    }
}