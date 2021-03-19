package bgu.spl.mics;

public class SubscriberMTest extends Subscriber {
    private MessageBroker mb;

    public SubscriberMTest() {
        super("NameM");
        mb = MessageBrokerImpl.getInstance();
    }

    @Override
    protected void initialize() {
        subscribeEvent(eventTestClass.class, (ev) -> {
            complete(ev, "O.k");
        });
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