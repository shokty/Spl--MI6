package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {
    private Future<Integer> future;

    @BeforeEach
    public void setUp() {
        future = new Future<>();
    }

    @Test
    /*
    Tests that trying to fetch the result after resolving returns immediately (using indefinitely blocking get)
     */
    public void BasicGetTest() {
        future.resolve(5);
        Integer result = future.get();
        assertEquals(5, result, "Different result after resolve");
        assertNotNull(result, "Should not be null here");
        //Integer Test

        Future<String> future2 = new Future<>();
        future2.resolve("BasicGetTest");
        String result2 = future2.get();
        assertEquals("BasicGetTest", result2, "Should be Equals");
        assertNotNull(result, "Should not be null here");
        //String test


    }

    @Test
    public void BasicDone() {
        assertFalse(future.isDone(), "Shouldent be done in here");
        future.resolve(190);
        assertTrue(future.isDone(), "Shouldent be done in here");
    }

    @Test
    /*
    Tests that trying to fetch the result after resolving returns immediately
     */
    public void NormalGetLongerChack() {
        assertFalse(future.isDone(), "Shouldent be done in here");
        future.resolve(5);
        long start = System.currentTimeMillis();
        Integer result = future.get();
        long end = System.currentTimeMillis();
        long duration = end - start;

        // Should be immediate, giving 1 milisec mrining error
        assertTrue(0 <= duration && duration <= 1, "takeing to much time");
        assertEquals(5, result, "Different result after resolve");
        assertTrue(future.isDone(), "Should be done afther resolve");
    }

    @Test
    /*
    Tests that trying to fetch the result after resolving returns immediately (using the timedget)
     */
    public void immediateTimedResolveGet() {
        future.resolve(5);
        long start = System.currentTimeMillis();
        Integer result = future.get(50, TimeUnit.MILLISECONDS);
        long end = System.currentTimeMillis();
        long duration = end - start;
        // error margin of 1ms

        assertTrue(0 <= duration && duration <= 1, "Waited before returning the result");
        assertEquals(5, result, "Different result after resolve");
    }

    @Test
    /*
    Test chacking if its going to sleep and getting the result in time
     */
    public void Timeget_ChackWatining3() {

        Thread fucturetest = new Thread(() -> {
            try {
                Thread.sleep(20);
                future.resolve(7);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        fucturetest.start();
        long start = System.currentTimeMillis(); // start a timer for start
        try {
            fucturetest.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        long duration = end - start;

        // error margin of 10ms
        assertTrue(20 <= duration && duration <= 30, "Did`nt go to sleep");
        fucturetest.interrupt();
    }


    @Test
    /*
    Should test if it is acctuly wating for 20 sec and return the true value.
     */
    public void Timeget_ChackWatining() {
        Thread fucturetest = new Thread(() -> {
            try {
                Thread.sleep(20);
                future.resolve(7);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        fucturetest.start();
        long start = System.currentTimeMillis();
        Integer result = future.get(50, TimeUnit.MILLISECONDS);
        long end = System.currentTimeMillis();
        long duration = end - start;

        assertEquals(7, result, "Different result after resolve");
        // error margin of 2ms
        assertTrue(20 <= duration && duration <= 60, "Waited too long or too little to get the result");
        assertTrue(future.isDone(), "Not done after resolve");
        fucturetest.interrupt();
        try {
            fucturetest.interrupt();
            fucturetest.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /*
    Tests that trying to fetch the result afther times out.
     */
    @Test
    public void Timeget_ChackWatining2() {
        Thread fucturetest = new Thread(() -> {
            try {
                Thread.sleep(50);
                future.resolve(7);
            } catch (InterruptedException e) {
            }
        });
        fucturetest.start();

        long start = System.currentTimeMillis();
        Integer result = future.get(30, TimeUnit.MILLISECONDS);
        long end = System.currentTimeMillis();
        long duration = end - start;

        assertNull(result, "Got some result despite timeout");
        // error margin of 2ms
        assertTrue(30 <= duration && duration <= 32, "Blocked for longer than neeeded");
        assertFalse(future.isDone(), "Done despite timeout");
        try {
            fucturetest.interrupt();
            fucturetest.join();
        } catch (InterruptedException e) {
        }
    }

    @Test
    //testing that the converting is working.
    public void Timeget_ChackWatining3_SECONDS() {
        Thread fucturetest = new Thread(() -> {
            try {
                Thread.sleep(500000);
                future.resolve(7);
            } catch (InterruptedException e) {
            }
        });
        fucturetest.start();

        long start = System.currentTimeMillis();
        Integer result = future.get(2, TimeUnit.SECONDS);
        long end = System.currentTimeMillis();
        long duration = end - start;
        duration = TimeUnit.MILLISECONDS.toSeconds(duration);

        assertNull(result, "Got some result despite timeout");
        // error margin of 2ms
        assertTrue(0 <= duration && duration <= 2, "Blocked for longer than neeeded");
        assertFalse(future.isDone(), "Done despite timeout");
        try {
            fucturetest.interrupt();
            fucturetest.join();
        } catch (InterruptedException e) {
        }
    }
}
