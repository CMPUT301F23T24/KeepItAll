package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

public class ViewItemActivity extends AppCompatActivity {

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

        ImageView logoImage = findViewById(R.id.itemIcon);
        logoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(this, ChangeLogoActivity.class);
                // startActivity(intent);
                //
            }
        });

        // DONE: back functionality, home functionality
        // TODO: delete functionality, gallery functionality, tag functionality, edit functionalities
    }

    /**
     * Displays the clicked item's properties
     */
    public void displayText() {
        // Get item properties
        Item item = (Item) getIntent().getSerializableExtra("item");
        Date date = item.getPurchaseDate();
        String make = item.getMake();
        String model = item.getModel();
        Float value = item.getValue();
        String description = item.getDescription();

        // Get text views
        TextView dateView = findViewById(R.id.purchaseDateText);
        TextView makeView = findViewById(R.id.makeText);
        TextView modelView = findViewById(R.id.modelText);
        TextView valueView = findViewById(R.id.valueText);
        TextView descriptionView = findViewById(R.id.descriptionText);

        // Set text based on item properties
        dateView.setText("Date of Purchase: " + date.toString());
        makeView.setText("Item Make: " + make);
        modelView.setText("Item Model: " + model);
        valueView.setText("Estimated Value: " + value.toString());
        descriptionView.setText("Description: " + description);
    }
}