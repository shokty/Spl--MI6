package bgu.spl.mics.application.addedClass;
import java.util.List;
import java.util.concurrent.CountDownLatch;
/**
*This class responsible for initialization of all the Threads and used of CountDownLatch
* to initialize TimeService at the end of the initialization of the other threads.
*/
public class initializeThreads {
    private static CountDownLatch latchObject;
    private List<Thread> threadListSub;
    List<Thread> threadListEvents;

    public initializeThreads (List<Thread> threadList,List<Thread> threadListEvents) {
        latchObject = new CountDownLatch(threadListEvents.size());
        this.threadListSub = threadList;
        this.threadListEvents = threadListEvents;
    }
        public static void informInitialize(){
            latchObject.countDown();
        }

        public  void initializeThreadsActivate() {
            for (Thread t : this.threadListSub) {
                t.start();
            }
            // Waiting for initialization of all the threads and then start the timeService
            try {
                latchObject.await();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Thread t : threadListEvents) {
                try {
                    t.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


}

