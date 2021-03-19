package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.addedClass.resultOfMonneyPenny;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static bgu.spl.mics.application.addedClass.initializeThreads.informInitialize;
import static java.lang.Long.min;


/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
    private int currentTime;
    private int serialNumber;
    private int finalTick;
    private Squad squadInstance = Squad.getInstance();

    //Constructor
    public Moneypenny(int serialNumber) {
        super("Moneypenny " + serialNumber);
        this.serialNumber = serialNumber;
    }

    protected void initialize() {
        // anonymous class, the method ‘call’ is overriden

        Callback callbackAgentsAvailable;
        callbackAgentsAvailable = new Callback<AgentsAvailableEvent>() {
            @Override
            public void call(AgentsAvailableEvent c) {
                int timeExpired = c.getTimeExpired();
                long timeout = min(timeExpired,finalTick);
                List<String> agentSerialNumber = c.getAgentsList();
                //sort the serials num to avoid dead lock
                Collections.sort(agentSerialNumber);
                Boolean getAgents = squadInstance.getAgents(agentSerialNumber);
                List<String> agentName = new LinkedList<>();
                //if Moneypenny got the agents she will get their names
                if(getAgents) {
                    agentName = squadInstance.getAgentsNames(agentSerialNumber);
                }

                Future<Boolean> MgotGadgets = new Future<>();
                resultOfMonneyPenny resultOfMonneyPenny = new resultOfMonneyPenny(serialNumber, agentName , MgotGadgets , getAgents);
                complete(c, resultOfMonneyPenny);
                //try to get the result till the mission time expired
                Boolean MGotGadgetsBool = MgotGadgets.get((timeout - currentTime) * 100, TimeUnit.MILLISECONDS);

                //if Moneypenny got the agents she will send them to the mission
                if (MGotGadgetsBool != null && MGotGadgetsBool.booleanValue() == true && getAgents) {
                    squadInstance.sendAgents(agentSerialNumber, c.getDuration());
                }
                else
                    squadInstance.releaseAgents(agentSerialNumber);
            }
        };
        this.subscribeEvent(AgentsAvailableEvent.class, callbackAgentsAvailable);

        Callback callbackTickBroadcast;
        callbackTickBroadcast = new Callback<TickBroadcast>() {
            @Override
            public void call(TickBroadcast c) {
                finalTick = c.getFinalTick();
                currentTime = c.getCurrentTime();
            }
        };
        //subscribe to get Tick
        this.subscribeBroadcast(TickBroadcast.class, callbackTickBroadcast);
        //inform the in Initialize
        informInitialize();
    }
}

