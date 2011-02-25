package org.eurekaj.demo.task;

import org.eurekaj.demo.derby.DerbyEnvironment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: jhs
 * Date: 2/25/11
 * Time: 1:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class PerformTask implements Runnable{
    private DerbyEnvironment derbyEnvironment;

    public PerformTask(DerbyEnvironment derbyEnvironment) {
        this.derbyEnvironment = derbyEnvironment;
    }


    private void readStatFromDatabase() throws SQLException {
        //System.out.println("Selecting stat for 'Average executiontime' from Statistics table");
        String sql = "select * from Statistics s where s.StatisticName = ?";
        PreparedStatement preparedStatement = derbyEnvironment.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, "Average executiontime");
        ResultSet rs = preparedStatement.executeQuery();

        int numStats = 0;
        double totalValue = 0d;

        while (rs.next()) {
            numStats++;
            totalValue += rs.getDouble("StatisticValue");
        }

        double averageValue = 0d;
        if (numStats > 0) {
            averageValue = totalValue / numStats;
        }

        System.out.println("There were " + numStats + " statistics fetched with an average value of: " + averageValue);
    }

    private void insertStatIntoDatabase() throws SQLException {
        //System.out.println("Inserting stat for 'Average executiontime' into Statistics table");

        double randomnumber = Math.random() * 100;

        String sql = "insert into Statistics(StatisticName, StatisticValue) values(?, ?)";

        PreparedStatement preparedStatement = derbyEnvironment.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, "Average executiontime");
        preparedStatement.setDouble(2, randomnumber);
        preparedStatement.executeUpdate();
    }

    public void run() {
        try {
            //System.out.println("Performing Task");
            if (derbyEnvironment != null) {
                insertStatIntoDatabase();
                readStatFromDatabase();
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
