package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

/**
 * Class used to for the ImageGalleryActivity
 * This activity allows the user to view the images of the item
 * This activity is not fully implemented
 */
public class ImageGalleryActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_gallery);
        // Connect the buttons to their respective views
        Button backButton = findViewById(R.id.viewBackButton);
        Button homeButton = findViewById(R.id.homeButton);

        // Set the click listeners for the buttons
        backButton.setOnClickListener(view -> finish());
        homeButton.setOnClickListener(view -> {
            // Create an intent to navigate back to HomePageActivity
            Intent intent = new Intent(ImageGalleryActivity.this, AddItemActivity.class);
            startActivity(intent);
        });
    }
}
