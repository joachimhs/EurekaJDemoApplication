package org.eurekaj.demo.task;

import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by IntelliJ IDEA.
 * User: jhs
 * Date: 3/8/11
 * Time: 7:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskKeeper {
    private InsertTask insertTask;
    private ScheduledFuture futureTask;

    public TaskKeeper(InsertTask insertTask, ScheduledFuture futureTask) {
        this.insertTask = insertTask;
        this.futureTask = futureTask;
    }

    public InsertTask getInsertTask() {
        return insertTask;
    }

    public ScheduledFuture getFutureTask() {
        return futureTask;
    }
}
