package com.company;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    public static void main(String[] args) {
        new ServerControl().start();
        boolean listening = true;
        try(ServerSocket socket = new ServerSocket(4044)) {
            while(listening) {
                new AwaitCommand(socket.accept()).start();
                System.out.println("Connection started...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
