package bgu.spl.mics;

public class eventTestClass implements Event<String>  {
    private String event;

    public eventTestClass (String event) {
        this.event= event;
    }

    public String getEvent() {
        return event;
    }
}
