package com.example.keepitall;

import android.nfc.Tag;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;



/**
 * Represents an item with various attributes and tags.
 */
public class Item implements Serializable {
    private Date purchaseDate;
    private String description;
    private String make;
    private String model;
    private Integer serialNumber;
    private Float value;
    private List<Tag> tags;
    private String name;


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
        this.name = name;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }
     public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
