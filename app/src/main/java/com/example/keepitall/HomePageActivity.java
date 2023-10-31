package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class HomePageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ///TODO: I just set this up so this will take in the userName
        //       from the login page that i am passing to you to use
        Bundle extras = getIntent().getExtras();
        String userName = extras.getString("username");
        ///TODO: The key for the username is "username"
        //       feel free to change how its stored, but this line gives you the current
        //       username of the user that is logged in
    }
}