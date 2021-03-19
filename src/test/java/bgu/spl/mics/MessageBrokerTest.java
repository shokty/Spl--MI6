package bgu.spl.mics;

import bgu.spl.mics.application.subscribers.M;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageBrokerTest {
    MessageBroker MessageBroker;
    M m;

    @BeforeEach
    void setUp() {
        MessageBroker = MessageBrokerImpl.getInstance();
        m=new M(1);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getInstance() {
        MessageBroker mb2 = MessageBrokerImpl.getInstance();
        assertNotNull(mb2);
        assertEquals(MessageBroker, mb2);
        assertSame(MessageBroker, mb2);
    }

    @Test
    void subscribeEvent() {
        assertDoesNotThrow(() -> {
            MessageBroker.subscribeEvent(null, null);
        });
    }

    @Test
    void subscribeBroadcast() {
        assertDoesNotThrow(() -> {
            MessageBroker.subscribeBroadcast(null, null);
        });
    }

    @Test
    void complete() {
        assertDoesNotThrow(() -> {
            MessageBroker.complete(null, null);
        });
    }

    @Test
    void sendBroadcast() {
    }

    @Test
    void sendEvent() {
        eventTestClass eventTest = new eventTestClass("event");
        Future<String> futureTest = MessageBroker.sendEvent(eventTest);
        assertEquals("O.k",futureTest.get());
    }

    @Test
    void register() {
    }

    @Test
    void unregister() {
    }

    @Test
    void awaitMessage() {
    }
}
