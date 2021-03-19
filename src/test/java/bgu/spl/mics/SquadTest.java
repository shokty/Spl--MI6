package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SquadTest {
    private Squad instanceSquad;
    Agent[] agents;

    @BeforeEach
    public void setUp() {
        instanceSquad = Squad.getInstance();
        agents = new Agent[6];
        agents[0] = new Agent();
        agents[0].setName("000");
        agents[0].setSerialNumber("0");

        agents[1] = new Agent();
        agents[1].setName("001");
        agents[1].setSerialNumber("1");

        agents[2] = new Agent();
        agents[2].setName("002");
        agents[2].setSerialNumber("2");

        agents[3] = new Agent();
        agents[3].setName("003");
        agents[3].setSerialNumber("3");

        agents[4] = new Agent();
        agents[4].setName("004");
        agents[4].setSerialNumber("4");

        agents[5] = new Agent();
        agents[5].setName("005");
        agents[5].setSerialNumber("5");
        instanceSquad.load(agents);
    }

    @Test
    void getInstance() {
    }

    @Test
    void load() {
        List<String> namesAgents = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            namesAgents.add(String.valueOf(i));
        }
        List<String> result = instanceSquad.getAgentsNames(namesAgents);
        assertEquals(true, result.contains("001"));
        assertEquals(true, result.contains("002"));
        assertEquals(true, result.contains("003"));
        assertEquals(true, result.contains("004"));
        assertEquals(true, result.contains("005"));
        assertEquals(false, result.contains("007"));
    }

    @Test
    void releaseAgents() {
        List<String> realsing = new ArrayList<>();
        for (int i = 1; i < 5; i++) {
            realsing.add(String.valueOf(i));
        }
        instanceSquad.releaseAgents(realsing);

        for (int i = 0; i < 5; i++)
            assertEquals(true, agents[0].isAvailable());

    }

    @Test
        // not need to do
    void sendAgents() {
    }

    @Test
    void getAgents() {
        List<String> agent1 = new ArrayList<>();
        agent1.add("1");
        assertEquals(true, instanceSquad.getAgents(agent1));

        List<String> agent2 = new ArrayList<>();
        agent2.add("2");
        assertEquals(true, instanceSquad.getAgents(agent2));

        agents[1].release();
        agents[2].release(); //relesing them


        List<String> agent1_2 = new ArrayList<>();
        agent1_2.add("1");
        agent1_2.add("2");
        assertEquals(true, instanceSquad.getAgents(agent1_2)); //chacking afther relese


        List<String> agentNull = new ArrayList<>();
        assertEquals(true, instanceSquad.getAgents(agentNull));

        List<String> agentNotInList = new ArrayList<>();
        agentNotInList = new ArrayList<>();
        agentNotInList.add("6");
        assertEquals(false, instanceSquad.getAgents(agentNotInList));
    }

    @Test
    void getAgentsNames() {
        List<String> namesAgents = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            namesAgents.add(String.valueOf(i));
        }
        List<String> result = instanceSquad.getAgentsNames(namesAgents);
        assertEquals(true, result.contains("001"));
        assertEquals(true, result.contains("002"));
        assertEquals(true, result.contains("003"));
        assertEquals(true, result.contains("004"));
        assertEquals(true, result.contains("005"));
        assertEquals(false, result.contains("007"));
    }
}
