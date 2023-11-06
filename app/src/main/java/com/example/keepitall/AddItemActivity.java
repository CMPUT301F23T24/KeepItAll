package com.example.keepitall;

import android.content.Intent;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class AddItemActivity extends AppCompatActivity {
    private EditText purchaseDateText, makeText, modelText, serialNumberText, valueText, descriptionText, itemNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        purchaseDateText = findViewById(R.id.purchaseDateText);
        makeText = findViewById(R.id.makeText);
        modelText = findViewById(R.id.modelText);
        serialNumberText = findViewById(R.id.serialNumberText);
        valueText = findViewById(R.id.valueText);
        descriptionText = findViewById(R.id.descriptionText);
        itemNameText = findViewById(R.id.itemNameText);

        AppCompatButton saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> {
            // TEST
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
        });

        AppCompatButton backButton = findViewById(R.id.viewBackButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}