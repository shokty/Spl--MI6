package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import static bgu.spl.mics.application.addedClass.initializeThreads.informInitialize;


/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
	private static Inventory instance;
	private int currentTime;

	//Constructor
	public Q() {
		super("Q");
		this.instance = Inventory.getInstance();
	}

	@Override
	protected void initialize() {
		// anonymous class, the method ‘call’ is overriden
		Callback callbackGadgetAvailable;
		callbackGadgetAvailable = new Callback<GadgetAvailableEvent>() {
			@Override
			public void call(GadgetAvailableEvent c) {
				//it it the qtime in the report
				int timeGetEvent = currentTime;
				boolean answer = instance.getItem(c.getGadget());
				if (answer) {
				complete(c, timeGetEvent);
				}
				else {
					complete(c, -1);
				}

			}
		};

		this.subscribeEvent(GadgetAvailableEvent.class,callbackGadgetAvailable);

		Callback callbackTickBroadcast;
		callbackTickBroadcast = new Callback<TickBroadcast>() {
			@Override
			public void call(TickBroadcast c) {
				currentTime = c.getCurrentTime();
			}
		};
		//subscribe to get Tick
		this.subscribeBroadcast(TickBroadcast.class,callbackTickBroadcast);
		//inform the in Initialize
		informInitialize();
	}

}
