package com.example.keepitall;


/**
 * Used for tags that would be used to categorize items
 */
public class Tag {
    private String tagName;


    /**
     * Constructor for Tag
     * @param tagName
     */
    public Tag(String tagName) {
        this.tagName = tagName;
    }

    /**
     * Changes tagName
     * @param tagName
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
