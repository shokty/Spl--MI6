package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;



public class GadgetAvailableEvent implements Event<Integer>{
    private String gadget;
    public GadgetAvailableEvent(String gadget, int time) {
        this.gadget = gadget;
    }

    public String getGadget() {
        return gadget;
    }

}
