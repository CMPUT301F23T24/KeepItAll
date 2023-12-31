package com.example.keepitall;

import static android.app.appsearch.SetSchemaRequest.READ_EXTERNAL_STORAGE;
import static com.example.keepitall.HomePageActivity.REQUEST_IMAGE_CAPTURE;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.graphics.drawable.Drawable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    StorageReference storageRef2;
    // TextView used to display the number of photos
    private TextView TotalPhotos;
    // ArrayList used to store the URIs of the images
    private ArrayList<Uri> uri = new ArrayList<>();
    // Adapter used to display the images in the grid view
    private PhotoGridAdapter photoGridAdapter;
    // The hidden image view used to save the image to the gallery (this is not displayed to user)
    private ImageView hiddenImage;
    // Grid View
    private GridView gridView;

    // Delete Variables
    private boolean deleteMode = false;
    private ArrayList<Uri> UriToDelete = new ArrayList<>();

    // Private variables
    KeepItAll keepItAll = KeepItAll.getInstance();
    private User user;
    private Item item;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ---------- INITIALIZATION ---------- //
        // Set the content view
        setContentView(R.layout.image_gallery);

        // Initialize the UI elements
        TotalPhotos = findViewById(R.id.totalPhotos);
        hiddenImage = findViewById(R.id.hiddenImage);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String userName = extras.getString("username");
            user = keepItAll.getUserByName(userName);

        }
        item = (Item) getIntent().getSerializableExtra("item");
        connectItemToUser();
        /// Gridview
        gridView = findViewById(R.id.imageGridView);
        if(uri != null){
            ///TODO: This is where it breaks
            setUrisToAdapter(item);
            photoGridAdapter = new PhotoGridAdapter(this, uri);
            gridView.setAdapter(photoGridAdapter);
            ///TODO: This is where it breaks
        } else {
            uri = new ArrayList<>();
            item.setPhotoList(uri);
            photoGridAdapter = new PhotoGridAdapter(this, uri);
            gridView.setAdapter(photoGridAdapter);
        }
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

        // ---------- POST INITIALIZATION ---------- //
    }


    /**
     * Method that will take in the item passed in from the previous activity, and connect it to the user
     * due to the way that info is passed, we need to do this to ensure that the item variable we are changing
     * is the same as the one in the user's list of items, which is within the KeepItAll class (singleton)
     */
    private void connectItemToUser(){
        // find the Item object in the user's list of items that is equal to the item passed in from the previous activity
        if (item != null && user != null) {
            // loop through the user's list of items
            for (Item userItem : user.getItemManager().getAllItems()) {
                // check if the item is equal to the item passed in from the previous activity
                if (userItem.isEqual(item)) {
                    // if it is, set the item variable to the item in the user's list of items
                    item = userItem;
                    break;
                }
            }
        }
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
                    uri.add(imageUri);
                    Toast.makeText(getApplicationContext(), "Calling Save function", Toast.LENGTH_SHORT).show();
                    photoManager.SaveImageToDataBase(user, item, imageUri);
                }
                if (photoGridAdapter != null) {
                    photoGridAdapter.notifyDataSetChanged();
                }
                TotalPhotos.setText("Total Photos: " + uri.size());
            } else if (data.getData() != null) {
                //Toast.makeText(getApplicationContext(), "Single image selected", Toast.LENGTH_SHORT).show();
                // get the URI of the image
                //String imageURL = data.getData().getPath();
                //uri.add(Uri.parse(imageURL));
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // save the value of the images to hiddenImage, which we will use to save to the gallery
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            hiddenImage.setImageBitmap(imageBitmap);
            photoManager.SaveImageToGallery(hiddenImage);
            // get the URI of hiddenImage
            Uri imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, "Title", null));
            uri.add(imageUri); // adds the photo we just took a picture of to the list of photos
            // update the UI
            TotalPhotos.setText("Total Photos: " + uri.size());
        }

        if (photoGridAdapter != null) {
            photoGridAdapter.notifyDataSetChanged();
        }
        ///TODO: Change the itemLogo to the image that was selected (the first image in the list)
    }

    @Override
    protected void onResume() {
        super.onResume();
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
            uri.removeAll(UriToDelete); // Remove the selected images from the list
            // call the delete function in the photoManager
            for (Uri uriToDelete : UriToDelete) {
                photoManager.DeleteImageFromDataBase(user, item, uriToDelete);
            }
            if (photoGridAdapter != null) {
                photoGridAdapter.notifyDataSetChanged();
            }
            UriToDelete.clear(); // Clear the selection
            deleteMode = false; // Exit delete mode
            deleteButton.setBackgroundColor(Color.WHITE); // Reset button color
            // Update the UI
            TotalPhotos.setText("Total Photos: " + uri.size());

            ///TODO: Refresh the adapter + update local uri list
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



    /////// --------------------- ///////

    /**
     * This is where ill be doing all my testing to try to bypass the issue
     */

    /**
     * Loops through all of a Users, items, photos, and adds them to to a list
     * We are connecting to the database here
     * We are saving into a New ArrayList<Uri> uri
     * @param item the item that we are getting the photos from
     *
     */
    private void setUrisToAdapter(Item item){

        if (item == null) {
            return;
        }
        // clear the list
        uri.clear();
        // if the item has no photos, return
        if (item.getPhotoList() == null || item.getPhotoList().size() == 0) {
            return;
        }
        for (int i=0; i<item.getPhotoList().size();i++) {
            StorageReference ref = storageReference.child(item.getPhotoList().get(i));
            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uriToAdd) {
                    uri.add(uriToAdd);
                    //Toast.makeText(ImageGalleryActivity.this, "This has succeeded",Toast.LENGTH_SHORT).show();
                    // notifiy the adapter that the data has changed
                    if (photoGridAdapter != null) {
                        photoGridAdapter.notifyDataSetChanged();
                        // update the UI
                        TotalPhotos.setText("Total Photos: " + uri.size());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                    Toast.makeText(ImageGalleryActivity.this, "bad",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
