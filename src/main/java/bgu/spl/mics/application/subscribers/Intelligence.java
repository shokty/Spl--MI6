package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.LinkedList;
import java.util.List;

import static bgu.spl.mics.application.addedClass.initializeThreads.informInitialize;


/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
	int serial;
	int currTick;
	List<MissionInfo> missionInfoList;

	//Constructor
	public Intelligence() {
		super("Intelligence");
	}

	public Intelligence(List<MissionInfo> missionInfoList, int serial) {
		super("Intelligence " + serial);
		//give a special num for every Intelligence
		this.serial = serial;
		this.missionInfoList = missionInfoList;
	}

	protected void initialize() {
		//initialize as a subscriber
		//initialize as a publisher
		// anonymous class, the method ‘call’ is Override
		Callback callbackTickBroadcast;
		callbackTickBroadcast = new Callback<TickBroadcast>() {
			@Override
			//Sends a MissionReceivedEvent with the information that is needed to accomplish the mission.
			//subscribe to get Tick
			public void call(TickBroadcast c) {
				// whatever you want to happen when TickBroadcast is received
				currTick = c.getCurrentTime();
				//get a mission that compatible with currTick
				List<MissionInfo> getMissionAtThisTimeTick = getMissionAtThisTimeTick(currTick);
				if (getMissionAtThisTimeTick!=null) {
					for(MissionInfo mission : getMissionAtThisTimeTick) {
						Event<MissionReceivedEvent> eventToSend = new MissionReceivedEvent(mission, getName(), currTick);
						Future<MissionReceivedEvent> futureToSend = getSimplePublisher().sendEvent(eventToSend);
					}
				}
			}
		};
		//subscribe to get Tick
		this.subscribeBroadcast(TickBroadcast.class,callbackTickBroadcast);
		//inform the in Initialize
		informInitialize();
	}

	/** this function get at time tick
	 * @param timeTick - get a time tick to find a mission in it
	 * @return mission that it time is the timeTick the function got
	 * */
	public List<MissionInfo> getMissionAtThisTimeTick(int timeTick) {
		//if there is no a mission you will get null
		List<MissionInfo> missionToReturn = new LinkedList<>();
		if(!(missionInfoList.isEmpty())) {
			for (MissionInfo mission : missionInfoList) {
				boolean stopLoop=(mission.getTimeIssued()==timeTick);
				if (stopLoop) {
					missionToReturn.add(mission);
				}
			}
		}
		return (missionToReturn) ;
	}

}
