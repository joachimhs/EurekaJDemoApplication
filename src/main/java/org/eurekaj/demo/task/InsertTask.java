package org.eurekaj.demo.task;

import org.eurekaj.demo.derby.DerbyEnvironment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: jhs
 * Date: 2/25/11
 * Time: 1:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class InsertTask implements Runnable{
    private DerbyEnvironment derbyEnvironment;

    public InsertTask(DerbyEnvironment derbyEnvironment) {
        this.derbyEnvironment = derbyEnvironment;
    }




    private void insertStatIntoDatabase(String statName, Double statValue) throws SQLException {
        String sql = "insert into Statistics(StatisticName, StatisticValue, StatisticTimestamp) values(?, ?, ?)";

        PreparedStatement preparedStatement = derbyEnvironment.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, statName);
        preparedStatement.setDouble(2, statValue);
        preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
        preparedStatement.executeUpdate();
    }

    public void run() {
        try {
            if (derbyEnvironment != null) {
                for (int i = 0; i < 1000; i++) {
                    insertStatIntoDatabase("Average Execution Time", Math.random() * Math.random() * 100);
                    insertStatIntoDatabase("Calls Per Interval", Math.random() * Math.random() * 100);
                }
                System.out.println("Inserted 1000 stats into table Statistics");
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
