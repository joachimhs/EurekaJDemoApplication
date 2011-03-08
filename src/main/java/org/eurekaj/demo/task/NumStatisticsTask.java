package org.eurekaj.demo.task;

import org.eurekaj.demo.derby.DerbyEnvironment;
import org.eurekaj.demo.gui.DemoJFrame;

import javax.swing.*;
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
public class NumStatisticsTask implements Runnable {
    private DerbyEnvironment derbyEnvironment;
    private DemoJFrame gui;

    public NumStatisticsTask(DerbyEnvironment derbyEnvironment, DemoJFrame gui) {
        this.derbyEnvironment = derbyEnvironment;
        this.gui = gui;
    }

    private Long readRowCountFromStatisticsTable() throws SQLException {
        String sql = "select count(1) as rowCount from Statistics";

        PreparedStatement preparedStatement = derbyEnvironment.getConnection().prepareStatement(sql);

        Long count = null;
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            count = rs.getLong("rowCount");
        }

        return count;
    }

    public void run() {
        try {
            if (derbyEnvironment != null) {
                Long count = readRowCountFromStatisticsTable();
                derbyEnvironment.setNumberOfRecordsInStatisticsDatabase(count);
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
