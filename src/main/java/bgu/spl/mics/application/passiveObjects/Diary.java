package bgu.spl.mics.application.passiveObjects;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Passive object representing the diary where all reports are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Diary {
    private List<Report> reports;
    private AtomicInteger total;

    /**
     * Retrieves the single instance of this class.
     */
    //Constructor
   private Diary() {
        reports = new LinkedList<Report>();
        total = new AtomicInteger(0);
    }

    private static class SingletonHolder {
        private static Diary instance = new Diary();
    }

    public static Diary getInstance() {
        return Diary.SingletonHolder.instance;
    }


    public List<Report> getReports() {
        return reports;
    }

    /**
     * adds a report to the diary
     *
     * @param reportToAdd - the report to add
     */
    public synchronized void addReport(Report reportToAdd) {
        if (reportToAdd != null) {
            reports.add(reportToAdd);
        }
    }

    /**
     * <p>
     * Prints to a file name @filename a serialized object List<Report> which is a
     * List of all the reports in the diary.
     * This method is called by the main method in order to generate the output.
     */
    public void printToFile(String filename) {
        Gson MyGson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).setPrettyPrinting().create();
        String MyDairyToPrint = MyGson.toJson(Diary.getInstance());
        try (Writer writer = new FileWriter(filename)) {
            writer.write(MyDairyToPrint);
        } catch (IOException e) {
        }

    }

    /**
     * Gets the total number of received missions (executed / aborted) be all the M-instances.
     *
     * @return the total number of received missions (executed / aborted) be all the M-instances.
     */
    public int getTotal() {
        return total.get();
    }

    /**
     * Increments the total number of received missions by 1
     */
    public void incrementTotal() {
        int val;

        do {
            val = total.get();
            //use cas to total in order to promise the correct value of num of reports
        } while (!total.compareAndSet(val, val + 1));
    }

}

