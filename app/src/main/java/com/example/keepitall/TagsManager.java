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

public class TagsManager {
    private static TagsManager instance;
    private Map<String, List<Tag>> itemTagsMap;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Set<String> appliedTags = new HashSet<>();

    public void markTagAsApplied(String tagName) {
        appliedTags.add(tagName);
    }

    public boolean isTagApplied(String tagName) {
        return appliedTags.contains(tagName);
    }


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

    public interface OnAllTagsFetchedListener {
        void onAllTagsFetched(List<String> tags);
    }

    /**
     * Fetch tags from Firestore and update the local tag list.
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

    public interface OnTagsFetchedListener {
        void onTagsFetched(List<Tag> tags);
    }

    public static synchronized TagsManager getInstance() {
        if (instance == null) {
            instance = new TagsManager();
        }
        return instance;
    }

    public TagsManager() {
        itemTagsMap = new HashMap<>();
    }

    public List<Tag> getTagsForItem(String itemId) {
        return itemTagsMap.getOrDefault(itemId, new ArrayList<>());
    }

    public void addTagToItem(String itemId, Tag tag) {
        List<Tag> tags = itemTagsMap.getOrDefault(itemId, new ArrayList<>());
        if (!tags.contains(tag)) {
            tags.add(tag);
        }
        itemTagsMap.put(itemId, tags);
    }

    public void removeTagFromItem(String itemId, Tag tag) {
        if (itemTagsMap.containsKey(itemId)) {
            List<Tag> tags = itemTagsMap.get(itemId);
            tags.remove(tag);
            if (tags.isEmpty()) {
                itemTagsMap.remove(itemId);
            }
        }
    }

    public List<String> getAllTags() {
        List<String> allTags = new ArrayList<>();
        for (String itemId : itemTagsMap.keySet()) {
            for (Tag tag : itemTagsMap.get(itemId)) {
                if (!allTags.contains(tag.getTagName())) {
                    allTags.add(tag.getTagName());
                }
            }
        }
        return allTags;
    }

}
