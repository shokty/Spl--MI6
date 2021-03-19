package bgu.spl.mics.application.publishers;


import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.FinalThick;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
    private final int singleTick = 100;
    private int finalTick;
    private int currTick;
    private Timer timer;


    public TimeService(int finalTick) {
        super("Time_Service");
        currTick = 0;
        this.finalTick = finalTick;
        timer = new Timer();
    }


    @Override
    protected void initialize() {
        run();
    }

    @Override
    public void run() {
            TickBroadcast b = new TickBroadcast(finalTick, finalTick - currTick);
            timer.schedule(new TimerTask() {
                public void run() {
                    if (currTick > finalTick) {
                        getSimplePublisher().sendBroadcast(new FinalThick());
                        timer.cancel();
                        timer.purge();

                    }
                    else {
                        b.setCurrentTime(currTick);
                        getSimplePublisher().sendBroadcast(b);
                        currTick++;
                    }
                }
            }, singleTick, singleTick);
    }
}