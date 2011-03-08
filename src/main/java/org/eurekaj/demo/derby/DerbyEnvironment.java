package org.eurekaj.demo.derby;

import java.sql.*;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by IntelliJ IDEA.
 * User: jhs
 * Date: 2/25/11
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class DerbyEnvironment {
    private static String driverName = "org.apache.derby.jdbc.EmbeddedDriver";
    private static String derbyProtocol = "jdbc:derby:";
    private static String derbyDBName = "EurekaJDemoDB";
    private Connection connection;
    private static String CREATE_STATISTICS_TABLE_SQL = "create table Statistics(StatisticID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT StatisticID_PK PRIMARY KEY, StatisticName char(50) NOT NULL, StatisticValue double, StatisticTimestamp timestamp)";
    private static String CREATE_AGGREGATED_STATISTICS_TABLE_SQL = "create table AggregatedStatistics(AggregatedStatisticID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1,INCREMENT BY 1) CONSTRAINT AggregatedStatisticID_PK PRIMARY KEY, StatisticName char(50) NOT NULL, AggregatedStatisticValue double, AggregatedStatisticTimeperiod BIGINT NOT NULL, UNIQUE (StatisticName, AggregatedStatisticTimeperiod))";
    private int blockingDelay = 0;
    private int nonBlockingDelay = 0;
    private static final int MAX_DELAY = 20000;
    private int numInsertsPerRun = 25;
    private static final int MAX_INSERTS = 250;
    private long numberOfRecordsInStatisticsDatabase = 0;

    public DerbyEnvironment() {

    }

    public void initializeDatabase() {

        try {
            Class.forName(driverName).newInstance();
            connection = DriverManager.getConnection(derbyProtocol + "EurekaJDemoDB;create=true", new Properties());
            createDatabaseIfNotCreated();
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public int getBlockingDelay() {
        return blockingDelay;
    }

    public void setBlockingDelay(int blockingDelay) {
        if (blockingDelay > MAX_DELAY) {
            blockingDelay = MAX_DELAY;
        }

        if (blockingDelay < 0) {
            blockingDelay = 0;
        }
        this.blockingDelay = blockingDelay;
    }

    public int getNonBlockingDelay() {
        return nonBlockingDelay;
    }

    public void setNonBlockingDelay(int nonBlockingDelay) {
        if (nonBlockingDelay > MAX_DELAY) {
            nonBlockingDelay = MAX_DELAY;
        }

        if (nonBlockingDelay < 0) {
            nonBlockingDelay = 0;
        }
        this.nonBlockingDelay = nonBlockingDelay;
    }

    public int getNumInsertsPerRun() {
        return numInsertsPerRun;
    }

    public void setNumInsertsPerRun(int numInsertsPerRun) {
        if (numInsertsPerRun > MAX_INSERTS) {
            numInsertsPerRun = MAX_INSERTS;
        }

        if (numInsertsPerRun < 0) {
            numInsertsPerRun = 0;
        }
        this.numInsertsPerRun = numInsertsPerRun;
    }

    public long getNumberOfRecordsInStatisticsDatabase() {
        return numberOfRecordsInStatisticsDatabase;
    }

    public void setNumberOfRecordsInStatisticsDatabase(long numberOfRecordsInStatisticsDatabase) {
        this.numberOfRecordsInStatisticsDatabase = numberOfRecordsInStatisticsDatabase;
    }

    private void createDatabaseIfNotCreated() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getTables(null, null, "STATISTICS", null);
        if (!rs.next()) {
            System.out.println("Creating Database Table 'Statistics'");
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_STATISTICS_TABLE_SQL);
            System.out.println("Statistics Table Created!");
        }

        rs = metaData.getTables(null, null, "AGGREGATEDSTATISTICS", null);
        if (!rs.next()) {
            System.out.println("Creating Database Table 'AggregatedStatistics'");
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_AGGREGATED_STATISTICS_TABLE_SQL);
            System.out.println("AggregatedStatistics Table Created!");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
