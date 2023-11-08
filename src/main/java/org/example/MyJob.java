package org.example;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.*;

public class MyJob implements Callable<Integer> {
    private String jobId;
    private String scriptPath;
    private Queue<Long>last5StartingTimes;

    public MyJob(String jobId, String scriptPath) {
        this.jobId = jobId;
        this.scriptPath = scriptPath;
        last5StartingTimes = new LinkedList<>();
    }

    public Queue<Long> getLast5StartingTimes() {
        return last5StartingTimes;
    }

    public void addStartTime(long time) {
        this.last5StartingTimes.add(time);
        while (last5StartingTimes.size()>5)last5StartingTimes.poll();
    }

    public String getJobId() {
        return jobId;
    }

    public String getScriptPath() {
        return scriptPath;
    }


    @Override
    public Integer call() throws Exception {
        try {
            last5StartingTimes.add(System.currentTimeMillis());
            String JobId = getJobId();
            String scriptPath = getScriptPath();
            System.out.println(scriptPath);
            System.out.println("Job: "+JobId+" Started at " + new Date());
            Process process = Runtime.getRuntime().exec("cmd /c" + scriptPath);
            int exitCode = process.waitFor();
            System.out.println("Job: "+JobId+" exited with code: " + exitCode +" at " + new Date());
            return exitCode;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
