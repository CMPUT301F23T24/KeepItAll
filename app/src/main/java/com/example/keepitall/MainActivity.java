package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;


public class MainActivity extends AppCompatActivity{

    // Public global Variables
    private KeepItAll keepItAll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Create the main KeepItAll Data class, which will
        // Hold the bulk of the class information

        launchLogin();
    }
    private void launchLogin(){
        Intent i = new Intent(MainActivity.this, loginActivity.class);
        startActivity(i);
    }




}