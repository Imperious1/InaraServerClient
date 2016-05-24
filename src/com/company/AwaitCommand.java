package com.company;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ConcurrentModificationException;

/**
 * Created by blaze on 5/22/2016.
 */
class AwaitCommand extends Thread {

    private Socket socket = null;

    AwaitCommand(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        // 1 = Query for names
        // 2 = TBD
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String s;
            RequestModel request;
            while ((s = in.readLine()) != null) {
                System.out.println(s);
                request = fromJson(s);
                handleRequest(request.getRequestId(), request.getSearchedName());
            }
        } catch (IOException | ConcurrentModificationException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(int id, String searchedName) {
        switch (id) {
            case 1:
                findAndSendPlayer(searchedName);
                break;
            case 2:
                findAndSendGroupsPlayers(searchedName.replace("Group: ", ""));
                break;
            case 3:
                findAndSendImps();
                break;
            case 4:
                findAndSendFeds();
                break;
            default:
                sendData(ParseThread.toJson(new DataModel().setError(true)));
        }
    }

    private void findAndSendPlayer(String searchedName) {
        for (DataModel m : Singleton.getParsedList()) {
            if (m.getCmdrName().toLowerCase().contains(searchedName.toLowerCase())) {
                sendData(ParseThread.toJson(m));
            }
        }
    }

    private void findAndSendGroupsPlayers(String groupName) {
        for (DataModel m : Singleton.getParsedList()) {
            if (m.getWing().toLowerCase().contains(groupName.toLowerCase().replace("Group: ", ""))) {
                sendData(ParseThread.toJson(m));
            }
        }
    }

    private void findAndSendImps() {
        for (DataModel m : Singleton.getParsedList()) {
            if (m.getAllegiance().toLowerCase().contains("empire")) {
                sendData(ParseThread.toJson(m));
            }
        }
    }

    private void findAndSendFeds() {
        for (DataModel m : Singleton.getParsedList()) {
            if (m.getAllegiance().toLowerCase().contains("federation")) {
                sendData(ParseThread.toJson(m));
            }
        }
    }

    private void sendData(String data) {
        new Thread(() -> {
            try {
                new PrintWriter(socket.getOutputStream(), true).println(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private RequestModel fromJson(String json) {
        return new Gson().fromJson(json, RequestModel.class);
    }

}
