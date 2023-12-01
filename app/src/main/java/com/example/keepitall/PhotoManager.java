package com.example.keepitall;

import static android.content.ContentValues.TAG;
import static com.example.keepitall.ImageGalleryActivity.REQUEST_IMAGE_CAPTURE;
import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Class used to manage to ussage of photos
 * including a few cases, such as:
 * - taking a photo
 * - accessing the gallery (on the user's device)
 * - syncing with the cloud (firebase)
 */
public class PhotoManager {
    // --- Request codes --- //
    private static final int PERMISSION_REQUEST_CODE = 2;
    private Context context;
    private Activity activity;
    private KeepItAll keepItAll = KeepItAll.getInstance();
    private final FirebaseFirestore Database = FirebaseFirestore.getInstance();
    private final CollectionReference userCollection;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();


    /**
     * Constructor for the PhotoManager class, which holds:
     * - the context of the activity that is using the PhotoManager
     * - the activity that is using the PhotoManager
     * - the list of photos that are currently held by the manager (initially empty)
     * @param context the context of the activity that is using the PhotoManager
     */
    public PhotoManager(Context context) {
        this.context = context;
        this.activity = (Activity) context;
        this.userCollection = Database.collection("users");
    }

    /**
     * Method called when the user wants to take a photo
     * the photo is then saved to the user's device after being taken.
     * This method requires an ImageView "hiddenImage" to be present in the activity
     */
    public void TakePhoto() {
        // Check if the CAMERA permission is not granted
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Request the CAMERA permission
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
        } else {
            // Open the camera
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            activity.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Method called when the user wants to access the gallery
     * This method requires an ImageView "hiddenImage" to be present in the activity
     * @param hiddenImage the ImageView that will be used to pass the information of the image (invisible to the user)
     */
    public void SaveImageToGallery(ImageView hiddenImage){
        // Create a new ContentValues object and put the image's data into it
        Uri images;
        ContentResolver contentResolver = activity.getContentResolver();
        // Check if the WRITE_EXTERNAL_STORAGE permission is not granted
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            images = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        } else{
            images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }
        // Create a new ContentValues object and put the image's data into it
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, System.currentTimeMillis() + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/*");
        Uri uri = contentResolver.insert(images, contentValues);
        // Save the image to the gallery
        try {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) hiddenImage.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();

            OutputStream outputStream = contentResolver.openOutputStream(Objects.requireNonNull(uri));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Objects.requireNonNull(outputStream);
        }catch(Exception e){
            //pass
            // not added to gallery
        }
    }

    /**
     * Method called when the user wants to save an image to the database
     * it will access firestore and save the image to the user's database (specifically to the "images" collection of the item)
     * @param user the user that is currently logged in
     * @param item the item that the image is being saved to
     * @param uri the uri of the image that is being saved
     */
    public void SaveImageToDataBase(User user, Item item, Uri uri) {
        // Ensure that the user and item are not null
        if (user == null || item == null || uri == null) {
            // Handle the null cases
            return;
        }
        String path = "/images/" + user.getUserName() + "/" + item.getName() + "/" + uri.getLastPathSegment();
        StorageReference reference = storageReference.child(path);
        reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<com.google.firebase.storage.UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(com.google.firebase.storage.UploadTask.TaskSnapshot taskSnapshot) {
                // Save the path to the image in the database
                Map<String, Object> data = new HashMap<>();
                data.put("path", path);
                userCollection.document(user.getUserName())
                        .collection("items")
                        .document(item.getName())
                        .collection("images")
                        .add(data);
                Toast.makeText(context, "Image saved to Storage", Toast.LENGTH_SHORT).show();
                // we need to update the local item object. (that is located in the keepItAll object)
                // we need to get the item from the database again, and then update the local item object
                // this is just to ensure that the local item object is up to date with the database
                Item tempitem = keepItAll.getUserByName(user.getUserName()).getItemManager().getItemByName(item.getName());
                if (tempitem != null) {
                    tempitem.getPhotoList().add(path);
                }
            }
        }).addOnFailureListener(exception -> {
            // Handle any errors
            Log.e(TAG, "Error downloading image: " + exception.getMessage());
        });
    }

    /**
     * Deletes an image from the database at the given path (the path is stored in the item object)
     */
    public void DeleteImageFromDataBase(User user, Item item, Uri uriToDelete) {


        if (item == null || uriToDelete == null || user == null) {
            return;
        }

        // loop through all the images in the item object, and find the one that matches the uriToDelete
        // then delete it from the database
        // parse the uriToDelete to a string, and cut out everything except the file name
        String path = uriToDelete.toString();
        // parse at each %
        String[] parts = path.split("%");
        if(parts.length == 4){
            path = parts[3];
            // remove the first 2 characters
            path = path.substring(2);
            // only keep the first 10 characters
            path = path.substring(0, 10);
        }

        String FinalPath = "/images/" + user.getUserName() + "/" + item.getName() + "/" + path;
        StorageReference ref = storageReference.child(FinalPath);
        // delete the image from the database
        ref.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // delete the image from the database
                userCollection.document(user.getUserName())
                        .collection("items")
                        .document(item.getName())
                        .collection("images")
                        .whereEqualTo("path", FinalPath)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {
                                    document.getReference().delete();
                                }
                            }
                        });
                // delete the image from the local item object
                Item tempitem = keepItAll.getUserByName(user.getUserName()).getItemManager().getItemByName(item.getName());
                if (tempitem != null) {
                    tempitem.getPhotoList().remove(uriToDelete.toString());
                }
                Toast.makeText(context, "Image deleted from Storage", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(exception -> {
            // Handle any errors
            Log.e(TAG, "Error deleting image: " + exception.getMessage());
        });


    }
}

