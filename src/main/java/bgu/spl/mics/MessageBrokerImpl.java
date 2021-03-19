package bgu.spl.mics;

import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */

public class MessageBrokerImpl implements MessageBroker {
    // only for getInstance
    private static MessageBrokerImpl Instance = new MessageBrokerImpl();

    private ConcurrentHashMap<Subscriber, BlockingDeque<Message>> Subscriber_ToDo;

    private ConcurrentHashMap<Class<? extends Message>, ConcurrentLinkedQueue<Subscriber>> Message_Subscriber_Map;
    // Every Message type should have a Que of Subscriber`s that should be ready to act when time arrive to do this.
    // we put them in que so it will be in Round-Robin matter.

    private ConcurrentHashMap<Event, Future> Event_Future_Map;
    //every Event should have a Future to it.


    private MessageBrokerImpl() {
        Subscriber_ToDo = new ConcurrentHashMap<>();
        Event_Future_Map = new ConcurrentHashMap<>();
        Message_Subscriber_Map = new ConcurrentHashMap<>();
    }

    /**
     * Retrieves the single instance of this class.
     */
    public static MessageBroker getInstance() {
        return Instance;
    }


    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
        Message_Subscriber_Map.putIfAbsent(type, new ConcurrentLinkedQueue<Subscriber>());
        Message_Subscriber_Map.get(type).add(m);
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
        Message_Subscriber_Map.putIfAbsent(type, new ConcurrentLinkedQueue<>());
        Message_Subscriber_Map.get(type).add(m);
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        Event_Future_Map.get(e).resolve(result);
    }


    @Override
    public void sendBroadcast(Broadcast b) {
        ConcurrentLinkedQueue que = Message_Subscriber_Map.get(b.getClass()); // get all the keys
        Iterator iter = que.iterator();
        while (iter.hasNext()) {
            Subscriber_ToDo.get(iter.next()).add(b);
        } // go over all the keys and try to remove m , if not exist do nothing
    }


    @Override
    /** Pre : we assume that we did subscribeEvent before sending it
     */
    public <T> Future<T> sendEvent(Event<T> e) {
        synchronized (Message_Subscriber_Map.get(e.getClass()))
        //We put the synchronized here so the round robin will be fine
        // if we dont put it here we might get an activation frame between poll and add
        {
            ConcurrentLinkedQueue que = Message_Subscriber_Map.get(e.getClass());
            if (!(que == null)) {
                Future<T> future = new Future<>();
                Event_Future_Map.put(e, future);
                Subscriber subscriber = (Subscriber) que.poll();
                // pull the first subscriber that is available in the round robin and give to him the event
                if (subscriber != null) {
                    synchronized (subscriber) {
                        BlockingDeque subque = Subscriber_ToDo.get(subscriber);
                        if (subque!=null) {
                            subque.add(e);
                            que.add(subscriber);
                        }
                    }
                }
                return future;
            } else
                return null;
        }
    }


    @Override
    public void register(Subscriber m) {
        Subscriber_ToDo.putIfAbsent(m, new LinkedBlockingDeque<Message>());
    }

    @Override
    public void unregister(Subscriber m) {
            // get all the keys
            Set<Class<? extends Message>> keys = Message_Subscriber_Map.keySet();
            for (Class<? extends Message> key : keys) {
                synchronized (Message_Subscriber_Map.get(key)) {
                    Message_Subscriber_Map.get(key).remove(m);
                }
            } // go over all the keys and try to remove m , if m not exist do nothing

        synchronized (m) {
            //remove the subscriber
            Subscriber_ToDo.remove(m);
        }
    }

    @Override
    public Message awaitMessage(Subscriber m) throws InterruptedException {
        return Subscriber_ToDo.get(m).take();
    }
}

