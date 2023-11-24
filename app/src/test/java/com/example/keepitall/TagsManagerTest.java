package com.example.keepitall;

import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class TagsManagerTest {

    private TagsManager tagsManager;
    private Tag testTag1;
    private Tag testTag2;
    private final String itemId = "item1";

    @Before
    public void setUp() {
        tagsManager = TagsManager.getInstance();
        testTag1 = new Tag("Tag1");
        testTag2 = new Tag("Tag2");
    }

    @Test
    public void testAddTagToItem() {
        tagsManager.addTagToItem(itemId, testTag1);
        List<Tag> tags = tagsManager.getTagsForItem(itemId);

        assertTrue("Tag should be added", tags.contains(testTag1));
    }

    @Test
    public void testGetTagsForItem() {
        tagsManager.addTagToItem(itemId, testTag1);
        tagsManager.addTagToItem(itemId, testTag2);
        List<Tag> tags = tagsManager.getTagsForItem(itemId);

        assertTrue("Tags list should contain testTag1", tags.contains(testTag1));
        assertTrue("Tags list should contain testTag2", tags.contains(testTag2));
        assertEquals("Tags list should have 2 tags", 2, tags.size());
    }

    @Test
    public void testRemoveTagFromItem() {
        tagsManager.addTagToItem(itemId, testTag1);
        tagsManager.addTagToItem(itemId, testTag2);
        tagsManager.removeTagFromItem(itemId, testTag1);
        List<Tag> tags = tagsManager.getTagsForItem(itemId);

        assertFalse("Tag1 should be removed", tags.contains(testTag1));
        assertTrue("Tag2 should remain", tags.contains(testTag2));
        assertEquals("Tags list should have 1 tag after removal", 1, tags.size());
    }

    @Test
    public void testRemoveTagFromEmptyList() {
        String nonExistentItemId = "nonExistentItem";
        tagsManager.removeTagFromItem(nonExistentItemId, testTag1);
        List<Tag> tags = tagsManager.getTagsForItem(nonExistentItemId);

        assertTrue("Tags list should be empty", tags.isEmpty());
    }

    @Test
    public void testSingletonInstance() {
        TagsManager anotherInstance = TagsManager.getInstance();
        assertSame("Instances should be the same", tagsManager, anotherInstance);
    }
}

