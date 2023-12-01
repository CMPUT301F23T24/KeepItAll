package com.example.keepitall;

import java.io.Serializable;
import java.util.Objects;

/**
 * Used for tags that would be used to categorize items
 * Not currently implemented
 */
public class Tag implements Serializable, Comparable<Tag> {
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

    /**
     * Purpose: Compares this tag object to another object for equality.
     * The comparison is based on the tag name.
     *
     * @param o The object to be compared with this tag.
     * @return true if the given object is also a tag and has the same tag name as this tag.
     *         false if the given object is null, not a tag, or has a different tag name.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(tagName, tag.tagName);
    }

    /**
     * Purpose: Provides a hash code value for a tag object, which is used in hash-based collections
     *          like hashmap.
     * @return An integer representing the hash code value of the Tag object. The hash code
     *         is generated based on the tagName field of the Tag object. If two Tag objects
     *         have the same tagName, they will have the same hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(tagName);
    }
}
