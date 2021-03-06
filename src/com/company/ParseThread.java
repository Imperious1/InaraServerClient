package com.company;

import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;

import static com.company.Utils.parseData;

/**
 * Created by blaze on 5/22/2016.
 */
class ParseThread extends Thread {

    private static final String TABLE_DATA = "td";
    private static int index = 1;
    private static int specifiedIndex = 0;
    private static boolean allowed = true;

    //
    @Override
    public void run() {
        try {
            if (specifiedIndex != 0)
                index = specifiedIndex;
            while (allowed) {
                String s = parseData("http://inara.cz/cmdr/" + index, 0);
                if (s != null) {
                    if (s.toLowerCase().contains("cmdr example") && index > 26644) {
                        System.out.println("End of commanders reached, breaking...");
                        break;
                    }
                }
                System.out.println("ParseThread index is " + index);
                index++;
            }
            specifiedIndex = 0;
            index = 1;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    static DataModel getModel(Elements element, int index) {
        String wing = element.select(TABLE_DATA).get(9).text();
        if (wing.contains("'")) wing = wing.replace("'", "''");
        return new DataModel()
                .setImageUrl("'" + element.select(TABLE_DATA).get(0).getElementsByTag("img").attr("src") + "'")
                .setId((index != 0) ? String.valueOf(index) : String.valueOf(ParseThread.index))
                .setCmdrName("'" + element.select(TABLE_DATA).get(1).text().replace("'", "''").replace("\\", "\\\\") + "'")
                .setRole("'" + element.select(TABLE_DATA).get(2).text().replace("Role ", "").replaceFirst("Role", "") + "'")
                .setAllegiance("'" + element.select(TABLE_DATA).get(3).text().replace("Allegiance ", "") + "'")
                .setRank("'" + element.select(TABLE_DATA).get(4).text().replace("Rank ", "") + "'")
                .setShip("'" + validateShip(element.select(TABLE_DATA).get(5).text()) + "'")
                .setPower("'" + element.select(TABLE_DATA).get(6).text().replace("Power ", "") + "'")
                .setBalance("'" + element.select(TABLE_DATA).get(7).text().replace("Credit Balance ", "") + "'")
                .setRegShipName("'" + element.select(TABLE_DATA).get(8).text().replace("'", "''").replace("Registered ship name ", "").replaceFirst("Registered ship name", "") + "'")
                .setWing("'" + wing.replace("Wing ", "") + "'")
                .setAssets("'" + element.select(TABLE_DATA).get(10).text().replace("Overall assets ", "") + "'");
    }

    //temp or perm fix for broken ship name bug
    private static String validateShip(String data) {
        String ship = data;
        switch (data) {
            case "Federal Dropship":
                ship = "Federal Dropship";
                break;
            case "Federal Assault Ship":
                ship = "Federal Assault Ship";
                break;
            case "Federal Gunship":
                ship = "Federal Gunship";
                break;
            case "Ship":
                ship = "";
            default:
                ship = ship.replace("Ship ", "".replaceFirst("Ship", ""));
                break;
        }
        return ship;
    }

    // whether or not the thread should be allowed to continue looping/running
    static void setAllowed(boolean allowed) {
        ParseThread.allowed = allowed;
    }

    //set the index for where the thread should start at **FOR USER MONITOR/SERVER CONTROL I THINk**
    void setSpecifiedIndex(int specifiedIndex) {
        ParseThread.specifiedIndex = specifiedIndex;
    }
}