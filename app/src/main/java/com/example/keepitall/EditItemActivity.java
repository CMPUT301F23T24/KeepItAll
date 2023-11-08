package com.example.keepitall;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditItemActivity extends AppCompatActivity {
    private EditText itemNameText, purchaseDateText, makeText, modelText, serialNumberText, valueText, descriptionText;
    private Item item;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        // Retrieve the Item object sent from the ViewItemActivity
        item = (Item) getIntent().getSerializableExtra("item");

        itemNameText = findViewById(R.id.itemNameText);
        purchaseDateText = findViewById(R.id.purchaseDateText);
        makeText = findViewById(R.id.makeText);
        modelText = findViewById(R.id.modelText);
        serialNumberText = findViewById(R.id.serialNumberText);
        valueText = findViewById(R.id.valueText);
        descriptionText = findViewById(R.id.descriptionText);

        // Populate EditText fields with the Item properties
        itemNameText.setText(item.getName());
        purchaseDateText.setText(dateFormat.format(item.getPurchaseDate()));
        makeText.setText(item.getMake());
        modelText.setText(item.getModel());
        serialNumberText.setText(String.valueOf(item.getSerialNumber()));
        valueText.setText(String.valueOf(item.getValue()));
        descriptionText.setText(item.getDescription());

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(view -> {
            // Validate and collect data from fields
            String itemName = itemNameText.getText().toString();
            String make = makeText.getText().toString();
            String model = modelText.getText().toString();
            String description = descriptionText.getText().toString();
            String serialNumberString = serialNumberText.getText().toString();
            String valueString = valueText.getText().toString();
            Date purchaseDate;

            try {
                purchaseDate = dateFormat.parse(purchaseDateText.getText().toString());
            } catch (ParseException e) {
                Toast.makeText(com.example.keepitall.EditItemActivity.this,
                        "Invalid date format", Toast.LENGTH_SHORT).show();
                return;
            }

            int serialNumber;
            try {
                serialNumber = Integer.parseInt(serialNumberString);
            } catch (NumberFormatException e) {
                Toast.makeText(com.example.keepitall.EditItemActivity.this,
                        "Invalid serial number", Toast.LENGTH_SHORT).show();
                return;
            }

            float value;
            try {
                value = Float.parseFloat(valueString);
            } catch (NumberFormatException e) {
                Toast.makeText(com.example.keepitall.EditItemActivity.this,
                        "Invalid value", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update the item
            item.setName(itemName);
            item.setPurchaseDate(purchaseDate);
            item.setMake(make);
            item.setModel(model);
            item.setSerialNumber(serialNumber);
            item.setValue(value);
            item.setDescription(description);

            Intent returnIntent = new Intent();
            returnIntent.putExtra("updatedItem", item);
            setResult(RESULT_OK, returnIntent);
            finish();
        });

        Button backButton = findViewById(R.id.viewBackButton);
        backButton.setOnClickListener(view -> finish());
    }
}
