package com.company;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    public static void main(String[] args) {
        new ServerControl().start();
        new UserMonitor().start();
        boolean listening = true;
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
