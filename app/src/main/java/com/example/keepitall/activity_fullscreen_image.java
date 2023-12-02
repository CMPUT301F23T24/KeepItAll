package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class activity_fullscreen_image extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_image);
        // Get the passed image
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String image = extras.getString("image");
            // Set the image
            ImageView imageView = findViewById(R.id.fullscreen_imageview);
            Glide.with(this).load(Uri.parse(image)).into(imageView);
        }
        // setup the back button
        findViewById(R.id.fullscreenimage_backbutton).setOnClickListener(v -> finish());
    }
}