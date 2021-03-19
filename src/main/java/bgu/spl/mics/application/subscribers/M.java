package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.addedClass.resultOfMonneyPenny;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Report;

import java.util.List;
import java.util.concurrent.TimeUnit;
import static bgu.spl.mics.application.addedClass.initializeThreads.informInitialize;
import static java.lang.Long.min;
/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
    int finalTick;
    int currentTime;
    private Diary diaryInstance = Diary.getInstance();
    private int Unique_Number;

    //Constructor
    public M(int Unique_Number) {
        super("M" + Unique_Number);
        this.Unique_Number = Unique_Number;
    }

    /** this function initialize every M.
     * subscribe M to Tick Broad cast - in order to get a Tick by call back
     * subscribe M to Mission Received Event - in order to get a mission info and do it
     * write in the Diary every mission that she did
     * */
    @Override
    protected void initialize() {
        //need a class back to broadcast and call back to
        // anonymous class, the method ‘call’ is overridden
        Callback callbackMissionReceivedEvent;
        callbackMissionReceivedEvent = new Callback<MissionReceivedEvent>() {
            @Override
            public void call(MissionReceivedEvent c) {
                diaryInstance.incrementTotal();

                long timeout = min(((c.getEndTime() - currentTime) * 100),(finalTick- currentTime)*100);
                //when get a MissionReceivedEvent check if agents available and sent them to the mission
                AgentsAvailableEvent eventToSendAgentsAvailable = new AgentsAvailableEvent(c.getSerialAgentsNumbersList(), c.getDuration(), c.getEndTime());
                //send the event to the mb and get a future when you see if the agents available by the result of the future
                Future<resultOfMonneyPenny> futureAgent = getSimplePublisher().sendEvent(eventToSendAgentsAvailable);
                //try to get the result till the mission time expired
                resultOfMonneyPenny resultMPenny = futureAgent.get(timeout, TimeUnit.MILLISECONDS);
                Future MGetGadgetResult;

                //if there is a result and the agents available check if the gadget available
                if (resultMPenny != null && resultMPenny.isGetAgents()) {
                    GadgetAvailableEvent eventToSendGadget = new GadgetAvailableEvent(c.getGadget(), currentTime);
                    //send the event GadgetAvailableEvent
                    Future<Integer> futureGadget = getSimplePublisher().sendEvent(eventToSendGadget);
                    //try to get the result till the mission time expired
                    Integer Found_Gadgets = futureGadget.get(timeout, TimeUnit.MILLISECONDS);
                    complete(eventToSendGadget, Found_Gadgets);

                    //if there is a result and the gadget available check if the time is not expired
                    if (Found_Gadgets != null && currentTime <= c.getEndTime() && Found_Gadgets != -1) {
                        //wait for a future if the M that did the GadgetAvailableEvent for this mission got it
                        MGetGadgetResult = resultMPenny.GetMGotTheGadgets();
                        //resolve result of the future - true
                        MGetGadgetResult.resolve(true);
                        //Get qTime for the report
                        int qTime = futureGadget.get();
                        List<String> agent = resultMPenny.getAgentsName();
                        int moneyPennyName = resultMPenny.getMonneyPennyName();
                        writeAReport(c.getMissionObject(), c.getTimeIssued(), agent, qTime, moneyPennyName, c.getGadget());
                    } else if (resultMPenny != null) {
                        MGetGadgetResult = resultMPenny.GetMGotTheGadgets(); // gets future from the future of monneypenny
                        MGetGadgetResult.resolve(false);
                    }//time expired or no agents
                }
                complete(eventToSendAgentsAvailable, resultMPenny);
            }
        };
        this.subscribeEvent(MissionReceivedEvent.class, callbackMissionReceivedEvent);

        Callback callbackTickBroadcast;
        callbackTickBroadcast = new Callback<TickBroadcast>() {
            @Override
            public void call(TickBroadcast c) {
                currentTime = c.getCurrentTime();
                finalTick=c.getFinalTick();
            }
        };
        //subscribe to get Tick
        this.subscribeBroadcast(TickBroadcast.class, callbackTickBroadcast);
        //inform the in Initialize
        informInitialize();
    }//end initialize


    /** this function get all the date it need to create a report, create it and add it to the diary
     * @param mission - a MissionInfo obj for get the mission data
     * @param timeIssued - the time the mission sent by Intelligence
     * @param agents - a list of agents name
     * @param  qTime - the time the q sent gadget available event
     * @param moneyPennyName -  the serial number of the moneyPenny
     * @param gadget - the name of the gadget of the mission
     * */

    private void writeAReport(MissionInfo mission, int timeIssued, List<String> agents, int qTime, int moneyPennyName, String gadget) {
        Report toWriteReport = new Report();
        toWriteReport.setQTime(qTime);
        toWriteReport.setAgentsNames(agents);
        toWriteReport.setAgentsSerialNumbers(mission.getSerialAgentsNumbers());
        toWriteReport.setMissionName(mission.getMissionName());
        toWriteReport.setTimeCreated(currentTime);
        toWriteReport.setTimeIssued(timeIssued);
        toWriteReport.setM(Unique_Number);
        toWriteReport.setMoneypenny(moneyPennyName);
        toWriteReport.setGadgetName(gadget);
        diaryInstance.addReport(toWriteReport);
    }
}



