package bll;

import model.Server;
import model.Task;
import view.SimulationFrame;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable {

    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int numberOfServer;
    public int numberOfClients;
    public int minArrivalTime;
    public int maxArrivalTime;
    private Scheduler scheduler;
    private static SimulationFrame frame = new SimulationFrame();
    private List<Task> generatedTasks = new ArrayList<>();

    private static float averageWaitTime = 0;
    private static float averageServiceTime = 0;
    private static int peakNrClients = 0;

    private static int peakTime = 0;
    PrintWriter printWriter = new PrintWriter("Log.txt");
    public SimulationManager(int timeLimit, int maxProcessingTime, int minProcessingTime, int numberOfServer, int numberOfClients, int minArrivalTime, int maxArrivalTime) throws FileNotFoundException {
        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.numberOfServer = numberOfServer;
        this.numberOfClients = numberOfClients;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.scheduler = new Scheduler(frame, numberOfServer, numberOfClients, timeLimit);
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public SimulationFrame getFrame() {
        return frame;
    }

    public void setFrame(SimulationFrame frame) {
        this.frame = frame;
    }

    public static void main(String[] args) {
        frame.setVisible(true);
    }


    public void generateRandomTasks(int n) {
        Random random = new Random();
        System.out.println(minArrivalTime);
        for (int i = 0; i < n; i++) {
            Task task = new Task(i + 1, random.nextInt(maxArrivalTime - minArrivalTime) + minArrivalTime, random.nextInt(maxProcessingTime - minProcessingTime) + minProcessingTime);
            generatedTasks.add(task);
        }
    }

    @Override
    public void run() {
        generateRandomTasks(numberOfClients);
        int currentTime = 0;
        while (currentTime <= timeLimit) {
            int j = 0;
            while (j < generatedTasks.size()) {
                if (generatedTasks.get(j).getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(generatedTasks.get(j));
                    calculateAverageServiceTime(generatedTasks.get(j).getServiceTime());
                    generatedTasks.remove(j);
                } else {
                    j++;
                }
            }

            try {
                displayQueues(scheduler.getServerList(), currentTime, printWriter);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            peakHour(scheduler.getServerList(), currentTime);
            currentTime++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        peakHour(scheduler.getServerList(), currentTime);
        averageWaitTime = averageWaitTime / (float) numberOfClients;
        averageServiceTime = averageServiceTime / (float) numberOfClients;
        frame.updateTextField("Simulation results: \n");
        frame.updateTextField("Peak hour: " + peakTime + "\n");
        frame.updateTextField("Peak number of clients: " + peakNrClients + "\n");
        frame.updateTextField("Average waiting time: " + averageWaitTime + "\n");
        frame.updateTextField("Average service time: " + averageServiceTime + "\n");
        printWriter.println("Simulation results: " );
        printWriter.println("Peak hour: " + peakTime);
        printWriter.println("Peak number of clients: " + peakNrClients );
        printWriter.println("Average waiting time: " + averageWaitTime );
        printWriter.println("Average service time: " + averageServiceTime );
        printWriter.close();
    }

    public void displayQueues(List<Server> serverList, int currentTime, PrintWriter printWriter) throws FileNotFoundException {
        int queueNr = 1;
        frame.updateTextField("Time: " + currentTime + "\n");
        printWriter.println("Time: " + currentTime);
        for (Server server : serverList) {
            ArrayList<Task> taskList = server.getTasks();
            if (taskList.isEmpty()) {
                frame.updateTextField("Queue " + queueNr + " is empty\n");
                printWriter.print("Queue " + queueNr + " is empty\n");
            } else {
                frame.updateTextField("Queue " + queueNr +": ");
                printWriter.print("Queue " + queueNr +": ");
                for (Task task : taskList) {
                    frame.updateTextField("(" + task.getID() + ", ");
                    frame.updateTextField(task.getArrivalTime() + ", ");
                    frame.updateTextField(task.getServiceTime() + ")   ");
                    printWriter.print("(" + task.getID() + ", "+ task.getArrivalTime() + ", " + task.getServiceTime() + ")   ");
                }
                frame.updateTextField("\n");
                printWriter.println("");
            }
            queueNr++;
        }
        frame.updateTextField("\n");
        printWriter.println("");
    }

    public void peakHour(List<Server> serverList, int currentTime) {
        int peakClients = 0;
        for (Server server : serverList) {
            peakClients += server.getTasks().size();
        }
        if (peakClients > peakNrClients) {
            peakTime = currentTime;
            peakNrClients = peakClients;
        }
    }

    public static void calculateAverageWaitingTime(int averageWaitingTime) {
        averageWaitTime += averageWaitingTime;
    }

    public static void calculateAverageServiceTime(int averageServicingTime) {
        averageServiceTime += averageServicingTime;
    }
}
