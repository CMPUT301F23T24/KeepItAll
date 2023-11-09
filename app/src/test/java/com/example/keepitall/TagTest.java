package com.example.keepitall;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TagTest {
    @Test
    public void testCompareTo() {
        Tag tag1 = new Tag("Food");
        Tag tag2 = new Tag("Electronic");
        Tag tag3 = new Tag("Food");

        assertEquals(0, tag1.compareTo(tag3)); // tag1 == tag3
        assertEquals(1, tag1.compareTo(tag2)); // tag1 < tag2
        assertEquals(-1, tag2.compareTo(tag1)); // tag2 > tag1
    }

}
