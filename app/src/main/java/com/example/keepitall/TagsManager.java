/**
 * Resource: https://www.w3schools.com/java/java_hashset.asp
 */
package com.example.keepitall;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is used to handle all functionalities regarding tags
 * It is connected to Firebase, and handles all data fetching from Firebase
 */
public class TagsManager {
    private static TagsManager instance;
    private Map<String, List<Tag>> itemTagsMap;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Set<String> appliedTags = new HashSet<>();

    /**
     * Marks a tag as applied by adding it to the set of applied tags.
     * This is useful for tracking which tags have already been used.
     *
     * @param tagName The name of the tag to mark as applied.
     */
    public void markTagAsApplied(String tagName) {
        appliedTags.add(tagName);
    }

    /**
     * Checks if a tag has been marked as applied.
     *
     * @param tagName The name of the tag to check.
     * @return true if the tag is in the set of applied tags, false otherwise.
     */
    public boolean isTagApplied(String tagName) {
        return appliedTags.contains(tagName);
    }

    /**
     * Fetches all tags from Firestore across all documents in the "tags" collection group.
     * This is useful for getting a comprehensive list of all tags used in the application.
     *
     * @param listener The listener that will be called with the list of all fetched tags.
     */
    public void fetchAllTags(OnAllTagsFetchedListener listener) {
        db.collectionGroup("tags").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Set<String> allTags = new HashSet<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String tagName = document.getString("tagName");
                    allTags.add(tagName);
                }
                listener.onAllTagsFetched(new ArrayList<>(allTags));
            } else {
                listener.onAllTagsFetched(new ArrayList<>()); // Return an empty list in case of failure
            }
        });
    }

    /**
     * Interface for a callback to be invoked when all tags are fetched from Firestore.
     */
    public interface OnAllTagsFetchedListener {
        void onAllTagsFetched(List<String> tags);
    }

    /**
     * Fetches tags from Firestore for a specific item and updates the local tag list.
     * This is used to retrieve the tags associated with a particular item in the "items" collection.
     *
     * @param itemId The ID of the item for which to fetch tags.
     * @param listener The listener that will be called with the list of fetched tags for the item.
     */
    public void fetchTagsFromFirestore(String itemId, OnTagsFetchedListener listener) {
        db.collection("items").document(itemId).collection("tags")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Tag> fetchedTags = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String tagName = document.getString("tagName");
                            fetchedTags.add(new Tag(tagName));
                        }
                        itemTagsMap.put(itemId, fetchedTags);
                        listener.onTagsFetched(fetchedTags);
                    } else {
                        listener.onTagsFetched(new ArrayList<>()); // Return an empty list in case of failure
                    }
                });
    }

    /**
     * Interface for a callback to be invoked when tags for a specific item are fetched from Firestore.
     */
    public interface OnTagsFetchedListener {
        void onTagsFetched(List<Tag> tags);
    }

    /**
     * Gets the singleton instance of the TagsManager, creating it if it doesn't exist.
     * This ensures that only one instance of the TagsManager exists throughout the application.
     *
     * @return The singleton instance of the TagsManager.
     */
    public static synchronized TagsManager getInstance() {
        if (instance == null) {
            instance = new TagsManager();
        }
        return instance;
    }

    /**
     * Constructor for TagsManager. Initializes the itemTagsMap to keep track of tags for each item.
     */
    public TagsManager() {
        itemTagsMap = new HashMap<>();
    }

    /**
     * Gets the list of tags for a specific item, if they exist.
     * This is used to retrieve the tags that are associated with an item.
     *
     * @param itemId The ID of the item for which to get tags.
     * @return The list of tags associated with the specified item.
     */
    public List<Tag> getTagsForItem(String itemId) {
        return itemTagsMap.getOrDefault(itemId, new ArrayList<>());
    }

    /**
     * Adds a tag to the list of tags for a specific item.
     * This is used to associate a new tag with an item.
     *
     * @param itemId The ID of the item to which the tag should be added.
     * @param tag The tag to add to the item.
     */
    public void addTagToItem(String itemId, Tag tag) {
        List<Tag> tags = itemTagsMap.getOrDefault(itemId, new ArrayList<>());
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
        itemTagsMap.put(itemId, tags);
    }

    /**
     * Removes a tag from the list of tags for a specific item.
     * This is used to disassociate a tag from an item.
     *
     * @param itemId The ID of the item from which the tag should be removed.
     * @param tag The tag to remove from the item.
     */
    public void removeTagFromItem(String itemId, Tag tag) {
        if (itemTagsMap.containsKey(itemId)) {
            List<Tag> tags = itemTagsMap.get(itemId);
            tags.remove(tag);
            if (tags.isEmpty()) {
                itemTagsMap.remove(itemId);
            }
        }
    }
}
