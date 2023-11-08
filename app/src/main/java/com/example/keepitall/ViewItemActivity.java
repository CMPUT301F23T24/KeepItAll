package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class ViewItemActivity extends AppCompatActivity {
    private Item item;
    private static final int REQUEST_CODE_EDIT_ITEM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        // Retrieve the Item object sent from the previous activity
        item = (Item) getIntent().getSerializableExtra("item");
        if (item == null) {
            Toast.makeText(this, "Item data is not available.", Toast.LENGTH_LONG).show();
            finish();
        } else {
            displayText();
        }

        // Go back to HomePage when back or homeButton is clicked
        Button backButton = findViewById(R.id.viewBackButton);
        backButton.setOnClickListener(v -> finish());
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(v -> finish());

        // If item logo is clicked, go to change logo page
        ImageView logoImage = findViewById(R.id.itemIcon);
        logoImage.setOnClickListener(v -> changeActivity(ChangeLogoActivity.class));

        // If gallery button is clicked, go to item gallery page
        Button galleryButton = findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(v -> changeActivity(ImageGalleryActivity.class));

        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(v -> {
            if (item != null) {
                Intent intent = new Intent(ViewItemActivity.this, EditItemActivity.class);
                intent.putExtra("item", item);
                startActivityForResult(intent, REQUEST_CODE_EDIT_ITEM);
            } else {
                Toast.makeText(ViewItemActivity.this, "Item data is not available.", Toast.LENGTH_SHORT).show();
            }
        });

        // TODO: delete functionality, tag functionality, edit properties functionality
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_EDIT_ITEM && resultCode == RESULT_OK && data != null) {
            // Get the updated item from the result intent
            Item updatedItem = (Item) data.getSerializableExtra("updatedItem");
            if (updatedItem != null) {
                // Update the item
                this.item = updatedItem;
                displayText();
            }
        }
    }

    /**
     * Displays the clicked item's properties
     */
    private void displayText() {
        // Get item properties
        Item item = (Item) getIntent().getSerializableExtra("item");
        String name = item.getName();
        Date date = item.getPurchaseDate();
        String make = item.getMake();
        String model = item.getModel();
        Float value = item.getValue();
        Integer serialNum = item.getSerialNumber();
        String description = item.getDescription();

        // Get text views
        TextView nameView = findViewById(R.id.itemNameText);
        TextView dateView = findViewById(R.id.purchaseDateText);
        TextView makeView = findViewById(R.id.makeText);
        TextView modelView = findViewById(R.id.modelText);
        TextView valueView = findViewById(R.id.valueText);
        TextView serialNumView = findViewById(R.id.serialNumberText);
        TextView descriptionView = findViewById(R.id.descriptionText);

        // Set text based on item properties
        nameView.setText(name);
        dateView.setText("Date of Purchase: " + date.toString());
        makeView.setText("Item Make: " + make);
        modelView.setText("Item Model: " + model);
        valueView.setText("Estimated Value: " + value.toString());
        serialNumView.setText("Serial Number: " + serialNum.toString());
        descriptionView.setText("Description: " + description);
    }

    /**
     * Changes the activity based on clicked button
     * @param activity
     */
    private void changeActivity(Class activity) {
        Intent intent = new Intent(ViewItemActivity.this, activity);
        startActivity(intent);
    }
}