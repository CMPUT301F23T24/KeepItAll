package com.example.keepitall;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * This activity is used to display the item's tags
 * It is synced with Firebase, if tag is in Firebase, it will be displayed here
 */
public class ItemTagsActivity extends AppCompatActivity {

    private GridView gridView;
    private ArrayList<Tag> tags;
    private TagAdapter tagAdapter;
    private ArrayList<Tag> tagsToDelete = new ArrayList<>();
    private boolean deleteMode = false;
    private static final int ADD_TAG_REQUEST_CODE = 1;
    private String currentItemId;
    private TagsManager tagsManager;

    /**
     * onCreate is called when the activity is starting. It initializes the activity's layout,
     * sets up the GridView and its adapter, and configures button click listeners.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down, then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}. Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_tags);

        currentItemId = getIntent().getStringExtra("itemId");
        tagsManager = new TagsManager();
        currentItemId = getIntent().getStringExtra("itemId");

        tags = new ArrayList<>(tagsManager.getTagsForItem(currentItemId));

        gridView = findViewById(R.id.gridView);
        tagAdapter = new TagAdapter(this, tags);
        gridView.setAdapter(tagAdapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            gridViewItemClickEvent(view, position);
        }
        );

        // Add Tag button
        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            if (currentItemId != null) {
                Intent intent = new Intent(ItemTagsActivity.this, AddTagActivity.class);
                intent.putExtra("itemId", currentItemId);
                startActivityForResult(intent, ADD_TAG_REQUEST_CODE);
            } else {
                Toast.makeText(this, "Current Item ID is missing.", Toast.LENGTH_SHORT).show();
            }
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

    /**
     * Handles click events on items in the GridView. In delete mode, it allows
     * selection and deselection of tags for deletion. It changes the background color
     * of the selected tags for visual feedback.
     * @param view The view that was clicked in the GridView.
     * @param position The position of the view in the grid.
     */
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
        }
    }

    /**
     * Handles the click event of the delete button. It toggles the delete mode, allowing
     * users to select tags for deletion. Changes the button's appearance to indicate the
     * current mode (delete mode on/off).
     */
    private void deleteButtonClickEvent() {
        Button deleteButton = findViewById(R.id.deleteButton);
        if (!deleteMode) {
            deleteMode = true;
            Toast.makeText(ItemTagsActivity.this, "Select tags to delete", Toast.LENGTH_SHORT).show();
            deleteButton.setBackgroundColor(Color.GRAY);
        } else {
            // Delete selected tags
            tags.removeAll(tagsToDelete);
            tagAdapter.notifyDataSetChanged();
            tagsToDelete.clear();
            deleteMode = false;
            deleteButton.setBackgroundColor(Color.WHITE);
        }
    }

    /**
     * Loads tags from the Firestore database for the current item. Updates the tags list
     * and notifies the adapter of changes.
     */
    private void loadTags() {
        if (currentItemId == null) {
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("items").document(currentItemId).collection("tags")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        tags.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String tagName = document.getString("tagName");
                            tags.add(new Tag(tagName));
                        }
                        tagAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Error loading tags", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Called when the activity is resumed. It calls the loadTags method to refresh the
     * list of tags from the Firestore database.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadTags();
    }

    /**
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TAG_REQUEST_CODE && resultCode == RESULT_OK) {
            TagsManager tagsManager = TagsManager.getInstance();
            // Update tags list from TagsManager
            tags.clear();
            tags.addAll(tagsManager.getTagsForItem(currentItemId));
            tagAdapter.notifyDataSetChanged();
            loadTags();
        }
    }
}

