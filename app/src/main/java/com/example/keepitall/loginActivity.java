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
     * This method will check if the username and password are correct
     * and if they are, it will go to the next activity.
     */
    private void Login(){
        // Display a toast message if the username or password is empty
        if(usernameInput.getText().toString().isEmpty() || passwordInput.getText().toString().isEmpty()){
            Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show();
        }
        // Temporary code just to get to the next activity
        else{
            username = usernameInput.getText().toString();
            Toast.makeText(this, "Welcome " + username, Toast.LENGTH_SHORT).show();
            // wait 2 seconds before going to the next activity
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Go to the next activity
            Intent i = new Intent(this, HomePageActivity.class);
            i.putExtra("username", username);
            startActivity(i);
        }
    }
}