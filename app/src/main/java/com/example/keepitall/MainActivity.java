package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Main Activity for the KeepItAll App, all this does is launch the login activity
 */
public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Launches the login activity to start the app
        Intent i = new Intent(MainActivity.this, loginActivity.class);
        startActivity(i);
    }
}