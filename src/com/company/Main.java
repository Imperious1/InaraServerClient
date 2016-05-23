package com.company;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    public static void main(String[] args) {
        new UpdateMonitor().start();
        new ParseThread().start();
        boolean listening = true;
        try(ServerSocket socket = new ServerSocket(4044)) {
            while(listening) {
                new AwaitCommand(socket.accept()).start();
                System.out.println("gotcha!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
