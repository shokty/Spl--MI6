package bgu.spl.mics.application;
import bgu.spl.mics.application.addedClass.load;

import java.util.List;
import static bgu.spl.mics.application.addedClass.printJsonFile.print;

/**
 * This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */

//TODO: We need to remove all the unnecessary imports from the all class becouse its causing error`s.
public class MI6Runner {
    public static void main(String[] args)
    {
        load loadInput = new load(args[0]);
        load.loadAll(args[0]);

        List<Thread> threadListSub = load.getThreadList();
        for (Thread t : threadListSub) {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        }
        //Print the inventory and the Diary
        print(args[1], args[2]);
    }
}
