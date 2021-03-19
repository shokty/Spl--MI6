package bgu.spl.mics.application.addedClass;

import bgu.spl.mics.Future;

import java.util.List;

/**
 * this object is an obj to create event from type AgentsAvailableEvent
 * this obj have the future result from M gor thr gadget
 */
public class resultOfMonneyPenny {
    private int MonneyPennynName;
    private List<String> agentsName;
    private Future<Boolean> M_get_TheGegts;
    private boolean getAgents;

    public resultOfMonneyPenny(int MonneyPennynName, List<String> agentsName, Future<Boolean> M_get_TheGadgets, boolean getAgents) {
        this.MonneyPennynName = MonneyPennynName;
        this.agentsName = agentsName;
        this.M_get_TheGegts = M_get_TheGadgets;
        this.getAgents = getAgents;
    }

    public Future<Boolean> GetMGotTheGadgets() {
        return M_get_TheGegts;
    }

    public int getMonneyPennyName() {
        return MonneyPennynName;
    }

    public List<String> getAgentsName() {
        return agentsName;
    }

    public boolean isGetAgents() {
        return getAgents;
    }
}
