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
        Item item = new Item(testDate, "TestItem", "TestMake", "TestModel", 12345, 10.5f);

        // Test attributes
        assertEquals(testDate, item.getPurchaseDate());
        assertEquals("TestItem", item.getDescription());
        assertEquals("TestMake", item.getMake());
        assertEquals("TestModel", item.getModel());
        assertEquals(Integer.valueOf(12345), item.getSerialNumber());
        assertEquals(Float.valueOf(10.5f), item.getValue());
        assertTrue(item.getTags().isEmpty());
    }
}


