package com.example.keepitall;

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
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.ImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.OutputStream;
import java.util.ArrayList;
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
}

