package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;


/**
 * Main Activity for the KeepItAll App, all this does is launch the login activity
 */
public class MainActivity extends AppCompatActivity{

    // Public global Variables
    private KeepItAll keepItAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Launches the login activity to start the app
        launchLogin();
    }

    /**
     * Launches the login activity at the start of the app
     */
    private void launchLogin(){
        Intent i = new Intent(MainActivity.this, loginActivity.class);
        startActivity(i);
    }




}