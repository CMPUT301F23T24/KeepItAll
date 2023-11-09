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


/* In the end, since the idea is very much synonymous to ItemManager, I decided to just
 make it pretty much one to one to ItemManager by copy paste and change some stuff
 Probably will not work right now.
 */

public class TagMenuActivity extends AppCompatActivity {

    GridView gridView;
    boolean deleteMode = false;

    // tests
    Tag tagTest1 = new Tag("Tag1");
    Tag testItem2 = new Tag("Tag2");
    Tag testItem3 = new Tag("Tag3");
    Tag testItem4 = new Tag("Tag4");

    ArrayList<Tag> tagList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tag_menu);

        tagList.add(tagTest1);
        tagList.add(testItem2);
        tagList.add(testItem3);
        tagList.add(testItem4);

        TagMenuAdapter homePageAdapter = new TagMenuAdapter(this, tagList);
        gridView.setAdapter(homePageAdapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            if (deleteMode == true) {
                tagList.remove(tagList.get(position));
                homePageAdapter.notifyDataSetChanged();
                deleteMode = false;
            }

            else {
                Intent intent = new Intent(getApplicationContext(), ViewItemActivity.class);
                // Android said to put (CharSequence) before tagList. Here is the reference to (CharSequence) just in case: https://docs.oracle.com/javase/8/docs/api/java/lang/CharSequence.html
                intent.putExtra("Tag", tagList.get(position).getTagName());
                intent.putExtra("image", R.drawable.app_icon);
                startActivity(intent);
            }
        });

        AppCompatButton addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TagMenuActivity.this, AddItemActivity.class);
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
            Toast.makeText(TagMenuActivity.this, "Select Tag to be Deleted", Toast.LENGTH_SHORT).show();
        });
    }

}
