package com.example.keepitall;

import android.net.Uri;
import android.nfc.Tag;
import android.widget.Toast;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents an item with various attributes and tags.
 * Implements Serializable to allow for passing between activities.
 */
public class Item implements Serializable {
    // Private variables
    private Date purchaseDate;
    private String description;
    private String make;
    private String model;
    private Integer serialNumber;
    private Float value;
    private List<com.example.keepitall.Tag> tags;
    private String name;
    private ArrayList<Uri> PhotoList;
    private boolean isSelected = false;

    /**
     * Constructs an Item object with all attributes.
     * @param purchaseDate The date the item was purchased.
     * @param description A description of the item.
     * @param make The make of the item.
     * @param model The model of the item.
     * @param serialNumber The serial number of the item.
     * @param value The value of the item.
     */
    public Item(Date purchaseDate, String description, String make, String model, Integer serialNumber, Float value, String name) {
        this.purchaseDate = purchaseDate;
        this.description = description;
        this.make = make;
        this.model = model;
        this.serialNumber = serialNumber;
        this.value = value;
        this.tags = new ArrayList<com.example.keepitall.Tag>();  // Initialize tags list
        this.PhotoList = new ArrayList<Uri>();
        this.name = name;
    }

    // Tags //
    /**
     * Adds a tag to the item.
     * @param tag The tag to add.

     */
    public void addTag(com.example.keepitall.Tag tag) {
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    public void addPhoto(Uri photo) {
        PhotoList.add(photo);
    }

    /**
     * Removes a tag from the item.
     * @param tag - The tag to remove.

     */
    public void removeTag(Tag tag) {
        this.tags.remove(tag);
    }

    /**
     * Default constructor for Item object. with no parameters
     */
    public Item(){
        this.tags = new ArrayList<com.example.keepitall.Tag>();  // Initialize tags list
    }

    // Getters and setters //
    // Name
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    // Purchase date
    public Date getPurchaseDate() { return purchaseDate;}
    public void setPurchaseDate(Date purchaseDate) { this.purchaseDate = purchaseDate; }
    // Description
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    // Make
    public String getMake() { return make; }
    public void setMake(String make) { this.make = make; }
    // Model
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    // Serial number
    public Integer getSerialNumber() { return serialNumber; }
    public void setSerialNumber(Integer serialNumber) { this.serialNumber = serialNumber; }
    // Value
    public Float getValue() { return value; }
    public void setValue(Float value) { this.value = value;}
    // Tag
    public List<com.example.keepitall.Tag> getTags() { return tags; }
    // Sort the tags
    public void sortTags() {
        List<com.example.keepitall.Tag> tags = getTags();
        Collections.sort(tags);
        this.tags = tags;
    }
    // Get first tag
    public String getItemFirstTag() {
        sortTags();
        if (tags.size() >= 1) {
            return tags.get(0).getTagName();
        }
        return null;
    }
    // Get last tag
    public String getItemLastTag() {
        sortTags();
        if (!tags.isEmpty()) {
            return tags.get(tags.size() - 1).getTagName();
        }
        return null;
    }
    // Photo
    public ArrayList<Uri> getPhotoList() { return PhotoList; }

    // For searching
    public ArrayList<String> getKeywords(String string) {
        String[] keywords  = string.toLowerCase().split("[,\\s]");
        return new ArrayList<>(Arrays.asList(keywords));
    }

    public boolean matchesQuery(String query) {
        ArrayList<String> queryKeywords = getKeywords(query);
        ArrayList<String> descriptionKeywords = getKeywords(description);
        for (String word: queryKeywords) {
            if (!(name.toLowerCase().contains(word) ||
                    make.toLowerCase().contains(word) ||
                    descriptionKeywords.contains(word))) {
                return false;
            }
        }
        return true;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

