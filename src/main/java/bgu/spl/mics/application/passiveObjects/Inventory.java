package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */



public class Inventory {
    private List<String> gadgets;

    /**
     * Retrieves the single instance of this class.
     */

    //Constructor
    private Inventory() {
        new Inventory.SingletonHolderInventory();
        gadgets = new LinkedList<>();
    }

    private static class SingletonHolderInventory {
        private static Inventory instance = new Inventory();
    }

    public static Inventory getInstance() {
        return Inventory.SingletonHolderInventory.instance;
    }

    /**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     *
     * @param inventory Data structure containing all data necessary for initialization
     *                  of the inventory.
     */
    public void load(String[] inventory) {
        if (!(inventory.length == 0)) {
            int gadgetsSize = inventory.length;
            for (int i = 0; i < gadgetsSize; i++) {
                if (!(gadgets.contains(inventory[i]) && !((inventory[i]) == null))) {
                    gadgets.add(inventory[i]);
                }
            }
        }
    }

    /**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     *
     * @param gadget Name of the gadget to check if available
     * @return ‘false’ if the gadget is missing, and ‘true’ otherwise
     */
    public boolean getItem(String gadget) {
        if (gadgets.isEmpty()) {
            return false;
        } else {
            if (gadgets.contains(gadget)) {
                gadgets.remove(gadget);
                return (true);
            } else {
                return false;
            }
        }
    }

    /**
     * <p>
     * Prints to a file name @filename a serialized object List<String> which is a
     * list of all the of the gadgeds.
     * This method is called by the main method in order to generate the output.
     */
    public void printToFile(String filename) {
        Gson g = new Gson();
        String inventory = g.toJson(gadgets);
        try (Writer write = new FileWriter(filename)) {
            write.write(inventory);
        } catch (Exception e) {
            System.out.println("filename incorrect");
        }
    }
}
