package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * Activity used for displaying the user's items (HomePage)
 * This page has multiple functionalities:
 *     1) Add an item
 *     2) Delete an item
 *     3) View an item's properties
 *     4) Sort items (soon)
 *     5) Filter items (soon)
 *     6) Search items (soon)
 *     7) Logout
 * Whenever an item is added or deleted, the total value of all items is updated
 * Whenever an item is added or deleted, the adapter is notified and the gridView is refreshed
 * Whenever an item is added or deleted, the itemManager is updated and synced with the database
 */
public class HomePageActivity extends AppCompatActivity {

    // Private variables
    private GridView gridView;
    private boolean deleteMode = false;
    private ItemManager userItemManager;
    private final ArrayList<Item> itemsToRemove = new ArrayList<>();
    private HomePageAdapter homePageAdapter;
    private TextView totalValueView;
    private Button deleteButton;
    private User user;
    private final KeepItAll keepItAll = KeepItAll.getInstance();
    private Button logoutButton;
    private Button pictureButton;
    static final int REQUEST_IMAGE_CAPTURE = 2; // For taking picture
    private ImageView TempImageView;
    private TextView usernameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        // Init the total value TextView
        totalValueView = findViewById(R.id.totalValueText);
        pictureButton = findViewById(R.id.take_picture_button);

        // Gets username (which is passed from the login screen as an extra)
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String userName = extras.getString("username");

            // Ensure that the username is not null or empty
            if (userName != null && !userName.isEmpty()) {
                // get User's itemManager
                user = keepItAll.getUserByName(userName);
                if (user != null) {
                    userItemManager = user.getItemManager();
                    updateTotalValue(); // Gets the total Value

                    // sets username
                    usernameView = findViewById(R.id.nameText);
                    usernameView.setText(userName);

                    // set the Adapter for gridView
                    gridView = findViewById(R.id.gridView);
                    homePageAdapter = new HomePageAdapter(this, userItemManager);
                    gridView.setAdapter(homePageAdapter);

                    // gridView onClickListener for deletion or view item properties
                    gridView.setOnItemClickListener((parent, view, position, id) -> {
                        gridViewClickEvent(view, position);
                    });

                    // Add item button
                    AppCompatButton addButton = findViewById(R.id.addButton);
                    addButton.setOnClickListener(v -> {
                        Intent intent = new Intent(HomePageActivity.this, AddItemActivity.class);
                        startActivityForResult(intent, 1);
                    });

                    // Take picture button
                    pictureButton.setOnClickListener(v -> takePictureClickEvent());

                    // Go back to login screen if back button is pressed
                    logoutButton = findViewById(R.id.logoutButton);
                    logoutButton.setOnClickListener(v -> finish());

                    // Delete an item
                    deleteButton = findViewById(R.id.deleteButton);
                    deleteButton.setOnClickListener(v -> {
                        deleteButtonClickEvent();
                    });

                    //TODO: sort by, filter by

                } else {
                    // Handle the case where the user is not found
                    Toast.makeText(this, "User not found.", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else {
                // Handle the case where username is not passed correctly
                Toast.makeText(this, "Username not received.", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            // Handle the case where extras is null
            Toast.makeText(this, "No data received.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    /**
     * Called when the user returns from AddItemActivity, it will check the result code
     * and add the new item to the item list if the result code is RESULT_OK
     * onSuccess, the item will be added to the database, and the homepage will be updated
     * @param requestCode - the code that was passed to startActivityForResult
     * @param resultCode - the result code that was passed back from AddItemActivity
     * @param data - the intent that was passed back from AddItemActivity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the new item from the result intent
                Item newItem = (Item) data.getSerializableExtra("newItem");
                userItemManager.addItem_DataSync(newItem, user);
                // Add the new item to your item list
                user.setItemManager(userItemManager);

                // Update total value
                updateTotalValue();
                // Notify the adapter that the data set has changed
                homePageAdapter.notifyDataSetChanged();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            //TempImageView.setImageBitmap(imageBitmap);
        }
    }

    /**
     * Click Listener for grid, either delete or view item property
     * @param view - the view that was clicked
     * @param position - the position of the view in the grid
     */
    private void gridViewClickEvent(View view, int position) {
        if (deleteMode) { // if delete
            if (itemsToRemove.contains(userItemManager.getItem(position))) { // if already selected, unselect
                itemsToRemove.remove(userItemManager.getItem(position));
                view.setBackgroundColor(Color.TRANSPARENT);
            } else {
                itemsToRemove.add(userItemManager.getItem(position));
                view.setBackgroundColor(Color.LTGRAY); // change color if selected
            }
        }

        else { // if user wants to view property item
            Intent intent = new Intent(getApplicationContext(), ViewItemActivity.class);
            intent.putExtra("item", userItemManager.getItem(position));
            intent.putExtra("image", R.drawable.app_icon);
            startActivity(intent);
        }
    }

    /**
     * Click Listener for deleteButton
     *      1) First click is to activate delete mode
     *      2) Second click is to delete selected items
     */
    private void deleteButtonClickEvent() {
        if (!deleteMode) {
            deleteMode = true;
            Toast.makeText(HomePageActivity.this, "Select items to be deleted", Toast.LENGTH_SHORT).show();
            deleteButton.setBackgroundResource(R.drawable.gray_button);
        } else {
            // Delete selected items
            for (Item item : itemsToRemove) {
                userItemManager.deleteItem(item);
                userItemManager.deleteItem_DataSync(item, user);
            }
            updateTotalValue();
            homePageAdapter.notifyDataSetChanged(); // Refresh the adapter
            itemsToRemove.clear(); // Clear the selection
            user.setItemManager(userItemManager);
            deleteMode = false; // Exit delete mode
            deleteButton.setBackgroundResource(R.drawable.white_button);
        }
    }

    private void searchClickEvent() {
        // TODO: search
    }

    private void sortClickEvent() {
        // TODO: sort
    }

    private void filterClickEvent() {
        // TODO: filter
    }

    /**
     * Gets total value of every item and display in homePage
     */
    private void updateTotalValue() {
        float totalValue = 0;
        ArrayList<Item> allItems = userItemManager.getAllItems();
        for (Item item: allItems) {
            totalValue += item.getValue();
        }
        totalValueView.setText(String.format("Total Value: $%.2f", totalValue));
    }

    private void takePictureClickEvent() {
        Toast.makeText(HomePageActivity.this, "Take picture", Toast.LENGTH_SHORT).show();
        // Opens the camera on the phone
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

    }
}