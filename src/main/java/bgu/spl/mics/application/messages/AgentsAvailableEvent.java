package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.addedClass.resultOfMonneyPenny;

import java.util.List;
// this is event in a type of resultOfMonneyPenny M sent this event to the mb
public class AgentsAvailableEvent implements Event<resultOfMonneyPenny> {
    private List<String> serialAgentsNumbers;
    private int duration;
    private int timeExpired;


    public AgentsAvailableEvent(List<String> serialAgentsNumbers, int duration, int timeExpired) {
        this.serialAgentsNumbers = serialAgentsNumbers;
        this.duration = duration;
        this.timeExpired = timeExpired;
    }

    public List<String> getAgentsList() {
        return serialAgentsNumbers;
    }


    public int getDuration() {
        return duration;
    }

    public int getTimeExpired() {
        return timeExpired;
    }
}