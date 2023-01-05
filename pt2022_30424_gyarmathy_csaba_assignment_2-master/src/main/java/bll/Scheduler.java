package bll;

import model.Server;
import model.Task;
import view.SimulationFrame;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private int maxNrServers;
    private final int currentTime;
    private final int maxNrOfClients;
    private int finalTime;
    SimulationFrame frame;
    private List<Server> serverList = new ArrayList<>();
    private int minWaitingPeriod;

    public Scheduler(SimulationFrame simulationFrame, int numberOfServer, int numberOfClients, int timeLimit) {
        currentTime = 0;
        for (int i = 0; i < numberOfServer; i++) {
            Server server = new Server(finalTime);
            Thread thread = new Thread(server);
            thread.start();
            serverList.add(server);
        }
        this.maxNrOfClients = numberOfClients;
        this.finalTime = timeLimit;
        this.maxNrServers = numberOfServer;
        this.frame = simulationFrame;
    }

    public List<Server> getServerList() {
        return serverList;
    }

    public void setServerList(List<Server> serverList) {
        this.serverList = serverList;
    }

    public int getMaxNrServers() {
        return maxNrServers;
    }

    public void setMaxNrServers(int maxNrServers) {
        this.maxNrServers = maxNrServers;
    }

    public int getMinWaitingPeriod() {
        return minWaitingPeriod;
    }

    public void setMinWaitingPeriod(int minWaitingPeriod) {
        this.minWaitingPeriod = minWaitingPeriod;
    }

    public void dispatchTask(Task task) {
        minWaitingPeriod = 1000000;
        int index = 0;
        int i = 0;
        for (Server server : serverList) {
            if (server.getWaitingPeriod() < minWaitingPeriod) {
                minWaitingPeriod = server.getWaitingPeriod();
                index = i;
            }
            i++;
        }
        serverList.get(index).addTask(task);
        int waitingPeriod = serverList.get(index).getWaitingPeriod();
        SimulationManager.calculateAverageWaitingTime(waitingPeriod - task.getServiceTime());
        serverList.get(index).setWaitingPeriod(waitingPeriod + task.getServiceTime());
    }

    public int getTotalWaitTime(){
        int total = 0;
        for(Server server : serverList){
            total += server.getWaitingPeriod();
        }
        return total;
    }


}
