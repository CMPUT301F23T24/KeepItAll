package com.example.keepitall;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ScanningUnitTest {

    @Test
    public void testScanningLogic() {
        // Mock scan result
        String mockScanResult = "1234-TestDescription";

        // Mock scanning logic
        String[] scannedParts = mockScanResult.split("-");
        Item newItem = new Item();

        if (scannedParts.length >= 2) {
            newItem.setSerialNumber(Integer.valueOf(scannedParts[0]));
            newItem.setDescription(scannedParts[1]);
            newItem.setPurchaseDate(new Date());
            newItem.setName("newItem");
            newItem.setMake(" ");
            newItem.setModel(" ");
            newItem.setValue(10.0f);
        }

        // Check the values set by scanning logic
        assertEquals(1234, newItem.getSerialNumber().intValue());
        assertEquals("TestDescription", newItem.getDescription());
        assertNotNull(newItem.getPurchaseDate());
        assertEquals("newItem", newItem.getName());
        assertEquals(" ", newItem.getMake());
        assertEquals(" ", newItem.getModel());
        assertEquals(10.0f, newItem.getValue(), 0.001);

    }
}
