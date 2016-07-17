package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.SQLException;

/**
 * Created by blaze on 5/24/2016.
 */
class ServerControl extends Thread {

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() {
        //await console admin input and act accordingly
        Singleton.getInstance();
        String s;
        try {
            while ((s = br.readLine()) != null) {
                switch (s.toLowerCase()) {
                    case "addall":
                        new ParseThread().start();
                        break;
                    case "updateall":
                        Utils.updateAll();
                        break;
                    case "stopadd":
                        ParseThread.setAllowed(false);
                        break;
                    case "stopupdate":
                        Utils.setAllowed(false);
                        break;
                    case "deleteallfromdb":
                        clearDb();
                    case "disconnectall":
                        disconnectAll();
                        break;
                    case "stopmonitor":
                        UserMonitor.setAllowed(false);
                        break;
                    case "restartmonitor":
                        new UserMonitor().start();
                        UserMonitor.setAllowed(true);
                    default:
                        break;
                }
                if (s.toLowerCase().contains("addfrom")) {
                    ParseThread parseThread = new ParseThread();
                    parseThread.setSpecifiedIndex(Integer.parseInt(s.replace("addfrom ", "")));
                    parseThread.start();
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void disconnectAll() {
        for (Socket socket : Singleton.getClients()) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        AwaitCommand.setShouldAwait(false);
        Singleton.removeAll();
    }

    private void clearDb() {
        while (true) {
            System.out.println("Are you sure you wish to delete every user from your database?");
            String input;
            try {
                input = br.readLine();
                if (input.equals("y") || input.equals("yes")) {
                    Utils.getDbConnection(1).createStatement().execute("DELETE FROM inara_users.cmdrs;");
                    System.out.println("Deleted...breaking");
                    break;
                } else if (input.equals("n") || input.equals("no")) {
                    System.out.println("Cancelled, breaking...");
                    break;
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }

        }
    }

}
