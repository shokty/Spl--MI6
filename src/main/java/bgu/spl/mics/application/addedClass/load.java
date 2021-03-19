package bgu.spl.mics.application.addedClass;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.application.passiveObjects.Squad;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.*;
import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * this class for load all the information from the input json that was given
 */

public class load {
    private static List<Thread> threadListEvents;
    private static List<Thread> threadListSub;
    private static String jsonPath;
    private FileReader reader;
    private JsonElement jsonElement;
    private static JsonObject jsonObject;
    private static int numOfpublisher;
    private static int numOfsubscriber;
    private static int time;

    public load(String filePath) {
        this.jsonPath = filePath;
        threadListEvents = new LinkedList<Thread>();
        threadListSub = new LinkedList<Thread>();
        try {
            this.reader = new FileReader(jsonPath);
            this.jsonElement = new JsonParser().parse(reader);
            jsonObject = jsonElement.getAsJsonObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // initialization of num of publisher and num of subscriber
        numOfpublisher = 0;
        numOfsubscriber = 0;
    }

    public static void loadAll(String filePath) {
        jsonPath = filePath;
        threadListEvents = new LinkedList<Thread>();
        threadListSub = new LinkedList<Thread>();

        try {
            FileReader reader = new FileReader(jsonPath);
            JsonElement jsonElement = new JsonParser().parse(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // initialization of num of publisher and num of subscriber
        numOfpublisher = 0;
        numOfsubscriber = 0;

        //load all by calling to a function
        inventory();
        squad();
        service();


        //Call the initializeThreads class after get the data from the json
        initializeThreads initialize = new initializeThreads(threadListSub , threadListEvents);
        initialize.initializeThreadsActivate();

    }

    private static void inventory() {
        //search for "inventory" at the file
        JsonArray gadgets = jsonObject.get("inventory").getAsJsonArray();
        //Create an Array of String and in the end insert it to the inventory
        String[] k = new String[gadgets.size()];
        int i = 0;
        // insert every gadget to array
        for (JsonElement element : gadgets) {
            String gadget = element.getAsString();
            k[i] = gadget;
            i++;
        }
        Inventory inv = Inventory.getInstance();
        inv.load(k); //load the array
    }

    private static void squad() {
        //search for "squad" at the file
        JsonArray agentsList = jsonObject.get("squad").getAsJsonArray();
        Iterator<JsonElement> iterAgents = agentsList.iterator();
        Agent[] agentList = new Agent[agentsList.size()];
        int AgentNum = 0;
        while (iterAgents.hasNext()) {
            JsonObject agent = (JsonObject) iterAgents.next();
            String name = agent.get("name").getAsString();
            String serialNumber = agent.get("serialNumber").getAsString();
            Agent agentToInsert = new Agent();
            agentToInsert.setName(name);
            agentToInsert.setSerialNumber(serialNumber);
            agentList[AgentNum] = agentToInsert;
            AgentNum++;
        }
        Squad squ = Squad.getInstance();
        squ.load(agentList);
    }

    private static void service() {
        //search for "services" at the file
        JsonObject servicesJsonObject = jsonObject.get("services").getAsJsonObject();
        int numOfM = servicesJsonObject.get("M").getAsInt();
        int numOfMoneypenny = servicesJsonObject.get("Moneypenny").getAsInt();
        JsonElement intelligenceJsonObject = servicesJsonObject.get("intelligence");
        int numOfIntelligence = IntelligenceNum(intelligenceJsonObject);
        time = servicesJsonObject.get("time").getAsInt();
        numOfpublisher = 2;
        numOfsubscriber = numOfMoneypenny + numOfM + numOfIntelligence + 1;
        createMThread(numOfM);
        createMoneypennyThread(numOfMoneypenny);
        IntelligenceCreate(intelligenceJsonObject);
        createAQ();
        createTimeService();
    }
    //this function return number of Intelligence Threads
    private static int IntelligenceNum(JsonElement intelligenceArry) {
        JsonArray IntelligenceListArry = intelligenceArry.getAsJsonArray();
        return (IntelligenceListArry.size());
    }

    //this function get number of Intelligence Threads and create them
    private static void IntelligenceCreate(JsonElement intelligenceArry) {
        int counterIntelligence = 1;
        JsonArray IntelligenceListArry = intelligenceArry.getAsJsonArray();
        for (JsonElement element : IntelligenceListArry) {
            List<MissionInfo> missionsList = extractMissions(getAJsonMissionsAsINeed(element));
            createIntelligence(counterIntelligence, missionsList);
            counterIntelligence++;
        }
    }

    private static JsonArray getAJsonMissionsAsINeed(JsonElement element) {
        JsonObject elementJsonObject = element.getAsJsonObject();
        JsonElement missionsJsonElement = elementJsonObject.get("missions");
        JsonArray missions = missionsJsonElement.getAsJsonArray();
        return (missions);
    }

    //this function get json Array of all the missions and extract every mission
    private static List<MissionInfo> extractMissions(JsonArray missionsArry) {
        List<MissionInfo> missionInfoList = new LinkedList<>();
        JsonArray MissionsList = missionsArry.getAsJsonArray();
        for (JsonElement element : MissionsList) {
            JsonObject mission = (JsonObject) element;
            MissionInfo missionToInsert = extractMissionInfo(mission);
            missionInfoList.add(missionToInsert);
        }
        return missionInfoList;
    }

    //this function get json Obj of the mission and extract all the info we need for build MissionInfo obj
    private static MissionInfo extractMissionInfo(JsonObject missions) {
        List<String> serialAgentsNumbers = extractSerialAgentsNumbers(missions.get("serialAgentsNumbers").getAsJsonArray());
        String gadget = missions.get("gadget").getAsString();
        int duration = missions.get("duration").getAsInt();
        String missionName = missions.get("name").getAsString();
        int timeIssued = missions.get("timeIssued").getAsInt();
        int timeExpired = missions.get("timeExpired").getAsInt();
        return (createMission(gadget, duration, timeIssued ,timeExpired, missionName,serialAgentsNumbers));

    }
    //this function get obj required for the mission and build MissionInfo obj
    private static MissionInfo createMission(String gadget, int duration, int timeIssued , int timeExpired,  String missionName, List<String> serialAgentsNumbers) {
        MissionInfo missionToInsert = new MissionInfo();
        missionToInsert.setSerialAgentsNumbers(serialAgentsNumbers);
        missionToInsert.setMissionName(missionName);
        missionToInsert.setGadget(gadget);
        missionToInsert.setDuration(duration);
        missionToInsert.setTimeIssued(timeIssued);
        missionToInsert.setTimeExpired(timeExpired);
        return (missionToInsert);
    }

    //this function get jsonArray of serial Agents Numbers of specific mission and return the array of it
    private static List<String> extractSerialAgentsNumbers(JsonArray serialAgentsNumbers) {
        List<String> serialAgentsNumbersArry = new LinkedList<>();
        for (JsonElement serialAgentsNumber : serialAgentsNumbers) {
            serialAgentsNumbersArry.add(serialAgentsNumber.getAsString());
        }
        return serialAgentsNumbersArry;
    }

    //this function get number of M Threads and create them
    private static void createMThread(int num) {
        for (int i = 0; i < num; i++) {
            M mToInsert = new M(i + 1);
            Thread toinsert = new Thread(mToInsert);
            toinsert.setName("M" + (i + 1));
            threadListSub.add(toinsert);
        }
    }
    //this function get number of Moneypenny Threads and create them
    private static void createMoneypennyThread(int num) {
        int nameCounter = 1;
        for (int i = 0; i < num; i++) {
            Moneypenny moneyPennyToInsert = new Moneypenny(nameCounter);
            Thread threadToInsert = new Thread(moneyPennyToInsert);
            threadToInsert.setName("MoneyPenny" + (nameCounter));
            threadListSub.add(threadToInsert);
            nameCounter++;
            }

        }
    //this function create Q thread
    private static void createAQ() {
        Q QToInsert = new Q();
        Thread QThread = new Thread(QToInsert);
        QThread.setName("Q");
        threadListSub.add(QThread);
    }
    //this function create TimeService thread
    private static void createTimeService() {
        TimeService timeService = new TimeService(time);
        Thread timeServiceThread = new Thread(timeService);
        threadListEvents.add(timeServiceThread);
        timeServiceThread.setName("TimeService");
    }
    //this function create one Intelligence thread
    private static void createIntelligence(int counterIntelligence, List<MissionInfo> missionsList) {
        Intelligence intelligenceToInsert = new Intelligence(missionsList, counterIntelligence);
        Thread intelligenceToInsertThreads = new Thread(intelligenceToInsert);
        threadListSub.add(intelligenceToInsertThreads);
        intelligenceToInsertThreads.setName("Intelligence " + counterIntelligence);
    }


    public static List<Thread> getThreadList() {
        return threadListSub;
    }


}
