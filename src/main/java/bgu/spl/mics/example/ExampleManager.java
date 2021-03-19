package bgu.spl.mics.example;

import bgu.spl.mics.example.subscribers.ExampleBroadcastSubscriber;
import bgu.spl.mics.example.publishers.ExampleMessageSender;
import bgu.spl.mics.example.subscribers.ExampleEventHandlerSubscriber;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ExampleManager {

    public static void main(String[] args) {
        Map<String, Creator> creators = new HashMap<>();
        creators.put("ev-handler", ExampleEventHandlerSubscriber::new);
        creators.put("brod-listener", ExampleBroadcastSubscriber::new);
        creators.put("sender", ExampleMessageSender::new);

        Scanner sc = new Scanner(System.in); // Above statement we create an object of a Scanner class which is define in import java.util.scanner package. scanner class allows user to take input form console.
        // System.in is passed as parameter in scanner class will tell to java compiler system input will be provided through console(keyboard).
        boolean quit = false;
        try {
            System.out.println("Example manager is started - supported commands are: start,quit");
            System.out.println("Supporting apps: " + creators.keySet());
            //The java.util.HashMap.keySet() method in Java is used to create a set out of the key elements contained in the hash map.
            // It basically returns a set view of the keys or we can create a new set and store the key elements in them.
            while (!quit) {

                String line = sc.nextLine();
                String[] params = line.split("\\s+"); //will split the string into string of array with separator as space or multiple spaces.
                    //start <subscriber-type> <subscriber-name> <subscriber-args>
                if (params.length > 0) {
                    //start
                    switch (params[0]) {
                        case "start":
                            try {
                                if (params.length < 3) {
                                    throw new IllegalArgumentException("Expecting app type and id, supported types: " + creators.keySet());
                                }
                                Creator creator = creators.get(params[1]); //get (<subscriber-type>) -
                                if (creator == null) {
                                    throw new IllegalArgumentException("unknown app type, supported types: " + creators.keySet());
                                }

                                new Thread(creator.create(params[2],// <subscriber-name>
                                        Arrays.copyOfRange(params, 3, params.length)
                                )// end of create method send a <subscriber-name> and an arry
                                ) // end of newThread
                                        .start(); // start the Thread

                            } catch (IllegalArgumentException ex) {
                                System.out.println("Error: " + ex.getMessage());
                            }

                            break;
                        case "quit":
                            quit = true;
                            break;
                    } //end switch
                } // end if
            } //end quit
        } catch (Throwable t) {
            System.err.println("Unexpected Error!!!!");
            t.printStackTrace();
        } finally {
            System.out.println("Manager Terminating - UNGRACEFULLY!");
            sc.close();
            System.exit(0);
        }
    }
}
