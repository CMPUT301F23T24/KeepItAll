package com.example.keepitall;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public class EditItemActivityTest {

    @Test
    public void itemUpdateTest() {
        Item testItem = new Item();
        testItem.setName("Old Name");
        testItem.setMake("Old Make");
        testItem.setModel("Old Model");
        testItem.setSerialNumber(123456);
        testItem.setValue(100.0f);
        testItem.setDescription("Old description");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date oldDate = new Date();
        testItem.setPurchaseDate(oldDate);

        String newName = "New Name";
        String newMake = "New Make";
        String newModel = "New Model";
        int newSerialNumber = 654321;
        float newValue = 200.0f;
        String newDescription = "New description";
        String newDateStr = "01/01/2022";
        Date newDate = null;

        try {
            newDate = dateFormat.parse(newDateStr);
        } catch (Exception e) {
            fail("Date format is invalid");
        }

        // Updating the item with new data
        testItem.setName(newName);
        testItem.setMake(newMake);
        testItem.setModel(newModel);
        testItem.setSerialNumber(newSerialNumber);
        testItem.setValue(newValue);
        testItem.setDescription(newDescription);
        testItem.setPurchaseDate(newDate);

        // Assert that the testItem's properties have been updated
        assertEquals("Name did not update correctly", newName, testItem.getName());
        assertEquals("Make did not update correctly", newMake, testItem.getMake());
        assertEquals("Model did not update correctly", newModel, testItem.getModel());
        assertEquals("Serial Number did not update correctly", Optional.of(newSerialNumber), testItem.getSerialNumber());
        assertEquals("Value did not update correctly", newValue, testItem.getValue(), 0.0);
        assertEquals("Description did not update correctly", newDescription, testItem.getDescription());
        assertEquals("Purchase Date did not update correctly", newDate, testItem.getPurchaseDate());
    }
}
