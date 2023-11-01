package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class HomePageActivity extends AppCompatActivity {

    GridView gridView;
    boolean deleteMode = false;

    // test data (REMOVE THIS AFTER)
    Item testItem = new Item(new Date(), "Description Example", "Toyota", "Rav-4", 1234, (float)24.42);
    Item testItem2 = new Item(new Date(), "Description Example", "Toyotar", "Rav-4", 1234, (float)24.42);
    Item testItem3 = new Item(new Date(), "Description Example", "Toyotad", "Rav-4", 1234, (float)24.42);
    Item testItem4 = new Item(new Date(), "Description Example", "Toyotab", "Rav-4", 1234, (float)24.42);

    ArrayList<Item> itemList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        itemList.add(testItem);
        itemList.add(testItem2);
        itemList.add(testItem3);
        itemList.add(testItem4);

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
            if (deleteMode == true) {
                itemList.remove(itemList.get(position));
                homePageAdapter.notifyDataSetChanged();
                deleteMode = false;
            }

            else {
                Intent intent = new Intent(getApplicationContext(), ViewItemActivity.class);
                intent.putExtra("item", itemList.get(position));
                intent.putExtra("image", R.drawable.app_icon);
                startActivity(intent);
            }
        });

        AppCompatButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomePageActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        // Go back to login screen if back button is pressed
        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Delete an item
        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(v -> {
            deleteMode = true;
            Toast.makeText(HomePageActivity.this, "Select Item to be Deleted", Toast.LENGTH_SHORT).show();
        });

        //TODO: add functionality, sort by, filter by, search
    }

}
