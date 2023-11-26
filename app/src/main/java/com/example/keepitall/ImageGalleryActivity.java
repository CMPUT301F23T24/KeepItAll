package com.example.keepitall;

import static android.app.appsearch.SetSchemaRequest.READ_EXTERNAL_STORAGE;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
    private PhotoGridAdapter photoGridAdapter;
    private ImageView hiddenImage;
    private static final int Read_Permission = 1;
    private static final int PICK_IMAGE = 1;
    private String stringIdentifier;
    private ItemPhotoManager itemPhotoManager;

    private GridView gridView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_gallery);
        // Connect the buttons to their respective views
        Button backButton = findViewById(R.id.viewBackButton);
        Button cameraButton = findViewById(R.id.cameraButton);
        Button galleryButton = findViewById(R.id.galleryOpenButton);
        recyclerViewText = findViewById(R.id.totalPhotos);
        hiddenImage = findViewById(R.id.hiddenImage);
        gridView = findViewById(R.id.imageGridView);
        stringIdentifier = getIntent().getStringExtra("itemId");
        itemPhotoManager = new ItemPhotoManager();
        uri = new ArrayList<>(itemPhotoManager.getPhotosForItem(stringIdentifier));
        photoGridAdapter = new PhotoGridAdapter(this, uri);
        gridView.setAdapter(photoGridAdapter);
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
        LoadPhotos();
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
                    ///TODO: add the uri to the list

                    ///TODO: add the uri to the database
                    //SaveToDatabase(imageUri);

                }
                photoGridAdapter.notifyDataSetChanged();
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

        photoGridAdapter.notifyDataSetChanged();
        ///TODO: Change the itemLogo to the image that was selected (the first image in the list)
    }
    //// ------------------------ ////

    /**
     * Saves a single image to the database
     */
    private void SaveToDatabase(Uri uriToAdd){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference itemRef = db.collection("items").document(stringIdentifier);
        Map<String, Uri> photoData = new HashMap<>();
        photoData.put("photo", uriToAdd);
        itemRef.collection("photos").add(photoData);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //LoadPhotos();
    }

    private void LoadPhotos(){
        if(stringIdentifier == null){
            return;
        }

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("items").document(stringIdentifier).collection("photos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        uri.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // get the URI of the image
                            String photoUri = document.getString("photo");
                            //uri.add(Uri.parse(photoUri));
                        }
                        photoGridAdapter.notifyDataSetChanged();
                    } else {
                        // Handle failure
                    }
                });
        photoGridAdapter.notifyDataSetChanged();
        recyclerViewText.setText("Total Photos: " + uri.size());
    }

}

