package bgu.spl.mics.application.passiveObjects;

import java.util.*;
/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {
	private static Squad instance = new Squad();
	private Map<String, Agent> agents;

	private Squad() {
		instance = null;
		agents = new TreeMap<>();
	}

	/**
	 * Retrieves the single instance of this class.
	 */

	public static Squad getInstance() {
		return instance;
	}

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 *
	 * @param agents Data structure containing all data necessary for initialization
	 *               of the squad.
	 */
	public void load(Agent[] agents) {
		for (int i = 0; i < agents.length; i++)
			this.agents.put(agents[i].getSerialNumber(), agents[i]);

	}

	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials) {
		for (int i = 0; i < serials.size(); i++) {
			Agent ToRelease = agents.get(serials.get(i));
			if (ToRelease != null)
				ToRelease.release();
		}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 *
	 * @param time milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time) {
		try {
			Thread.sleep(time*100);
		} catch (InterruptedException e) {
		}
		releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 *
	 * @param serials the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials) {
		Collections.sort(serials);
		boolean findAllAgents = true;

		//Check if all the agents is in the agents map
		for (int i = 0; i < serials.size() & findAllAgents; i++) {
			Agent agent = this.agents.get(serials.get(i));
			String serialsNum = serials.get(i);
			findAllAgents = this.agents.containsKey(serialsNum);
			}


		if (findAllAgents) {
			for (int i = 0; i < serials.size() & findAllAgents; i++) {
				Agent agent = this.agents.get(serials.get(i));
				agent.acquire();
			}

		}
		return findAllAgents;
	}

	/**
	 * gets the agents names
	 *
	 * @param serials the serial numbers of the agents
	 * @return a list of the names of the agents with the specified serials.
	 */
	public List<String> getAgentsNames(List<String> serials) {
		List<String> AgentsNames = new LinkedList<>();
		for (int i = 0; i < serials.size(); i++) {
			AgentsNames.add(this.agents.get(serials.get(i)).getName());
		}
		return AgentsNames;
	}

}
