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

    private PhotoManager photoManager = new PhotoManager(this);

    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    static final int REQUEST_IMAGE_CAPTURE = 3;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private RecyclerView recyclerView;
    private TextView recyclerViewText;
    private ArrayList<Uri> uri = new ArrayList<>();
    private ImageRecyclerAdapter imageRecyclerAdapter;
    private ImageView hiddenImage;
    private static final int Read_Permission = 1;
    private static final int PICK_IMAGE = 1;
    private Item item;

    private User user;

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
        Bundle extras = getIntent().getExtras();
        String userName = extras.getString("username");
        user = KeepItAll.getInstance().getUserByName(userName);


        // Set the click listeners for the buttons
        backButton.setOnClickListener(view -> finish());
        cameraButton.setOnClickListener(view -> photoManager.TakePhoto());
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


        // Retrieve the Item object sent from the previous activity
        item = (Item) getIntent().getSerializableExtra("item");
        if (item == null) {
            Toast.makeText(this, "Item data is not available.", Toast.LENGTH_LONG).show();
        } else {
            displayUriList();
        }

    }


    private void OpenGallery(){
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

                for(int i=0; i < x; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    uri.add(imageUri);
                    // add the uri to the list
                    user.getItemManager().getItemByName(item.getName()).getPhotoList().add(imageUri);
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

            photoManager.SaveImageToGallery(hiddenImage);
        }

        imageRecyclerAdapter.notifyDataSetChanged();
        ///TODO: Change the itemLogo to the image that was selected (the first image in the list)
    }
    //// ------------------------ ////

    private void fillUriList(){

    }

    private void displayUriList(){
        // Loop through the list of photo URIs that are associated with the item
        if (item ==null || item.getPhotoList() == null) {
            Toast.makeText(this, "Item data is not available.", Toast.LENGTH_LONG).show();
            return;
        }
        for (Uri uri : user.getItemManager().getItemByName(item.getName()).getPhotoList()) {
            // Add the URI to the list
            this.uri.add(uri);
        }
        imageRecyclerAdapter.notifyDataSetChanged();
    }
}

