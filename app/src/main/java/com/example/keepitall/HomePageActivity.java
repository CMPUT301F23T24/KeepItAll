package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class HomePageActivity extends AppCompatActivity {

    GridView gridView;
    boolean deleteMode = false;
    ItemManager itemList = new ItemManager();
    ArrayList<Item> itemsToRemove = new ArrayList<>();
    HomePageAdapter homePageAdapter;
    TextView totalValueView;
    Button deleteButton;

    // test data (REMOVE THIS AFTER)
    Item testItem = new Item(new Date(), "Description Example", "Toyota", "Rav-4", 1234, (float)24.42, "Item1");
    Item testItem2 = new Item(new Date(), "Description Example", "Toyotar", "Rav-4", 1234, (float)24.42, "Item2");
    Item testItem3 = new Item(new Date(), "Description Example", "Toyotad", "Rav-4", 1234, (float)24.42, "Item3");
    Item testItem4 = new Item(new Date(), "Description Example", "Toyotab", "Rav-4", 1234, (float)24.42, "Item4");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        totalValueView = findViewById(R.id.totalValueText);

        itemList.addItem(testItem);
        itemList.addItem(testItem2);
        itemList.addItem(testItem3);
        itemList.addItem(testItem4);

        updateTotalValue(); // Gets the total Value

        // Gets username
        Bundle extras = getIntent().getExtras();
        String userName = extras.getString("username");

        // sets username
        TextView usernameView = findViewById(R.id.nameText);
        usernameView.setText(userName);

        // set the Adapter for gridView
        gridView = findViewById(R.id.gridView);
        homePageAdapter = new HomePageAdapter(this, itemList);
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

        // Go back to login screen if back button is pressed
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> finish());

        // Delete an item
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            deleteButtonClickEvent();
        });

        //TODO: sort by, filter by, search
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the new item from the result intent
                Item newItem = (Item) data.getSerializableExtra("newItem");
                // Add the new item to your item list
                itemList.addItem(newItem);
                //
                updateTotalValue();
                // Notify the adapter that the data set has changed
                homePageAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Click Listener for grid, either delete or view item property
     * @param view
     * @param position
     */
    private void gridViewClickEvent(View view, int position) {
        if (deleteMode) { // if delete
            if (itemsToRemove.contains(itemList.getItem(position))) { // if already selected, unselect
                itemsToRemove.remove(itemList.getItem(position));
                view.setBackgroundColor(Color.TRANSPARENT);
            } else {
                itemsToRemove.add(itemList.getItem(position));
                view.setBackgroundColor(Color.LTGRAY); // change color if selected
            }
        }

        else { // if user wants to view property item
            Intent intent = new Intent(getApplicationContext(), ViewItemActivity.class);
            intent.putExtra("item", itemList.getItem(position));
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
                itemList.deleteItem(item);
            }
            updateTotalValue();
            homePageAdapter.notifyDataSetChanged(); // Refresh the adapter
            itemsToRemove.clear(); // Clear the selection
            deleteMode = false; // Exit delete mode
            deleteButton.setBackgroundResource(R.drawable.white_button);
        }
    }

    private void searchClickEvent() {

    }
    /**
     * Gets total value of every item and display in homePage
     */
    private void updateTotalValue() {
        float totalValue = 0;
        ArrayList<Item> allItems = itemList.getAllItems();
        for (Item item: allItems) {
            totalValue += item.getValue();
        }
        totalValueView.setText(String.format("Total Value: $%.2f", totalValue));
    }
}
