package bgu.spl.mics.example.publishers;

import java.util.concurrent.TimeUnit;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;

public class ExampleMessageSender extends Subscriber {

    private boolean broadcast;

    public ExampleMessageSender(String name, String[] args) {
        super(name);

        if (args.length != 1 || !args[0].matches("broadcast|event")) {
            throw new IllegalArgumentException("expecting a single argument: broadcast/event");
        }

        this.broadcast = args[0].equals("broadcast");
    }

    @Override
    protected void initialize() {
        System.out.println("Sender " + getName() + " started");
        if (broadcast) {
            getSimplePublisher().sendBroadcast(new ExampleBroadcast(getName()));
            System.out.println("Sender " + getName() + " publish an event and terminate");
            terminate();
        } else {
            Future<String> futureObject = getSimplePublisher().sendEvent(new ExampleEvent(getName()));
            if (futureObject != null) {
            	String resolved = futureObject.get(100, TimeUnit.MILLISECONDS);
            	if (resolved != null) {
            		System.out.println("Completed processing the event, its result is \"" + resolved + "\" - success");
            	}
            	else {
                	System.out.println("Time has elapsed, no subscriber has resolved the event - terminating");
                }
            }
            else {
            	System.out.println("No Subscriber has registered to handle ExampleEvent events! The event cannot be processed");
            }
            terminate();
        }
    }

    /**
     * Subscribes to events of type {@code type} with the callback
     * {@code callback}. This means two things:
     * 1. Subscribe to events in the singleton MessageBroker using the supplied
     * {@code type}
     * 2. Store the {@code callback} so that when events of type {@code type}
     * are received it will be called.
     * <p>
     * For a received message {@code m} of type {@code type = m.getClass()}
     * calling the callback {@code callback} means running the method
     * {@link Callback#call(Object)} by calling
     * {@code callback.call(m)}.
     * <p>
     *
     * @param type     The {@link Class} representing the type of event to
     *                 subscribe to.
     * @param callback The callback that should be called when messages of type
     *                 {@code type} are taken from this Subscriber message
     */

}
