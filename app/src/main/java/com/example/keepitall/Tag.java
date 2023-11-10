package com.example.keepitall;


/**
 * Used for tags that would be used to categorize items
 * Not currently implemented
 */
public class Tag implements Comparable<Tag> {
    // Private variables
    private String tagName;

    /**
     * Constructor for Tag
     * @param tagName
     */
    public Tag(String tagName) {
        this.tagName = tagName;
    }

    /**
     * Compares two tags to one another; tag1.compareTo(tag2)
     * Will be used for sorting
     * @param tag the object to be compared.
     * @return
     * 1 = if tag1 < tag2;
     * 0 = if tag1 == tag2
     * -1 = if tag1 > tag2
     */
    @Override
    public int compareTo(Tag tag) {
        return this.tagName.compareTo(tag.getTagName());
    }

    // getters and setters
    public String getTagName() {return tagName;}
    public void setTagName(String tagName) {this.tagName = tagName;}
}
