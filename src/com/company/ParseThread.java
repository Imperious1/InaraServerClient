package com.company;

import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by blaze on 5/22/2016.
 */
class ParseThread extends Thread {

    private static final String TABLE_DATA = "td";
    private int index = 1;
    private HttpsURLConnection mConnection = null;
    private HttpURLConnection mHttpConnection = null;

    @Override
    public void run() {
        Singleton.getInstance();
        String s;
        try {
            while (true) {
                s = parseData("http://inara.cz/cmdr/" + index);
                if (s.toLowerCase().contains("cmdr example") && index > 26474) {
                    System.out.println("End of commanders reached, breaking...");
                    break;
                }
                System.out.println(index);
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String toJson(DataModel model) {
        return new Gson().toJson(model);
    }

    private String downloadPage(String url) throws IOException {
        if (url.contains("https")) mConnection = (HttpsURLConnection) new URL(url).openConnection();
        else mHttpConnection = (HttpURLConnection) new URL(url).openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader((mConnection == null) ? mHttpConnection.getInputStream() : mConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String s;
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }
        return sb.toString();
    }

    private String parseData(String url) throws IOException {
        return toJson(Singleton.addToList(getModel(Jsoup.parse(downloadPage(url)).getElementsByTag("td"))));
    }

    private DataModel getModel(Elements element) {
        return new DataModel()
                .setId(String.valueOf(index))
                .setCmdrName(element.select(TABLE_DATA).get(1).text())
                .setRole(element.select(TABLE_DATA).get(2).text().replace("Role ", ""))
                .setAllegiance(element.select(TABLE_DATA).get(3).text().replace("Allegiance ", ""))
                .setRank(element.select(TABLE_DATA).get(4).text().replace("Rank ", ""))
                .setShip(element.select(TABLE_DATA).get(5).text().replace("Ship ", ""))
                .setPower(element.select(TABLE_DATA).get(6).text().replace("Power ", ""))
                .setBalance(element.select(TABLE_DATA).get(7).text().replace("Credit Balance ", ""))
                .setRegShipName(element.select(TABLE_DATA).get(8).text().replace("Registered ship name ", ""))
                .setWing(element.select(TABLE_DATA).get(9).text().replace("Wing ", ""))
                .setAssets(element.select(TABLE_DATA).get(10).text().replace("Overall assets ", ""));
    }
}
