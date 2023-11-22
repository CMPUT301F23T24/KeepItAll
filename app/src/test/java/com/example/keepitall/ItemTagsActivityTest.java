package com.example.keepitall;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ItemTagsActivityTest {

    private ArrayList<Tag> tags;
    private ArrayList<Tag> tagsToDelete;

    @Before
    public void setUp() {
        tags = new ArrayList<>();
        tagsToDelete = new ArrayList<>();
    }

    @Test
    public void testAddTag() {
        // Simulate adding a tag
        Tag newTag = new Tag("TestTag");
        tags.add(newTag);

        // Assert that the tag has been added
        assertEquals(1, tags.size());
        assertTrue(tags.contains(newTag));
    }

    @Test
    public void testDeleteTag() {
        Tag tag1 = new Tag("Tag1");
        Tag tag2 = new Tag("Tag2");
        tags.add(tag1);
        tags.add(tag2);

        // Simulate selecting and deleting a tag
        tagsToDelete.add(tag1);
        tags.removeAll(tagsToDelete);

        // Assert that the tag has been deleted
        assertEquals(1, tags.size());
        assertFalse(tags.contains(tag1));
        assertTrue(tags.contains(tag2));
    }
}

