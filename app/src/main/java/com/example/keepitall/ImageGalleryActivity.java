package com.example.keepitall;

import static com.example.keepitall.HomePageActivity.REQUEST_IMAGE_CAPTURE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class used to for the ImageGalleryActivity
 * This activity allows the user to view the images of the item
 * This activity is not fully implemented
 */
public class ImageGalleryActivity extends AppCompatActivity {
    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    private String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_gallery);
        // Connect the buttons to their respective views
        Button backButton = findViewById(R.id.viewBackButton);
        Button homeButton = findViewById(R.id.homeButton);
        Button cameraButton = findViewById(R.id.cameraButton);
        Button galleryButton = findViewById(R.id.galleryOpenButton);
        // Set the click listeners for the buttons
        backButton.setOnClickListener(view -> finish());
        homeButton.setOnClickListener(view ->TraverseToHome());
        cameraButton.setOnClickListener(view -> OpenCamera());
        galleryButton.setOnClickListener(view -> OpenGallery());

        // Registers a photo picker activity launcher in single-select mode.
        pickMultipleMedia =
                registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
                    // Callback is invoked after the user selects media items or closes the
                    // photo picker.
                    if (!uris.isEmpty()) {
                        Log.d("PhotoPicker", "Number of items selected: " + uris.size());
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });


    }


    private void OpenGallery(){
        // Launch the photo picker and let the user choose only images.
        pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());


    }
    private void TraverseToHome(){
        // Create an intent to navigate back to HomePageActivity
        Intent intent = new Intent(ImageGalleryActivity.this, HomePageActivity.class);
        startActivity(intent);
    }
    private void OpenCamera(){
        // Check if the CAMERA permission is not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request the CAMERA permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        } else {
            // Permission already granted, proceed with your camera-related code
            startCameraIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            // Check if the CAMERA permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with your camera-related code
                startCameraIntent();

            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
                Toast.makeText(this, "Camera permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startCameraIntent(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else{
            Toast.makeText(this, "Camera not found.", Toast.LENGTH_SHORT).show();
        }
    }
}

