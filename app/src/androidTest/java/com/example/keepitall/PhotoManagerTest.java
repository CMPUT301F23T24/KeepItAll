package com.example.keepitall;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import androidx.test.espresso.action.ViewActions;
import org.junit.Test;

/**
 * This class is used to test the photo functionality of the app.
 */
public class PhotoManagerTest {
    @Test
    public void testAddPhoto() {
        // Test the ability to add, remove photos
        Item item = new Item();
        item.addPhoto("photoPath");
        assertEquals("photoPath", item.getPhotoList().get(0));
        // check the size of the list
        assertEquals(1, item.getPhotoList().size());

        // Remove the photo (since its saved in the database)
        item.removePhoto("photoPath");
    }
    @Test
    public void testRemovePhoto() {
        // Test the ability to add, remove photos
        Item item = new Item();
        item.addPhoto("photoPath");
        assertEquals("photoPath", item.getPhotoList().get(0));
        // check the size of the list
        assertEquals(1, item.getPhotoList().size());
        // Remove the photo (since its saved in the database)
        item.removePhoto("photoPath");
        // check the size of the list
        assertEquals(0, item.getPhotoList().size());
    }
}
