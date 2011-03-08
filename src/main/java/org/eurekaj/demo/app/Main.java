package org.eurekaj.demo.app;

import org.eurekaj.demo.derby.DerbyEnvironment;
import org.eurekaj.demo.gui.DemoJFrame;
import org.eurekaj.demo.task.CalculateAverageTask;
import org.eurekaj.demo.task.InsertTask;
import org.eurekaj.demo.task.NumStatisticsTask;
import org.eurekaj.demo.task.TaskKeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
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
    private ScheduledThreadPoolExecutor insertTaskPool;
    private ScheduledThreadPoolExecutor calculateTaskPool;
    private int THREADPOOLSIZE = 25;
    private List<TaskKeeper> insertTaskList = new ArrayList<TaskKeeper>();
    private int insertTaskSize = 1;
    private  DerbyEnvironment derbyEnvironment;
    private DemoJFrame demoJFrame;

    public Main() {
        derbyEnvironment = new DerbyEnvironment();
        insertTaskPool = new ScheduledThreadPoolExecutor(THREADPOOLSIZE);
        calculateTaskPool = new ScheduledThreadPoolExecutor(5);
        demoJFrame = new DemoJFrame("EurekaJ Demo App", this);
        startExecuting();
    }

    public int getInsertTaskSize() {
        return insertTaskSize;
    }

    public void setInsertTaskSize(int insertTaskSize) {
        int delta = insertTaskSize - this.insertTaskSize;

        if (insertTaskSize > 25) {
            insertTaskSize = 25;
        }

        this.insertTaskSize = insertTaskSize;

        if (delta > 0) {
            increaseExecutingInsertTasks();
        } else if (delta < 0) {
            reduceExecutingInsertTasks();
        } //else no changes
    }

    public void startExecuting() {
        derbyEnvironment.initializeDatabase();

        scheduleNewInsertThread();

        CalculateAverageTask calculateAverageTask = new CalculateAverageTask(derbyEnvironment);
        calculateTaskPool.scheduleAtFixedRate(calculateAverageTask, 15000, 15000, TimeUnit.MILLISECONDS);

        NumStatisticsTask numStatisticsTask = new NumStatisticsTask(derbyEnvironment, demoJFrame);
        calculateTaskPool.scheduleAtFixedRate(numStatisticsTask, 0, 5000, TimeUnit.MILLISECONDS);
    }

    private void reduceExecutingInsertTasks() {
        List<TaskKeeper> tasksForRemoval = new ArrayList<TaskKeeper>();
        for (int i = insertTaskSize; i < insertTaskList.size(); i++) {
            TaskKeeper taskKeeper = insertTaskList.get(i);
            taskKeeper.getFutureTask().cancel(false);
            insertTaskPool.remove(taskKeeper.getInsertTask());

            tasksForRemoval.add(taskKeeper);
        }

        insertTaskList.removeAll(tasksForRemoval);

        System.out.println("Reduced the number of executing tasks to: " + insertTaskList.size() + ":" + insertTaskSize);
    }

    private void increaseExecutingInsertTasks() {
        int increaseBy = insertTaskSize - insertTaskList.size();
        for (int i = 0; i < increaseBy; i++) {
            scheduleNewInsertThread();
        }

        System.out.println("Increased the number of executing tasks by : " + increaseBy + " to: " + insertTaskList.size() + ":" + insertTaskSize);
    }

    private void scheduleNewInsertThread() {
        InsertTask task = new InsertTask(derbyEnvironment);
        ScheduledFuture futureTask = insertTaskPool.scheduleAtFixedRate(task, 0, 2500, TimeUnit.MILLISECONDS);
        insertTaskList.add(new TaskKeeper(task, futureTask));
    }

    public DerbyEnvironment getDerbyEnvironment() {
        return derbyEnvironment;
    }

    public static void main(String[] args) {
        Main main  = new Main();
        main.startExecuting();
    }
}
