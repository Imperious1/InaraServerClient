package com.company;

import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by blaze on 5/25/2016.
 */
class Singleton {

    private static ArrayList<Socket> clients = new ArrayList<>();

    private static Singleton ourInstance = new Singleton();

    static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
    }

    static ArrayList<Socket> getClients() {
        return clients;
    }

    static Socket addToList(Socket socket) {
        clients.add(socket);
        return socket;
    }

    static void removeAll() {
        clients.clear();
    }

}
