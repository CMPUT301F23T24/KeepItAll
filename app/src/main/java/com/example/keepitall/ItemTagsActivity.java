package com.example.keepitall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ItemTagsActivity extends AppCompatActivity {

    private GridView gridView;
    private ArrayList<Tag> tags;
    private TagAdapter tagAdapter;
    private ArrayList<Tag> tagsToDelete = new ArrayList<>();
    private boolean deleteMode = false;
    private static final int ADD_TAG_REQUEST_CODE = 1;
    private String currentItemId; // Current item identifier
    private TagsManager tagsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_tags);

        // Initialize TagsManager
        tagsManager = new TagsManager();

        // Get current item ID passed from previous activity
        currentItemId = getIntent().getStringExtra("itemId");

        // Initialize tags list for the current item
        tags = new ArrayList<>(tagsManager.getTagsForItem(currentItemId));

        // Initialize GridView and its adapter
        gridView = findViewById(R.id.gridView);
        tagAdapter = new TagAdapter(this, tags);
        gridView.setAdapter(tagAdapter);

        // GridView item click listener
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            gridViewItemClickEvent(view, position);
        });

        // Add Tag button
        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(ItemTagsActivity.this, AddTagActivity.class);
            intent.putExtra("itemId", currentItemId); // Pass the item identifier
            startActivityForResult(intent, ADD_TAG_REQUEST_CODE);
        });

        // Delete Tag button
        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            deleteButtonClickEvent();
        });

        // Back button
        Button backButton = findViewById(R.id.viewBackButton);
        backButton.setOnClickListener(v -> finish());
    }

    private void gridViewItemClickEvent(View view, int position) {
        if (deleteMode) {
            Tag selectedTag = tags.get(position);
            if (tagsToDelete.contains(selectedTag)) {
                tagsToDelete.remove(selectedTag);
                view.setBackgroundColor(Color.TRANSPARENT);
            } else {
                tagsToDelete.add(selectedTag);
                view.setBackgroundColor(Color.LTGRAY);
            }
        } else {
            // Handle non-delete mode item click if necessary
        }
    }

    private void deleteButtonClickEvent() {
        Button deleteButton = findViewById(R.id.deleteButton);
        if (!deleteMode) {
            deleteMode = true;
            Toast.makeText(ItemTagsActivity.this, "Select tags to delete", Toast.LENGTH_SHORT).show();
            deleteButton.setBackgroundColor(Color.GRAY); // Change button color to indicate delete mode
        } else {
            // Delete selected tags
            tags.removeAll(tagsToDelete);
            tagAdapter.notifyDataSetChanged(); // Refresh the adapter
            tagsToDelete.clear(); // Clear the selection
            deleteMode = false; // Exit delete mode
            deleteButton.setBackgroundColor(Color.WHITE); // Reset button color
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TAG_REQUEST_CODE && resultCode == RESULT_OK) {
            TagsManager tagsManager = TagsManager.getInstance();
            // Update tags list from TagsManager
            tags.clear();
            tags.addAll(tagsManager.getTagsForItem(currentItemId));
            tagAdapter.notifyDataSetChanged();
        }
    }
}

