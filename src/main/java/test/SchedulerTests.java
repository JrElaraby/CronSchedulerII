package test;
import org.example.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Queue;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.Timeout.ThreadMode;
@Timeout(value = 7, unit = TimeUnit.SECONDS, threadMode = ThreadMode.SEPARATE_THREAD)
public class SchedulerTests {
    static final String testPath=System.getProperty("user.dir")+"/src/main/java/test";
    @Test
    public void testMyJobStandAlone() throws Exception {
        MyJob myJob = new MyJob("dummyId",testPath+"/mainII.sh");
        assertEquals(0,(int)myJob.call());
    }

    @Test
    public void testMyJobStandAloneWhenFileNotFound() throws Exception {
        MyJob myJob = new MyJob("dummyId",testPath+"/NONEXISTENTSCRIPT");
        assertEquals(1,(int)myJob.call());
    }

    @Test
    public void testWorkerTimeOut() {
        Worker worker = new Worker("dummyId",2000,3000,testPath+"/sleep.sh");
        assertThrows(RuntimeException.class, worker::run);
    }

    @Test
    public void testSchedulingFrequency() throws InterruptedException {
        Worker worker = new Worker("dummyId",2000,1000,testPath+"/mainII.sh");
        new Thread(worker).start();
        Thread.sleep(4000);
        worker.stopRunningThread();
        Queue<Long>startTimes = worker.getJob().getLast5StartingTimes();
        long prev = startTimes.poll();
        System.out.println(startTimes);
        boolean flag = true;
        while(!startTimes.isEmpty()){
            long next = startTimes.poll();
            long diff = next - prev;
            if (diff<900 || diff>1100)
                flag=false;
            prev=next;
        }
        assertTrue(flag);
    }

    @Test
    public void testSchedulerNoRunningJobs(){
        MyScheduler myScheduler = new MyScheduler("../../test/Jobs.txt");
        assertEquals(0,myScheduler.getRunningJobs());
    }

    @Test
    public void testSchedulerRunningJobs(){
        MyScheduler myScheduler = new MyScheduler("../../test/Jobs.txt");
        myScheduler.scheduleJobs();
        assertEquals(2,myScheduler.getRunningJobs());
    }
    @Test
    public void testSchedulerStopRunningJobs(){
        MyScheduler myScheduler = new MyScheduler("../../test/Jobs.txt");
        myScheduler.scheduleJobs();
        myScheduler.stopJobs();
        assertEquals(0,myScheduler.getRunningJobs());
    }
}
