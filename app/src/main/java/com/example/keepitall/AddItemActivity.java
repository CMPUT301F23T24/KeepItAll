package com.example.keepitall;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

/**
 * Activity use for creating and adding a new item
 * Information is passed back to HomePageActivity where it will be updated in the database
 * and displayed on the screen
 */
public class AddItemActivity extends AppCompatActivity {
    // Private variables
    private EditText purchaseDateText, makeText, modelText, serialNumberText, valueText, descriptionText, itemNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Init the EditTexts to their respective views
        purchaseDateText = findViewById(R.id.purchaseDateText);
        makeText = findViewById(R.id.makeText);
        modelText = findViewById(R.id.modelText);
        serialNumberText = findViewById(R.id.serialNumberText);
        valueText = findViewById(R.id.valueText);
        descriptionText = findViewById(R.id.descriptionText);
        itemNameText = findViewById(R.id.itemNameText);
        AppCompatButton saveButton = findViewById(R.id.saveButton);
        AppCompatButton backButton = findViewById(R.id.viewBackButton);

        // Init click listeners
        // Saves all the information to a new Item object and passes it back to HomePageActivity
        saveButton.setOnClickListener(view -> saveButtonPressed());
        // Closes the activity (returns to HomePageActivity)
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button tagButton = findViewById(R.id.tagButton); //
        tagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start ItemTagsActivity
                Intent intent = new Intent(AddItemActivity.this, ItemTagsActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Called when the save button is pressed
     * Creates a new Item object and passes it back to HomePageActivity where it will be updated in the database
     * and displayed on the screen
     */
    private void saveButtonPressed(){
        Date purchaseDate = new Date();
        String make = makeText.getText().toString();
        String model = modelText.getText().toString();
        Integer serialNumber = 0;
        Float value = 0.0f;
        String description = descriptionText.getText().toString();
        String name = itemNameText.getText().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            purchaseDate = dateFormat.parse(purchaseDateText.getText().toString());
        } catch (ParseException e) {
        }

        try {
            serialNumber = Integer.parseInt(serialNumberText.getText().toString());
        } catch (NumberFormatException e) {
        }

        try {
            value = Float.parseFloat(valueText.getText().toString());
        } catch (NumberFormatException e) {
        }

        Item newItem = new Item(purchaseDate, description, make, model, serialNumber, value, name);

        // Pass the Item back to HomePageActivity
        Intent returnIntent = new Intent();
        returnIntent.putExtra("newItem", newItem);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}