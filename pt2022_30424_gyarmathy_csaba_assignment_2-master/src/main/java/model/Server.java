package model;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    private int waitingPeriod;
    private int finalTime;

    public Server(int finalTime) {
        this.finalTime = finalTime;
        tasks = new ArrayBlockingQueue<Task>(20);
        waitingPeriod = 0;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks);
    }

    public void setTasks(BlockingQueue<Task> tasks) {
        this.tasks = tasks;
    }

    public int getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setWaitingPeriod(int waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

    public void addTask(Task task) {
        tasks.add(task);
        waitingPeriod += task.getServiceTime();
    }

    @Override
    public void run() {
        int currentTime = 0;
        while (true) {
            Task firstTask = tasks.peek();
            if(firstTask == null){
                continue;
            }
            int serviceTime = firstTask.getServiceTime();
            if (serviceTime != 1) {
                firstTask.setServiceTime(serviceTime - 1);
                setWaitingPeriod(getWaitingPeriod() - 1);
            } else {
                try {
                    tasks.take();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            currentTime++;

            if (currentTime == finalTime)
                break;
        }
    }
}
