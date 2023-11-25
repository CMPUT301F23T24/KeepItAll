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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * Class used to for the ImageGalleryActivity
 * This activity allows the user to view the images of the item
 * This activity is not fully implemented
 */
public class ImageGalleryActivity extends AppCompatActivity {
    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    private String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 3;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private RecyclerView recyclerView;
    private TextView recyclerViewText;
    private ArrayList<Uri> uri = new ArrayList<>();
    private ImageRecyclerAdapter imageRecyclerAdapter;
    private ImageView hiddenImage;
    private static final int Read_Permission = 1;
    private static final int PICK_IMAGE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_gallery);
        // Connect the buttons to their respective views
        Button backButton = findViewById(R.id.viewBackButton);
        Button cameraButton = findViewById(R.id.cameraButton);
        Button galleryButton = findViewById(R.id.galleryOpenButton);
        recyclerViewText = findViewById(R.id.totalPhotos);
        hiddenImage = findViewById(R.id.hiddenImage);
        recyclerView = findViewById(R.id.recyclerView_Gallery_Images);
        imageRecyclerAdapter = new ImageRecyclerAdapter(uri);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(imageRecyclerAdapter);
        // Set the click listeners for the buttons
        backButton.setOnClickListener(view -> finish());
        cameraButton.setOnClickListener(view -> OpenCamera());
        galleryButton.setOnClickListener(view -> OpenGallery());




        ///


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
        //pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                //.setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                //.build());
        if(ContextCompat.checkSelfPermission(ImageGalleryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ImageGalleryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
            Toast.makeText(ImageGalleryActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent();
        intent.setType("image/*");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2){
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check if the result comes from the correct activity
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            if(data.getClipData() != null){
                int x = data.getClipData().getItemCount();

                for(int i=0; i < x; i++){
                    uri.add(data.getClipData().getItemAt(i).getUri());
                }
                imageRecyclerAdapter.notifyDataSetChanged();
                recyclerViewText.setText("Total Photos: " + uri.size());
            } else if(data.getData() != null){
                Toast.makeText(getApplicationContext(), "Single image selected", Toast.LENGTH_SHORT).show();
                // get the URI of the image
                String imageURL = data.getData().getPath();
                uri.add(Uri.parse(imageURL));
            }
        }

        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            // save the value of the images to hiddenImage, which we will use to save to the gallery
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            hiddenImage.setImageBitmap(imageBitmap);

            SaveImageToGallery();
            // set the bitmap to the image view

        }

        imageRecyclerAdapter.notifyDataSetChanged();
        ///TODO: Change the itemLogo to the image that was selected (the first image in the list)
    }


    private void SaveImageToGallery(){
        // -- S
        Toast.makeText(getApplicationContext(), "Image saved to gallery", Toast.LENGTH_SHORT).show();
        Uri images;
        ContentResolver contentResolver = getContentResolver();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else{
            images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
        Uri uri = contentResolver.insert(images, contentValues);

        try {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) hiddenImage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Objects.requireNonNull(outputStream);
            Toast.makeText(getApplicationContext(), "Image saved to gallery", Toast.LENGTH_SHORT).show();

        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Image NOT saved to gallery!!", Toast.LENGTH_SHORT).show();
        }

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

        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }



    //// ------------------------ ////

}

