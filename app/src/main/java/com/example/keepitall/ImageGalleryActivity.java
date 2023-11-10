package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ImageGalleryActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_gallery);

        Button backButton = findViewById(R.id.viewBackButton);
        Button homeButton = findViewById(R.id.homeButton);

        backButton.setOnClickListener(view -> finish());

        homeButton.setOnClickListener(view -> {
            // Create an intent to navigate back to HomePageActivity
            Intent intent = new Intent(ImageGalleryActivity.this, AddItemActivity.class);
            startActivity(intent);
        });
    }

}
