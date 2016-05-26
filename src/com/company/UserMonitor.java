package com.company;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.company.Utils.getDbConnection;
import static com.company.Utils.parseData;

/**
 * Created by blaze on 5/25/2016.
 */
class UserMonitor extends Thread {

    private static boolean allowed = true;

    @Override
    public void run() {
        try {
            int index = getLastAdded() + 1;
            String s;
            while (allowed) {
                s = parseData("http://inara.cz/cmdr/" + index, index);
                if (s != null) {
                    if (s.toLowerCase().contains("cmdr example") && index > 26655) {
                        System.out.println("End reached...holding loop for 1 hour.");
                        index = getLastAdded();
                        synchronized (this) {
                            wait(3600000);
                        }
                    }
                }
                System.out.println("UserMonitor index is " + index);
                index++;
            }
        } catch (IOException | SQLException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getLastAdded() throws SQLException {
        Statement statement = getDbConnection(1).createStatement();
        statement.execute("SELECT id FROM cmdrs ORDER BY id DESC LIMIT 1;");
        ResultSet rs = statement.getResultSet();
        if (rs.next()) {
            return rs.getInt("id");
        } else
            return -1;
    }

    static void setAllowed(boolean allowed) {
        UserMonitor.allowed = allowed;
    }
}
