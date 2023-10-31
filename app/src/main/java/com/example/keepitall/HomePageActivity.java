package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomePageActivity extends AppCompatActivity {

    GridView gridView;
    String[] items = {"item1", "item2", "item3", "item4", "item5", "item6", "item7", "item8"};
    int[] images = {R.drawable.app_icon, R.drawable.app_icon, R.drawable.app_icon, R.drawable.app_icon, R.drawable.app_icon, R.drawable.app_icon, R.drawable.app_icon, R.drawable.app_icon};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ///TODO: I just set this up so this will take in the userName
        //       from the login page that i am passing to you to use
        Bundle extras = getIntent().getExtras();
        String userName = extras.getString("username");
        ///TODO: The key for the username is "username"
        //       feel free to change how its stored, but this line gives you the current
        //       username of the user that is logged in


        gridView = findViewById(R.id.gridView);
        CustomAdapter customAdapter = new CustomAdapter();
        gridView.setAdapter(customAdapter);
    }

    private class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view1 = getLayoutInflater().inflate(R.layout.item_grid, null);

            TextView name = view1.findViewById(R.id.gridDataName);
            ImageView image = view1.findViewById(R.id.gridDataImage);
            name.setText(items[position]);
            image.setImageResource(images[position]);
            return view1;
        }
    }
}