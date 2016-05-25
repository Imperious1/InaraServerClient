package com.company;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by blaze on 5/25/2016.
 */
class NewUsersThread extends Thread {

    @Override
    public void run() {
        try {
            ParseThread parseThread = new ParseThread();
            parseThread.setEndless(true);
            parseThread.setSpecifiedIndex(getLastAdded());
            parseThread.start();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int getLastAdded() throws SQLException {
        Statement statement = DriverManager.getConnection("jdbc:mysql://localhost:3306/inara_users", "root", "").createStatement();
        statement.execute("SELECT id FROM cmdrs ORDER BY id DESC LIMIT 1;");
        ResultSet rs = statement.getResultSet();
        if (rs.next()) {
            return rs.getInt("id");
        } else
            return -1;
    }

}
