package com.example.keepitall;

import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog; // Import AlertDialog class

/**
 * Class used to for the ChangeLogoActivity
 * This activity allows the user to change the logo of the item
 */
public class ChangeLogoActivity extends AppCompatActivity {
    // creating a index for the users to choose the logo from
    private int  currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_ltem_logo);
        //created the functionality to go back to the previousPage
        Button backButton = findViewById(R.id.viewBackButton);
        backButton.setOnClickListener(view -> finish());
        //created the functionality to go back to the homePage
        Button homeButton = findViewById(R.id.homeButton);
        homeButton.setOnClickListener(view -> moveToActivity(AddItemActivity.class));

        //created the functionality to change the logo of the item
        Button changeButton = findViewById(R.id.changebutton);
        changeButton.setOnClickListener(view -> showLogoOptionsDialog());

        //created the functionality to traverse to the GalleryButton
        Button galleryButton = findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(view -> moveToActivity(ImageGalleryActivity.class));
    }

    /**
     * This function is used to move to the activity passed in the parameter
     * @param activityClass - the activity to move to
     */
    private void moveToActivity(Class<?> activityClass) {
        Intent intent = new Intent(ChangeLogoActivity.this, activityClass);
        startActivity(intent);
    }

    /**
     * This function is used to show the dialog box to the user to choose the logo
     */
    private void showLogoOptionsDialog() {
        // giving three options to appear in the Dialog
        String[] logoOptions = {"OPTION A", "OPTION B", "OPTION C "};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose the logo")
                .setItems(logoOptions, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int selectedlogo) {
                        // this is used to change the logo on the basis of the index chosen by the user
                        currentIndex = selectedlogo;
                        //updating the logo
                        updateLogoImage();
                    }
                });
        builder.create().show();
    }
    /**
     * This function is used to update the logo of the item
     */
    private void updateLogoImage() {
        ImageView imageView = findViewById(R.id.imageView);
        if (currentIndex == 0) {
            //need to have new icons rn using the ones in drawable
            imageView.setImageResource(R.drawable.save_icon);
        } else if (currentIndex == 1) {
            imageView.setImageResource(R.drawable.logout_icon);
        } else if (currentIndex == 2) {
            imageView.setImageResource(R.drawable.gallery_icon);
        }
    }
}
