package com.example.keepitall;

import android.content.Intent;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class AddItemActivity extends AppCompatActivity {
    private EditText purchaseDateText, makeText, modelText, valueText, descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
         /*
        purchaseDateText = findViewById(R.id.purchaseDateText);
        makeText = findViewById(R.id.makeText);
        modelText = findViewById(R.id.modelText);
        serialNumberText = findViewById(R.id.serialNumberText); // Make sure this ID exists in your layout
        valueText = findViewById(R.id.valueText);
        descriptionText = findViewById(R.id.descriptionText);

        AppCompatButton saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> {
            try {
                // Parse the user input
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                Date purchaseDate = dateFormat.parse(purchaseDateText.getText().toString());
                String make = makeText.getText().toString();
                String model = modelText.getText().toString();
                Integer serialNumber = Integer.parseInt(serialNumberText.getText().toString()); // Handle NumberFormatException appropriately
                Float value = Float.parseFloat(valueText.getText().toString()); // Handle NumberFormatException appropriately
                String description = descriptionText.getText().toString();

                // Create a new Item object
                Item newItem = new Item(purchaseDate, description, make, model, serialNumber, value);

                // Pass the Item back to HomePageActivity
                Intent returnIntent = new Intent();
                returnIntent.putExtra("newItem", newItem); // Make sure newItem is Serializable
                setResult(RESULT_OK, returnIntent);
                finish();
            } catch (ParseException e) {
                Toast.makeText(AddItemActivity.this, "Invalid date format", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(AddItemActivity.this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            }
        });
       */

        AppCompatButton backButton = findViewById(R.id.viewBackButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}