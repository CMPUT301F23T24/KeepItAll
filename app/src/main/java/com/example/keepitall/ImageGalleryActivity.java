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
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
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

    // == Request Codes == //
    static final int REQUEST_IMAGE_CAPTURE = 3;
    private static final int Read_Permission = 1;
    private static final int PICK_IMAGE = 1;

    // -- Variables -- //
    // PhotoManager object used for TAKING photos
    private PhotoManager photoManager = new PhotoManager(this);
    // ActivityResultLauncher used for PICKING photos
    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    // TextView used to display the number of photos
    private TextView TotalPhotos;
    // ArrayList used to store the URIs of the images
    private ArrayList<Uri> uri = new ArrayList<>();
    // Adapter used to display the images in the grid view
    private PhotoGridAdapter photoGridAdapter;
    // The hidden image view used to save the image to the gallery (this is not displayed to user)
    private ImageView hiddenImage;
    // ID Identifier of the item (which is passed from the previous activity)
    private String stringIdentifier;
    //
    private ItemPhotoManager itemPhotoManager;
    // Grid View
    private GridView gridView;

    // Delete Variables
    private boolean deleteMode = false;
    private ArrayList<Uri> UriToDelete = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ---------- INITIALIZATION ---------- //

        // Set the content view
        setContentView(R.layout.image_gallery);

        // Initialize the UI elements
        TotalPhotos = findViewById(R.id.totalPhotos);
        hiddenImage = findViewById(R.id.hiddenImage);
        stringIdentifier = getIntent().getStringExtra("itemId");
        itemPhotoManager = new ItemPhotoManager();
        uri = new ArrayList<>(itemPhotoManager.getPhotosForItem(stringIdentifier));
        /// Gridview
        gridView = findViewById(R.id.imageGridView);
        photoGridAdapter = new PhotoGridAdapter(this, uri);
        gridView.setAdapter(photoGridAdapter);

        // Connect the buttons to their respective views
        Button backButton = findViewById(R.id.viewBackButton);
        Button cameraButton = findViewById(R.id.cameraButton);
        Button galleryButton = findViewById(R.id.galleryOpenButton);
        Button deleteButton = findViewById(R.id.ImageDeleteButton);

        // Connect Buttons to their respective Functions
        deleteButton.setOnClickListener(v -> deleteButtonClickEvent());
        backButton.setOnClickListener(view -> finish());
        cameraButton.setOnClickListener(view -> photoManager.TakePhoto());
        galleryButton.setOnClickListener(view -> OpenGallery());
        gridView.setOnItemClickListener((parent, view, position, id) -> gridViewItemClickEvent(view, position));

        // Register the photo picker activity
        registerPhotoPickerActivity();

        // ---------- POST INITIALIZATION ---------- //


        //LoadPhotos();
    }


    /**
     * Method called when the user clicks on the camera button, to take a photo
     * The photo is then saved to the user's device after being taken.
     * This method requires an ImageView "hiddenImage" to be present in the activity
     * @param resultCode the result code of the activity
     * @param requestCode the request code of the activity
     * @Intent data the intent of the activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check if the result comes from the correct activity
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {
                int x = data.getClipData().getItemCount();

                for (int i = 0; i < x; i++) {
                    Uri imageUri = data.getClipData().getItemAt(i).getUri();
                    ///TODO: add the uri to the list
                    //uri.add(imageUri);
                    ItemPhotoManager itemPhotoManager = ItemPhotoManager.getInstance();
                    itemPhotoManager.addPhotoToItem(stringIdentifier, imageUri);
                    ///TODO: add the uri to the database
                    SaveToDatabase(imageUri);

                }
                photoGridAdapter.notifyDataSetChanged();
                TotalPhotos.setText("Total Photos: " + uri.size());
            } else if (data.getData() != null) {
                Toast.makeText(getApplicationContext(), "Single image selected", Toast.LENGTH_SHORT).show();
                // get the URI of the image
                String imageURL = data.getData().getPath();
                uri.add(Uri.parse(imageURL));
            }

            ///TODO: Refresh the adapter + update local uri list
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // save the value of the images to hiddenImage, which we will use to save to the gallery
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            hiddenImage.setImageBitmap(imageBitmap);
            photoManager.SaveImageToGallery(hiddenImage);
        }

        photoGridAdapter.notifyDataSetChanged();
        ///TODO: Change the itemLogo to the image that was selected (the first image in the list)
        LoadPhotosTest();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //LoadPhotos();
    }

    /**
     * Method called when the user clicks on the gallery button, to select images from the gallery
     * to add to the items photo list
     */
    private void OpenGallery() {
        if (ContextCompat.checkSelfPermission(ImageGalleryActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ImageGalleryActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Permission);
            Toast.makeText(ImageGalleryActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
        Intent intent = new Intent();
        intent.setType("image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        }
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

    }

    /**
     * Method called when the user clicks on the delete button, to allow the user to delete images
     * (multiple images can be selected)
     */
    private void deleteButtonClickEvent() {
        // Get the delete button
        Button deleteButton = findViewById(R.id.ImageDeleteButton);
        // Toggle delete mode
        if (!deleteMode) {
            deleteMode = true;
            Toast.makeText(this, "Select Pictures to delete", Toast.LENGTH_SHORT).show();
            deleteButton.setBackgroundColor(Color.GRAY); // Change button color to indicate delete mode
        } else {
            // Delete selected Images
            for (Uri uriToDelete : UriToDelete) {
                ItemPhotoManager itemPhotoManager = ItemPhotoManager.getInstance();
                itemPhotoManager.removePhotoFromItem(stringIdentifier, uriToDelete);
            }
            ///TODO: remove the uri from the database

            photoGridAdapter.notifyDataSetChanged(); // Refresh the adapter
            UriToDelete.clear(); // Clear the selection
            deleteMode = false; // Exit delete mode
            deleteButton.setBackgroundColor(Color.WHITE); // Reset button color
            // Update the UI
            TotalPhotos.setText("Total Photos: " + uri.size());

            ///TODO: Refresh the adapter + update local uri list
            LoadPhotosTest();
        }
    }

    /**
     * Method called when the user clicks on an image in the grid view.
     * it will either allow the user to delete the image, or open the image in full screen
     * @param view the view that was clicked
     * @param position the position of the view in the grid view
     */
    private void gridViewItemClickEvent(View view, int position) {
        // Check if we are in delete mode
        if (deleteMode) {
            // get the uri of the selected image
            Uri selectedTag = uri.get(position);
            if (UriToDelete.contains(selectedTag)) {
                // remove the tag from the list
                UriToDelete.remove(selectedTag);
                view.setBackgroundColor(Color.TRANSPARENT);
            } else {
                // add the tag to the list
                UriToDelete.add(selectedTag);
                view.setBackgroundColor(Color.LTGRAY);
            }
        } else {
            // Open a new activity to view the image in full screen
            Intent intent = new Intent(this, activity_fullscreen_image.class);
            // Save the uri of the image to the intent
            Uri imageUri = uri.get(position);
            intent.putExtra("image", imageUri.toString());
            // Start the full screen image activity
            startActivity(intent);
        }
    }

    private void registerPhotoPickerActivity(){
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

    // -- Saving and Loading Photos -- //
    private void LoadPhotos() {
        if (stringIdentifier == null) {
            return;
        }
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("items").document(stringIdentifier).collection("photos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        uri.clear(); // Clear the existing list before adding new items
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Get the URL of the image
                            String photoUrl = document.getString("photo");
                            if (photoUrl != null) {
                                //TODO: Figure out how to gain permission to access the image
                                //uri.add(Uri.parse(photoUrl));
                            }
                        }
                        // Notify the adapter of the data change
                        photoGridAdapter.notifyDataSetChanged();
                        // Update the UI or perform additional tasks as needed
                        TotalPhotos.setText("Total Photos: " + uri.size());
                    } else {
                        // Handle failure
                    }
                });
    }
    private void SaveToDatabase(Uri uriToAdd) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference itemRef = db.collection("items").document(stringIdentifier);
        Map<String, Object> photoData = new HashMap<>();
        photoData.put("photo", uriToAdd.toString());
        itemRef.collection("photos").add(photoData);

    }

    private void LoadPhotosTest(){
        ItemPhotoManager itemPhotoManager = ItemPhotoManager.getInstance();
        // update the list of photos
        if(itemPhotoManager.getPhotosForItem(stringIdentifier) != null){
            uri.clear();
            uri.addAll(itemPhotoManager.getPhotosForItem(stringIdentifier));
        }
        // Notify the adapter of the data change
        photoGridAdapter.notifyDataSetChanged();
        // Update the UI of the total number of photos
        TotalPhotos.setText("Total Photos: " + uri.size());

    }
    // -- Work in Progress -- //


}