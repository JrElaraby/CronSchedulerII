package org.example;

import java.io.*;
import java.util.*;

public class MyScheduler {
    Queue<Worker> workers;
    private String jobPath;
    private String fullPath;

    public MyScheduler(String jobPath) {
        workers = new LinkedList<>();
        this.jobPath = jobPath;
        fullPath = System.getProperty("user.dir") + "/src/main/java/org/example";
    }

    public String getJobPath() {
        return jobPath;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void scheduleJobs() {
        parseJobs();
        runJobs();
    }

    private Worker createWorker(String[] jobAttributes) {
        String id = jobAttributes[0];
        long expectedInterval = Long.parseLong(jobAttributes[1]);
        long schedulingFrequency = Long.parseLong(jobAttributes[2]);
        String scriptPath = jobAttributes[3];
        File file = new File(getFullPath(), scriptPath);
        Worker jobWorker = new Worker(id, expectedInterval, schedulingFrequency, file.getAbsolutePath());
        return jobWorker;
    }

    private void parseJobs() {
        try {
            String fullPath = getFullPath();
            File file = new File(fullPath, getJobPath());
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] jobAttributes = line.split(",");
                Worker jobWorker = createWorker(jobAttributes);
                workers.add(jobWorker);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runJobs() {
        Iterator<Worker> iterator = workers.iterator();
        while (iterator.hasNext()) {
            Worker worker = iterator.next();
            new Thread(worker).start();
        }
    }

    public void stopJobs() {
        while (!workers.isEmpty()) {
            Worker worker = workers.poll();
            worker.stopRunningThread();
        }
    }

    public int getRunningJobs() {
        return workers.size();
    }

    public static void main(String[] args) throws InterruptedException {
        MyScheduler myScheduler = new MyScheduler("../../test/Jobs.txt");
        myScheduler.scheduleJobs();
    }
}