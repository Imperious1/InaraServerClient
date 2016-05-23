package client.com;

import com.google.gson.Gson;

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
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String s;
        DataModel dataModel;
        sendRequest(toJson(new RequestModel().setRequestId(1).setSearchedName("Zwamp")));
        while((s = br.readLine()) != null) {
            dataModel = fromJson(s);
            System.out.println(dataModel.getCmdrName());
        }

    }

    private static void sendRequest(String stuff) {
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

    private static String toJson(RequestModel model) {
        return new Gson().toJson(model);
    }
}
