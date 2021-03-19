package bgu.spl.mics.application.messages;
import bgu.spl.mics.Broadcast;

//sub and pub register to this broadcast inorder to get ticks
public class TickBroadcast implements Broadcast {
    private int currentTime;
    private final int finalTick;

    public TickBroadcast(int finalTick, int currentTime){
        this.currentTime = currentTime;
        this.finalTick = finalTick;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public int getFinalTick() {
        return finalTick;
    }
}
