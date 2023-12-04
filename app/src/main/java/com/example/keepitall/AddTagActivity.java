package com.example.keepitall;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity use for adding tags on an item
 * Saves it in firebase, and will be displayed later on
 */
public class AddTagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        EditText tagNameEditText = findViewById(R.id.tagText);
        String itemId = getIntent().getStringExtra("itemId");
        Log.d("AddTagActivity", "Received Item ID: " + itemId);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            String tagName = tagNameEditText.getText().toString();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference itemRef = db.collection("items").document(itemId);
            Map<String, Object> tagData = new HashMap<>();
            tagData.put("tagName", tagName);

            itemRef.collection("tags").add(tagData)
                    .addOnSuccessListener(documentReference -> {
                        // Handle successful tag addition
                        Intent returnIntent = new Intent();
                        setResult(RESULT_OK, returnIntent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                    });
        });

        Button backButton = findViewById(R.id.viewBackButton);
        backButton.setOnClickListener(v -> finish());
    }
}
