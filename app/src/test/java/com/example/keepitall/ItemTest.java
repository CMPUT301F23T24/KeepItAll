/**
 * Resources
 * https://www.javatpoint.com/java-util-date
 */

package com.example.keepitall;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Date;

public class ItemTest {

    @org.junit.Test
    public void testItemAttributes() {
        Date testDate = new Date();
        Item item = new Item(testDate, "TestItem", "TestMake", "TestModel", 12345, 10.5f, "Name");

        // Test attributes
        assertEquals(testDate, item.getPurchaseDate());
        assertEquals("TestItem", item.getDescription());
        assertEquals("TestMake", item.getMake());
        assertEquals("TestModel", item.getModel());
        assertEquals(Integer.valueOf(12345), item.getSerialNumber());
        assertEquals(Float.valueOf(10.5f), item.getValue());
        assertTrue(item.getTags().isEmpty());
    }

    @org.junit.Test
    public void testSortTags() {
        Date testDate = new Date();
        Item item = new Item(testDate, "TestItem", "TestMake", "TestModel", 12345, 10.5f, "Name");
        item.addTag(new Tag("a"));
        item.addTag(new Tag("d"));
        item.addTag(new Tag("c"));
        item.addTag(new Tag("b"));
        assertEquals("d", item.getTags().get(1).getTagName());
        item.sortTags();
        assertEquals("d", item.getTags().get(3).getTagName());

    }
}


