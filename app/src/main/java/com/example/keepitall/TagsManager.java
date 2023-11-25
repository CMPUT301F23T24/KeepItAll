package com.example.keepitall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagsManager {
    private static TagsManager instance;
    private Map<String, List<Tag>> itemTagsMap;

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
}
