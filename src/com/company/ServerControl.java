package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * Created by blaze on 5/24/2016.
 */
class ServerControl extends Thread {
    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String s;
        try {
            while ((s = br.readLine()) != null) {
                if (s.toLowerCase().equals("addall"))
                    new ParseThread().start();
                else if (s.toLowerCase().equals("updateall"))
                    Utils.updateAll();
                else if (s.toLowerCase().equals("stopadd"))
                    ParseThread.setAllowed(false);
                else if (s.toLowerCase().equals("stopupdate"))
                    Utils.setAllowed(false);
                else if(s.toLowerCase().contains("addfrom")) {
                    ParseThread parseThread = new ParseThread();
                    parseThread.setSpecifiedIndex(Integer.parseInt(s.replace("addfrom ", "")));
                    parseThread.start();
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}
