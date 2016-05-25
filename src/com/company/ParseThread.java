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

    @Override
    public void run() {
        String s;
        try {
            while (true) {
                s = parseData("http://inara.cz/cmdr/" + index);
                assert s != null;
                if (s.toLowerCase().contains("cmdr example") && index > 26474) {
                    System.out.println("End of commanders reached, breaking...");
                    break;
                }
                index++;
            }
            index = 1;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    static DataModel getModel(Elements element) {
        String wing = element.select(TABLE_DATA).get(9).text();
        String regShipName = element.select(TABLE_DATA).get(8).text().replace("'", "").replace("Registered ship name ", "").replaceFirst("Registered ship name", "");
        if (wing.contains("'")) wing = wing.replace("'", "");
        return new DataModel()
                .setImageUrl("'" + element.select(TABLE_DATA).get(0).getElementsByTag("img").attr("src") + "'")
                .setId(String.valueOf(index))
                .setCmdrName("'" + element.select(TABLE_DATA).get(1).text() + "'")
                .setRole("'" + element.select(TABLE_DATA).get(2).text().replace("Role ", "").replaceFirst("Role", "") + "'")
                .setAllegiance("'" + element.select(TABLE_DATA).get(3).text().replace("Allegiance ", "") + "'")
                .setRank("'" + element.select(TABLE_DATA).get(4).text().replace("Rank ", "") + "'")
                .setShip("'" + validateShip(element.select(TABLE_DATA).get(5).text()) + "'")
                .setPower("'" + element.select(TABLE_DATA).get(6).text().replace("Power ", "") + "'")
                .setBalance("'" + element.select(TABLE_DATA).get(7).text().replace("Credit Balance ", "") + "'")
                .setRegShipName("'" + regShipName + "'")
                .setWing("'" + wing.replace("Wing ", "") + "'")
                .setAssets("'" + element.select(TABLE_DATA).get(10).text().replace("Overall assets ", "") + "'");
    }

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
}