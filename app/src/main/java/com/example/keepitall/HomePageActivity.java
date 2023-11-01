package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class HomePageActivity extends AppCompatActivity {

    GridView gridView;

    // testing purposes
    Item testItem = new Item(new Date(), "Description Example", "Toyota", "Rav-4", 1234, (float)24.42);
    ArrayList<Item> itemList = new ArrayList<>();
    int[] images = {R.drawable.app_icon, R.drawable.app_icon, R.drawable.app_icon, R.drawable.app_icon, R.drawable.app_icon, R.drawable.app_icon, R.drawable.app_icon, R.drawable.app_icon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        for (int i=0; i<6; i++) {
            itemList.add(testItem);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // gets username
        Bundle extras = getIntent().getExtras();
        String userName = extras.getString("username");

        // sets username
        TextView usernameView = findViewById(R.id.nameText);
        usernameView.setText(userName);

        gridView = findViewById(R.id.gridView);
        HomePageAdapter homePageAdapter = new HomePageAdapter(this, itemList);
        gridView.setAdapter(homePageAdapter);


        gridView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(getApplicationContext(), ViewItemActivity.class);
            intent.putExtra("item", itemList.get(position));
            intent.putExtra("image", images[position]);
            startActivity(intent);
        });
    }
}