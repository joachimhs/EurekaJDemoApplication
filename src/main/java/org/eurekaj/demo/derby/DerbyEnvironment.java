package org.eurekaj.demo.derby;

import java.sql.*;
import java.util.Properties;

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
    private static String CREATE_STATISTICS_TABLE_SQL = "create table Statistics(StatisticID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) CONSTRAINT StatisticID_PK PRIMARY KEY, StatisticName char(50) NOT NULL, StatisticValue double)";

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

    private void createDatabaseIfNotCreated() throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getTables(null, null, "STATISTICS", null);
        if (!rs.next()) {
            System.out.println("Creating Database Table 'Statistics'");
            Statement statement = connection.createStatement();
            statement.executeUpdate(CREATE_STATISTICS_TABLE_SQL);
            System.out.println("Statistics Table Created!");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
