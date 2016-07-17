package com.company;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    public static void main(String[] args) {
        // start admin control thread and database hourly updater thread
        new ServerControl().start();
        new UserMonitor().start();
        boolean listening = true;
        //listen for connection attempt and handle it accordingly
        try (ServerSocket socket = new ServerSocket(4044)) {
            while (listening) {
                new AwaitCommand(Singleton.addToList(socket.accept())).start();
                System.out.println("Connection started...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
