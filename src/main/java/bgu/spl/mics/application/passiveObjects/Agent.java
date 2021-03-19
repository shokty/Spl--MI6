package bgu.spl.mics.application.passiveObjects;


/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Agent{
	private  String name;
	private  String serialNumber;
	private  Boolean available = true;
	// the num of thread we allowed to be in the same time at this agent
	private final int numOfThreads =1;
	// the num of thread is acquire this agent
	private int usedThreads;
	/**
	 * Sets the serial number of an agent.
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * Retrieves the serial number of an agent.
	 * <p>
	 * @return The serial number of an agent.
	 */

	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * Sets the name of the agent.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retrieves the name of the agent.
	 * <p>
	 * @return the name of the agent.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Retrieves if the agent is available.
	 * <p>
	 * @return if the agent is available.
	 */
	public boolean isAvailable() {
		return available;
	}

	/**
	 * Acquires an agent.
	 */
	public synchronized void acquire() {
		//isEligibleToAcquire()  - isAvailable()
		//isAgentAvailable() - there is no one use in this agent
		while(!(isAgentAvailable() && isEligibleToAcquire())) {
			try {
				//wait until you get the agent
				wait();
			} catch(InterruptedException e) {
			}
		}
		available = false;
		usedThreads++;
	}

	/**
	 * Releases an agent.
	 */
	public synchronized void release() {
		// if there is a thread that use in this agent we will release it and notifyAll thread is released
		if (usedThreads > 0) {
			usedThreads--;
			notifyAll();
		}
		available = true;
	}
	//this function check if the number of thread allowed is smaller then the use of threads
	private boolean isAgentAvailable() {
		return usedThreads < numOfThreads;
	}

	private boolean isEligibleToAcquire() {
		return isAvailable();
	}
}

