package com.company;

import com.sun.org.apache.xerces.internal.impl.io.ASCIIReader;
import com.sun.xml.internal.ws.util.ASCIIUtility;
import sun.nio.cs.SingleByte;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.company.Utils.*;

/**
 * Created by blaze on 5/22/2016.
 */
class AwaitCommand extends Thread {

    private Socket socket = null;
    private static boolean shouldAwait = true;

    AwaitCommand(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // 1 = Query for names
        // 2 = Group
        // 3 = Update & Search
        try {
            //Wait for requests and handle them accordingly
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String s;
            RequestModel request;
            while ((s = in.readLine()) != null && shouldAwait) {
                System.out.println(s);
                request = fromJson(s);
                handleRequest(request.getRequestId(), request.getSearchedName());
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(int id, String searchedName) throws SQLException, IOException {
        switch (id) {
            case 1:
                if (searchedName.length() >= 2)
                    findAndSendPlayers(searchedName);
                else findAndSendPlayer(searchedName);
                break;
            case 2:
                findAndSendGroupsPlayers(searchedName.replace("Group: ", "").replace("'", ""));
                break;
            case 3:
                if (searchedName.length() >= 2)
                    updateEntries(formModelListStandard(searchedName.replace("Update: ", "")), socket);
                else updateEntry(formModelStandard(searchedName.replace("Update: ", "")), socket);
                break;
            default:
                sendData(toJson(new DataModel().setError(true)), socket);
                break;
        }
    }

    private void findAndSendPlayers(String searchedName) {
        try {
            ArrayList<DataModel> m = formModelListStandard(searchedName.toLowerCase());
            if (m != null) {
                for (DataModel e : m) {
                    if (e.getCmdrName().toLowerCase().contains(searchedName.toLowerCase())) {
                        Utils.sendData(toJson(e), socket);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void findAndSendPlayer(String searchedName) throws SQLException {
        DataModel m = formModelStandard(searchedName.toLowerCase());
        if (m != null) {
            if (m.getCmdrName().toLowerCase().contains(searchedName.toLowerCase())) {
                Utils.sendData(toJson(m), socket);
            }
        }
    }

    private void findAndSendGroupsPlayers(String groupName) {
        try {
            for (DataModel m : formModelGroup(groupName)) {
                sendData(toJson(m), socket);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void setShouldAwait(boolean shouldAwait) {
        // set if server script should await requests or not
        AwaitCommand.shouldAwait = shouldAwait;
    }
}
