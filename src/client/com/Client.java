package client.com;

import com.google.gson.Gson;
import com.sun.org.apache.xerces.internal.impl.io.ASCIIReader;
import com.sun.xml.internal.ws.util.ASCIIUtility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by blaze on 5/23/2016.
 */
public class Client {

    private static Socket socket;

    public static void main(String[] args) throws IOException {
        socket = new Socket("127.0.0.1", 4044);
        new UserInputThread().start();
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String s;
        DataModel dataModel;
        while ((s = br.readLine()) != null) {
            dataModel = fromJson(s);
            if (!dataModel.isError()) {
                System.out.println(dataModel.getId());
                System.out.println(dataModel.getCmdrName());
                System.out.println(dataModel.getRole());
                System.out.println(dataModel.getAllegiance());
                System.out.println(dataModel.getRank());
                System.out.println(dataModel.getShip());
                System.out.println(dataModel.getPower());
                System.out.println(dataModel.getBalance());
                System.out.println(dataModel.getRegShipName());
                System.out.println(dataModel.getWing());
                System.out.println(dataModel.getAssets());
            } else
                System.out.println("Invalid request.");
        }

    }

    static void sendRequest(String stuff) {
        new Thread(() -> {
            try {
                new PrintWriter(socket.getOutputStream(), true).println(stuff);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static DataModel fromJson(String s) {
        return new Gson().fromJson(s, DataModel.class);
    }

    static String toJson(RequestModel model) {
        return new Gson().toJson(model);
    }
}