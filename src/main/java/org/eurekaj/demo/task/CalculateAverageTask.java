package org.eurekaj.demo.task;

import org.eurekaj.demo.derby.DerbyEnvironment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Created by IntelliJ IDEA.
 * User: jhs
 * Date: 2/28/11
 * Time: 8:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class CalculateAverageTask implements Runnable {
    private DerbyEnvironment derbyEnvironment;

    public CalculateAverageTask(DerbyEnvironment derbyEnvironment) {
        this.derbyEnvironment = derbyEnvironment;
    }

    private Double readStatFromDatabase(String statName, long msFrom, long msTo) throws SQLException {
        //System.out.println("Selecting stat for 'Average executiontime' from Statistics table");
        String sql = "select avg(s.StatisticValue) as AverageStatisticValue from Statistics s where s.StatisticName = ? and s.StatisticTimestamp between ? and ?";

        PreparedStatement preparedStatement = derbyEnvironment.getConnection().prepareStatement(sql);
        preparedStatement.setString(1, statName);
        preparedStatement.setTimestamp(2, new Timestamp(msFrom));
        preparedStatement.setTimestamp(3, new Timestamp(msTo));

        Double averageValue = null;
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            averageValue = rs.getDouble("AverageStatisticValue");
        }

        return averageValue;
    }

    private void insertStatIntoAggregatedStatistic(String statisticName, Double aggregatedStatisticValue, Long timeperiod) throws SQLException {
        String selectSql = "select * from AggregatedStatistics as aggStat where aggStat.statisticName = ? and aggStat.AggregatedStatisticTimeperiod = ?";
        String insertSql = "insert into AggregatedStatistics(statisticName, AggregatedStatisticValue, AggregatedStatisticTimeperiod) values(?, ?, ?)";
        String updateSql = "update AggregatedStatistics as aggStat set statisticName.AggregatedStatisticValue = ? where aggStat.statisticName = ? and aggStat.AggregatedStatisticTimeperiod = ?";

        PreparedStatement preparedStatement = derbyEnvironment.getConnection().prepareStatement(selectSql);
        preparedStatement.setString(1, statisticName);
        preparedStatement.setLong(2, timeperiod);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next()) {
            //Update
            PreparedStatement updatePs = derbyEnvironment.getConnection().prepareStatement(updateSql);
            updatePs.setDouble(1, aggregatedStatisticValue);
            updatePs.setString(2, statisticName);
            updatePs.setLong(3, timeperiod);
            updatePs.executeUpdate();

            System.out.println("Updated Aggregated Stat: " + statisticName + " at: " + timeperiod + " with value: " + aggregatedStatisticValue);
        } else {
            //Insert
            PreparedStatement insertPs = derbyEnvironment.getConnection().prepareStatement(insertSql);
            insertPs.setString(1, statisticName);
            insertPs.setDouble(2, aggregatedStatisticValue);
            insertPs.setLong(3, timeperiod);
            insertPs.executeUpdate();

            System.out.println("Inserted Aggregated Stat: " + statisticName + " at: " + timeperiod + " with value: " + aggregatedStatisticValue);
        }
    }

    public void run() {
        long msTo = ((System.currentTimeMillis() - 15000) / 15000) * 15000;
        long msFrom = msTo - 15000;

        try {
            if (derbyEnvironment != null) {
                Double average = readStatFromDatabase("Average Execution Time", msFrom, msTo);
                if (average != null) {
                    System.out.println("Average Execution Time between: " + msFrom + " and: " + msTo + " is: " + average);
                    insertStatIntoAggregatedStatistic("Average Execution Time", average, msFrom);
                }

                average = readStatFromDatabase("Calls Per Interval", msFrom, msTo);
                if (average != null) {
                    System.out.println("Calls Per Interval between: " + msFrom + " and: " + msTo + " is: " + average);
                    insertStatIntoAggregatedStatistic("Calls Per Interval", average, msFrom);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
