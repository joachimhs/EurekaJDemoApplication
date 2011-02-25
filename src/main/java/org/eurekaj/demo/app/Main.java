package org.eurekaj.demo.app;

import org.eurekaj.demo.derby.DerbyEnvironment;
import org.eurekaj.demo.task.PerformTask;

/**
 * Created by IntelliJ IDEA.
 * User: jhs
 * Date: 2/25/11
 * Time: 1:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String[] args) {
        DerbyEnvironment derbyEnvironment = new DerbyEnvironment();
        derbyEnvironment.initializeDatabase();

        PerformTask task = new PerformTask(derbyEnvironment);

        while(true) {
            task.doWork();
        }
    }
}
