package com.example.keepitall;
import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Date;

public class ItemManagerTest {
    private ItemManager itemManager;


    @Test
    public void testAddItem() {
        itemManager = new ItemManager();
        Item item = new Item(new Date(), "Test Item", "Test Make", "Test Model", 1, 10.0f, "Name");
        itemManager.addItem(item);
        assertTrue(itemManager.getAllItems().contains(item));
    }

    @Test
    public void testDeleteItem() {
        itemManager = new ItemManager();
        Item item = new Item(new Date(), "Test Item", "Test Make", "Test Model", 2, 30.0f, "Name");
        itemManager.addItem(item);
        itemManager.deleteItem(item);
        assertFalse(itemManager.getAllItems().contains(item));
    }

    @Test
    public void testEditItem() {
        itemManager = new ItemManager();
        Item item = new Item(new Date(), " test desc", " Vintage", "Bond007", 143, 70.0f, "Name");
        itemManager.addItem(item);

        Date PurchaseDate_new = new Date();
        String Description_new = "234";
        String Make_new = "2023";
        String Model_new = "Yamaha";
        Integer SerialNumber_test = 999;
        float Value_new = 60.0f;

        itemManager.editItem(item,PurchaseDate_new, Description_new, Make_new,Model_new, SerialNumber_test, Value_new);

        assertEquals(PurchaseDate_new, item.getPurchaseDate());
        assertEquals(Description_new, item.getDescription());
        assertEquals(Make_new, item.getMake());
        assertEquals(Model_new, item.getModel());
        assertEquals(SerialNumber_test, item.getSerialNumber());
        assertEquals(Value_new, item.getValue(), 0.001f);
    }
}