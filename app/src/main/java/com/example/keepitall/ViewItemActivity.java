package com.example.keepitall;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Class used to for the ViewItemActivity
 * This activity allows the user to view the item's properties
 * This view will be where we can edit / view the item's properties
 */
public class ViewItemActivity extends AppCompatActivity {
    // Private variables
    private Item item;
    private static final int REQUEST_CODE_EDIT_ITEM = 1; // Request code for editing an item
    private String userName = "No User Name";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    private static final int REQUEST_CODE_CHANGE_LOGO = 2;

    private ActivityResultLauncher<Intent> changeLogoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        int selectedLogoId = data.getIntExtra("selectedLogo", -1);
                        if (selectedLogoId != -1) {
                            ImageView logoImageView = findViewById(R.id.itemIcon); // Make sure this ID matches your layout
                            logoImageView.setImageResource(selectedLogoId);
                        }
                    }
                }
            });

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


        Button updateInfoButton = findViewById(R.id.updateInfoButton);
        updateInfoButton.setOnClickListener(v -> updateHomePage());
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
        // Access the username
        Bundle extras = getIntent().getExtras();
        userName = extras.getString("username");
        galleryButton.setOnClickListener(v -> {
            if (item != null) {
                Intent intent = new Intent(ViewItemActivity.this, ImageGalleryActivity.class);
                intent.putExtra("itemId", item.getName());
                // pass in the Item object to the ImageGalleryActivity
                intent.putExtra("item", item);
                // pass in the user's name to the ImageGalleryActivity
                intent.putExtra("username", userName);
                startActivity(intent);
            } else {
                Toast.makeText(ViewItemActivity.this, "Item data is not available.", Toast.LENGTH_SHORT).show();
            }
        });

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

        // Set up OnClickListener for the tagButton
        Button tagButton = findViewById(R.id.tagButton);
        tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ItemTagsActivity
                Intent intent = new Intent(ViewItemActivity.this, ItemTagsActivity.class);
                intent.putExtra("itemId", item.getName());
                startActivity(intent);
            }
        });
        displayText();
    }

    /**
     * Called when the EditItemActivity finishes and returns to this activity
     * @param requestCode - the request code used to start the activity
     * @param resultCode - the result code returned from the activity
     * @param data - the intent returned from the activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle result from EditItemActivity
        if (requestCode == REQUEST_CODE_EDIT_ITEM && resultCode == RESULT_OK && data != null) {
            Item updatedItem = (Item) data.getSerializableExtra("updatedItem");
            if (updatedItem != null) {
                this.item = updatedItem;
                displayText();
            }
        }

        // Handle result from ChangeLogoActivity
        if (requestCode == REQUEST_CODE_CHANGE_LOGO && resultCode == RESULT_OK && data != null) {
            int selectedLogoId = data.getIntExtra("selectedLogo", -1);
            if (selectedLogoId != -1) {
                ImageView logoImageView = findViewById(R.id.itemIcon); // Replace with your actual ImageView ID
                logoImageView.setImageResource(selectedLogoId);
            }
        }
    }

    /**
     * Displays the clicked item's properties
     */
    private void displayText() {
        // Get item properties
        String name = item.getName();
        Date date = item.getPurchaseDate();
        String make = item.getMake();
        String model = item.getModel();
        Float value = item.getValue();
        Integer serialNum = item.getSerialNumber();
        String description = item.getDescription();
        String formattedDate = dateFormat.format(item.getPurchaseDate());

        // Get text views
        TextView nameView = findViewById(R.id.itemNameText);
        TextView dateView = findViewById(R.id.purchaseDateText);
        TextView makeView = findViewById(R.id.makeText);
        TextView modelView = findViewById(R.id.modelText);
        TextView valueView = findViewById(R.id.valueText);
        TextView serialNumView = findViewById(R.id.serialNumberText);
        TextView descriptionView = findViewById(R.id.descriptionText);

        // Set text based on item properties
        nameView.setText(item.getName());
        dateView.setText("Date of Purchase: " + formattedDate);
        makeView.setText("Item Make: " + make);
        modelView.setText("Item Model: " + model);
        valueView.setText("Estimated Value: " + value.toString());
        serialNumView.setText("Serial Number: " + serialNum.toString());
        descriptionView.setText("Description: " + description);
    }

    /**
     * Changes the activity based on clicked button
     * @param activityClass - the activity to change to
     */
    private void changeActivity(Class<?> activityClass) {
        Intent intent = new Intent(ViewItemActivity.this, activityClass);
        if (activityClass == ChangeLogoActivity.class) {
            changeLogoLauncher.launch(intent);
        } else {
            startActivity(intent);
        }
    }

    /**
     * Returns to homepage and updates it if item is updated
     */
    private void updateHomePage() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("updatedItem", item); // assuming 'item' contains the updated data
        setResult(Activity.RESULT_OK, returnIntent);
        finish(); // this will close the current activity and return to HomePage
    }

}