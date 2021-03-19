package bgu.spl.mics.application.addedClass;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Inventory;
/**
 * this class will be called in the end of the run
 * we will print the dairy and what left of the inventory
 */

public class printJsonFile {
    private static Diary s = Diary.getInstance();
    private static Inventory inventory = Inventory.getInstance();

    public static void print (String pathInventory, String pathDiary) {
        s.printToFile(pathDiary);
        inventory.printToFile(pathInventory);
    }
}
