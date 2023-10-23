package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // When the app is open, launch the login screen as a default
        launchLogin();
    }
    private void launchLogin(){
        Intent myIntent = new Intent(MainActivity.this, loginActivity.class);
        MainActivity.this.startActivity(myIntent);
    }




}