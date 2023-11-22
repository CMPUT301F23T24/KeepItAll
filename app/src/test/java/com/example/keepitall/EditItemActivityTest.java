package com.example.keepitall;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditItemActivityTest {

    private Item testItem;
    private Date newDate;

    @Before
    public void setUp() throws ParseException {
        testItem = new Item(new Date(), "Old Description", "Old Make", "Old Model", 123456, 100.0f, "Old Name");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String newDateStr = "01/01/2022";
        newDate = dateFormat.parse(newDateStr);
    }

    @Test
    public void testItemEditing() {
        String newName = "New Name";
        String newMake = "New Make";
        String newModel = "New Model";
        int newSerialNumber = 654321;
        float newValue = 200.0f;
        String newDescription = "New description";

        // Update the item
        testItem.setName(newName);
        testItem.setMake(newMake);
        testItem.setModel(newModel);
        testItem.setSerialNumber(newSerialNumber);
        testItem.setValue(newValue);
        testItem.setDescription(newDescription);
        testItem.setPurchaseDate(newDate);

        assertEquals("Name did not update correctly", newName, testItem.getName());
        assertEquals("Make did not update correctly", newMake, testItem.getMake());
        assertEquals("Model did not update correctly", newModel, testItem.getModel());
        assertEquals("Serial Number did not update correctly", newSerialNumber, (int) testItem.getSerialNumber());
        assertEquals("Value did not update correctly", newValue, testItem.getValue(), 0.0);
        assertEquals("Description did not update correctly", newDescription, testItem.getDescription());
        assertEquals("Purchase Date did not update correctly", newDate, testItem.getPurchaseDate());
    }
}
