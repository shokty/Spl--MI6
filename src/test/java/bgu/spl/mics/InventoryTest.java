package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class InventoryTest {
    private static Inventory instanceInventory;
    private List<String> gadgets;

    @BeforeEach
    public void setUp() {
        Inventory instance = Inventory.getInstance();
        instanceInventory = instance;
        String[] gadget = {"Sky Hook", "Dagger shoe", "X-ray Glasses"};
        instanceInventory.load(gadget);
    }

    @Test
    void getInstance() {

    }

    @Test
    void load() {
        boolean sky_hook = instanceInventory.getItem("Sky Hook");
        boolean Dagger_shoe = instanceInventory.getItem("Dagger shoe");
        boolean X_ray_Glasses = instanceInventory.getItem("X-ray Glasses");
        boolean someThingDoesNotin = instanceInventory.getItem("item");
        assertTrue(sky_hook, "Pass check - Sky Hook ");
        assertTrue(Dagger_shoe, "Pass check - Dagger shoe");
        assertTrue(X_ray_Glasses, "Pass check - X-ray Glasses");
        assertTrue(!someThingDoesNotin, "Pass check - Not included item");
    }

    @Test
    void getItem() {
        boolean sky_hook = instanceInventory.getItem("Sky Hook");
        boolean Dagger_shoe = instanceInventory.getItem("Dagger shoe");
        boolean X_ray_Glasses = instanceInventory.getItem("X-ray Glasses");
        boolean someThingDoesNotin = instanceInventory.getItem("item");
        assertTrue(sky_hook, "Pass check - Sky Hook ");
        assertTrue(Dagger_shoe, "Pass check - Dagger shoe");
        assertTrue(X_ray_Glasses, "Pass check - X-ray Glasses");
        assertTrue(!someThingDoesNotin, "Pass check - Not included item");

    }

    @Test
    void printToFile() {
    }

    @AfterEach
    void tearDown() {
    }
}
