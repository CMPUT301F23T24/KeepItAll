package com.example.keepitall;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddTagActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tag);

        EditText tagNameEditText = findViewById(R.id.tagText);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            String tagName = tagNameEditText.getText().toString();
            Intent returnIntent = new Intent();
            returnIntent.putExtra("newTag", tagName);
            setResult(RESULT_OK, returnIntent);
            finish();
        });

        Button backButton = findViewById(R.id.viewBackButton);
        backButton.setOnClickListener(v -> finish());
    }
}
