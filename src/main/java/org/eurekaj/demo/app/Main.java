package org.eurekaj.demo.app;

import org.eurekaj.demo.derby.DerbyEnvironment;
import org.eurekaj.demo.task.CalculateAverageTask;
import org.eurekaj.demo.task.InsertTask;

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

        InsertTask task = new InsertTask(derbyEnvironment);
        threadPool.scheduleAtFixedRate(task, 0, 2500, TimeUnit.MILLISECONDS);

        CalculateAverageTask calculateAverageTask = new CalculateAverageTask(derbyEnvironment);
        threadPool.scheduleAtFixedRate(calculateAverageTask, 15000, 15000, TimeUnit.MILLISECONDS);
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
