package org.example;

import java.util.Date;
import java.util.concurrent.*;

public class Worker implements Runnable{

    private long expectedInterval;
    private long schedulingFrequency;
    private boolean isThreadRunnable;
    private MyJob job;
    public Worker(String jobId, long expectedInterval, long schedulingFrequency,String scriptPath) {
        this.expectedInterval = expectedInterval;
        this.schedulingFrequency = schedulingFrequency;
        this.job = new MyJob(jobId,scriptPath);
        isThreadRunnable = true;
    }

    public long getExpectedInterval() {
        return expectedInterval;
    }

    public long getSchedulingFrequency() {
        return schedulingFrequency;
    }

    public MyJob getJob() {
        return job;
    }

    public void setIsThreadRunnable(boolean isThreadRunnable){
        this.isThreadRunnable = isThreadRunnable;
    }

    public void stopRunningThread(){
        setIsThreadRunnable(false);
    }
    @Override
    public void run() {
        while (isThreadRunnable){
            ExecutorService executor = Executors.newSingleThreadExecutor();
            MyJob myJob = getJob();
            Future<Integer> future = executor.submit(myJob);
            try {
                long startTime = System.currentTimeMillis();
                System.out.println("Job: "+myJob.getJobId()+" Started..");
                future.get(getExpectedInterval(), TimeUnit.MILLISECONDS);
                long endTime = System.currentTimeMillis();
                long executionTime = endTime-startTime;
                System.out.println("Job: "+myJob.getJobId()+" Finished!");
                System.out.println("Job: "+myJob.getJobId()+" executed in "+executionTime+ " Milliseconds");
                Thread.sleep(getSchedulingFrequency()-executionTime);
            } catch (TimeoutException | InterruptedException | ExecutionException e) {
                future.cancel(true);
                System.out.println("Job: "+myJob.getJobId()+" Terminated at "+new Date()+" due to Timeout");
                throw new RuntimeException(e);
            }
            executor.shutdownNow();
        }
        setIsThreadRunnable(true);
    }
}
