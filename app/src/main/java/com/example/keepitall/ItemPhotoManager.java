package com.example.keepitall;

import android.net.Uri;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemPhotoManager {
    private static ItemPhotoManager instance;
    private Map<String, List<Uri>> itemPhotosMap;


    public static synchronized ItemPhotoManager getInstance() {
        if (instance == null) {
            instance = new ItemPhotoManager();
        }
        return instance;
    }

    public ItemPhotoManager() {
        itemPhotosMap = new HashMap<>();
    }

    public List<Uri> getPhotosForItem(String itemId) {
        return itemPhotosMap.getOrDefault(itemId, new ArrayList<>());
    }

    public void addPhotoToItem(String itemId, Uri photo) {
        List<Uri> photos = itemPhotosMap.getOrDefault(itemId, new ArrayList<>());
        if (!photos.contains(photo)) {
            photos.add(photo);
        }
        itemPhotosMap.put(itemId, photos);
    }

    public void removePhotoFromItem(String itemId, Uri photo) {
        if (itemPhotosMap.containsKey(itemId)) {
            List<Uri> photos = itemPhotosMap.get(itemId);
            photos.remove(photo);
            if (photos.isEmpty()) {
                itemPhotosMap.remove(itemId);
            }
        }
    }
}
