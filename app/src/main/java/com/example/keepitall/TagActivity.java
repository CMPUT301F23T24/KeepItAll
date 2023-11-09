package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
/*
Idea synonymous with Item, so I decided to just use the same code as the item code.
 */
public class TagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        displayText();

        // Go back to HomePage when back or homeButton is clicked
        Button backButton = findViewById(R.id.viewBackButton);
        backButton.setOnClickListener(v -> finish());
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> finish());


    }

    /**
     * Displays the clicked tag's properties
     */
    public void displayText() {
        // Get item properties

        Tag tag = (Tag) getIntent().getSerializableExtra("tag");
        String tagName = tag.getTagName();

        // Get text views
        TextView tagNameView = findViewById(R.id.tagName);



        // Set text based on item properties
        tagNameView.setText("Tag Name: " + tagName);



    }

    /*
     * Changes the activity based on clicked button
     * @param activity
     */

    public void changeActivity(Class activity) {
        Intent intent = new Intent(TagActivity.this, activity);
        startActivity(intent);


    }
}
