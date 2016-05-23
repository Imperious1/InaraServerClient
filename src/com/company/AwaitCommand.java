package com.company;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(int id, String searchedName) {
        if (id == 1) {
            findAndSend(searchedName);
        }
    }

    private void findAndSend(String searchedName) {
        for (DataModel m : Singleton.getParsedList()) {
            if (m.getCmdrName().toLowerCase().contains(searchedName.toLowerCase())) {
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
