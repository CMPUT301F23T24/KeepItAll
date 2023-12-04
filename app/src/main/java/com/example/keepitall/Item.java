package com.example.keepitall;
import android.net.Uri;
import android.nfc.Tag;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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
    private ArrayList<com.example.keepitall.Tag> tags;
    private String name;
    private ArrayList<String> photoList;
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
        this.tags = new ArrayList<>();  // Initialize tags list
        this.photoList = new ArrayList<>();
        this.tags = new ArrayList<>();  // Initialize tags list
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

    public void addPhoto(String photo) {
        photoList.add(photo);
    }
    public void removePhoto(String photo) {
        photoList.remove(photo);
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
        this.tags = new ArrayList<>();  // Initialize tags list
        this.photoList = new ArrayList<>(); // Initialize photo list
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
    public ArrayList<com.example.keepitall.Tag> getTags() {
        return tags;
    }
    // Photo
    public ArrayList<String> getPhotoList() { return photoList; }
    public void setPhotoList(ArrayList<Uri> photoList) { photoList = photoList; }

    /**
     * Gets the tagNames of all tags into strings
     * @return ArrayList: arrayList of all item's tag names
     */
    public ArrayList<String> getTagNames() {
        ArrayList<String> tagNames = new ArrayList<>();
        for (com.example.keepitall.Tag tag: tags) {
            tagNames.add(tag.getTagName().toLowerCase());
        }
        return tagNames;
    }

    /**
     * Sorts the item's tags
     */
    public void sortTags() {
        ArrayList<com.example.keepitall.Tag> tags = getTags();
        Collections.sort(tags);
        this.tags = tags;
    }

    /**
     * Gets the item's first (best) tag alphabetically.
     * @return string: item's best tagName
     */
    public String getItemFirstTag() {
        sortTags();
        if (tags.size() >= 1) {
            return tags.get(0).getTagName();
        }
        return "ZZZZ";
    }

    // For searching
    /**
     * Gets the keywords from strings
     * @param string: original string where the keywords originate from
     * @return ArrayList: arrayList of keywords of the given string
     */
    public ArrayList<String> getKeywords(String string) {
        String[] keywords  = string.toLowerCase().split("[,\\s*]");
        return new ArrayList<>(Arrays.asList(keywords));
    }

    /**
     * Checks if an item matches the query that the user searched
     * @param query: what the user searched in the searchbar
     * @return boolean: if an item matches the query or not
     */
    public boolean matchesQuery(String query) {
        // Checks for the entire query first
        if (name.toLowerCase().contains(query.toLowerCase()) ||
                make.toLowerCase().contains(query.toLowerCase()) ||
                tags.contains(new com.example.keepitall.Tag(query.toLowerCase()))) {

            return true;
        }

        // Gets keywords and see if it matches the query and item properties
        ArrayList<String> queryKeywords = getKeywords(query);
        ArrayList<String> descriptionKeywords = getKeywords(description);
        ArrayList<String> tagNames = getTagNames();
        for (String word: queryKeywords) {
            if (!(name.toLowerCase().contains(word.toLowerCase()) ||
                    make.toLowerCase().contains(word.toLowerCase()) ||
                    descriptionKeywords.contains(word.toLowerCase()) ||
                    tagNames.contains(word.toLowerCase()))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if two items are equal.
     * @param item
     * @return true if the items are equal, false otherwise.
     */
    public boolean isEqual(Item item) {
        return this.name.equals(item.name) &&
                this.purchaseDate.equals(item.purchaseDate) &&
                this.description.equals(item.description) &&
                this.make.equals(item.make) &&
                this.model.equals(item.model) &&
                this.serialNumber.equals(item.serialNumber) &&
                this.value.equals(item.value) &&
                this.tags.equals(item.tags);
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

