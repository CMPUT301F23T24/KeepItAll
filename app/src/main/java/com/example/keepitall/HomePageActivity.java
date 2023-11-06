package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
    Float totalValue;

    // test data (REMOVE THIS AFTER)
    Item testItem = new Item(new Date(), "Description Example", "Toyota", "Rav-4", 1234, (float)24.42, "Item1");
    Item testItem2 = new Item(new Date(), "Description Example", "Toyotar", "Rav-4", 1234, (float)24.42, "Item2");
    Item testItem3 = new Item(new Date(), "Description Example", "Toyotad", "Rav-4", 1234, (float)24.42, "Item3");
    Item testItem4 = new Item(new Date(), "Description Example", "Toyotab", "Rav-4", 1234, (float)24.42, "Item4");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        itemList.addItem(testItem);
        itemList.addItem(testItem2);
        itemList.addItem(testItem3);
        itemList.addItem(testItem4);

        totalValue = getTotal(itemList);
        TextView totalValueView = findViewById(R.id.totalValueText);
        totalValueView.setText(String.format("Total Estimated Value: $%.2f", totalValue));

        // Gets username
        Bundle extras = getIntent().getExtras();
        String userName = extras.getString("username");

        // sets username
        TextView usernameView = findViewById(R.id.nameText);
        usernameView.setText(userName);

        gridView = findViewById(R.id.gridView);
        HomePageAdapter homePageAdapter = new HomePageAdapter(this, itemList);
        gridView.setAdapter(homePageAdapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            if (deleteMode) {
                if (itemsToRemove.contains(itemList.getItem(position))) { // if already selected, unselect
                    Toast.makeText(this, "does this work", Toast.LENGTH_SHORT).show();
                    itemsToRemove.remove(itemList.getItem(position));
                    view.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    itemsToRemove.add(itemList.getItem(position));
                    view.setBackgroundColor(Color.LTGRAY); // change color if selected
                }
            }

            else {
                Intent intent = new Intent(getApplicationContext(), ViewItemActivity.class);
                intent.putExtra("item", itemList.getItem(position));
                intent.putExtra("image", R.drawable.app_icon);
                startActivity(intent);
            }
        });

        AppCompatButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomePageActivity.this, AddItemActivity.class);
            startActivity(intent);
        });

        // Go back to login screen if back button is pressed
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Delete an item
        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            if (!deleteMode) {
                deleteMode = true;
                Toast.makeText(HomePageActivity.this, "Select items to be deleted", Toast.LENGTH_SHORT).show();
                deleteButton.setBackgroundResource(R.drawable.gray_button);
            } else {
                // Delete selected items
                for (Item item : itemsToRemove) {
                    itemList.deleteItem(item);
                    totalValue -= item.getValue();
                }
                totalValueView.setText(String.format("Total Estimated Value: $%.2f", totalValue));
                homePageAdapter.notifyDataSetChanged(); // Refresh the adapter
                itemsToRemove.clear(); // Clear the selection
                deleteMode = false; // Exit delete mode
                deleteButton.setBackgroundResource(R.drawable.white_button);
            }
        });

        //TODO: sort by, filter by, search
    }

    private float getTotal(ItemManager itemList) {
        float total = 0;
        ArrayList<Item> allItems = itemList.getAllItems();
        for (Item item: allItems) {
            total += item.getValue();
        }
        return total;
    }
}
