package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import java.util.List;


 //Intelligence sends a MissionReceivedEvent to the MessageBroker.
 //M fetches this event and the corresponding if the agent available and if gadget available for the mission

public class MissionReceivedEvent implements Event<MissionReceivedEvent> {
    private MissionInfo missionObject;
    private String senderName;
    private String missionName;
    private List<String> serialAgentsNumbers;
    private int timeIssued;
    private String gadget;
    private int startTime;
    private int endTime;
    private int duration;

    public MissionReceivedEvent(MissionInfo mission, String senderName, int currTick) {
        this.missionObject = mission;
        this.senderName = senderName;
        this.missionName = mission.getMissionName();
        this.serialAgentsNumbers = mission.getSerialAgentsNumbers();
        this.gadget = mission.getGadget();
        this.endTime = mission.getTimeExpired();
        this.startTime = mission.getTimeIssued();
        this.duration = mission.getDuration();
        this.timeIssued =currTick;
    }


    public List<String> getSerialAgentsNumbersList() {
        return this.serialAgentsNumbers;
    }

    public String getGadget() {
        return gadget;
    }

    public int getEndTime() {
        return endTime;
    }


    public MissionInfo getMissionObject() {
        return missionObject;
    }
    public int getDuration(){return duration;}

    public int getTimeIssued() {
        return timeIssued;
    }
}



