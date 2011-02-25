package org.eurekaj.demo.app;

import org.eurekaj.demo.derby.DerbyEnvironment;
import org.eurekaj.demo.task.PerformTask;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: jhs
 * Date: 2/25/11
 * Time: 1:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {
    private ScheduledThreadPoolExecutor threadPool;
    private int THREADPOOLSIZE = 25;

    public Main() {
        threadPool = new ScheduledThreadPoolExecutor(THREADPOOLSIZE);
    }

    public void startExecuting() {
        DerbyEnvironment derbyEnvironment = new DerbyEnvironment();
        derbyEnvironment.initializeDatabase();
        long millisStart = System.currentTimeMillis();

        while(true) {
            while (threadPool.getActiveCount() >= THREADPOOLSIZE) {
                System.out.println("ThreadPool Full. Sleeping. ");
                doSleep(1000);
            }


            PerformTask task = new PerformTask(derbyEnvironment);
            threadPool.scheduleAtFixedRate(task, 0, 5000, TimeUnit.MILLISECONDS);
        }
    }

    public void doSleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void main(String[] args) {
        Main main  = new Main();
        main.startExecuting();
    }
}
