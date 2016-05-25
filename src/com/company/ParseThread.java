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
    private static boolean allowed = true;
    private int specifiedIndex = 0;
    private boolean isEndless = false;
    private String s;

    @Override
    public void run() {
        try {
            if (specifiedIndex != 0)
                index = specifiedIndex;
            while (allowed) {
                if (!isEndless) {
                    normalParse();
                } else {

                }
                index = 1;
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void normalParse() throws IOException, SQLException {
        while (allowed) {
            s = parseData("http://inara.cz/cmdr/" + index, false);
            if (s != null) {
                if (s.toLowerCase().contains("cmdr example") && index >= 26584) {
                    if (!isEndless)
                        break;
                } else {
                    if (!isEndless) {
                        System.out.println("At end, breaking...");
                        break;
                    }
                }
                index++;
            }
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

    static void setAllowed(boolean allowed) {
        ParseThread.allowed = allowed;
    }

    void setSpecifiedIndex(int specifiedIndex) {
        this.specifiedIndex = specifiedIndex;
    }

    ParseThread setEndless(boolean endless) {
        isEndless = endless;
        return this;
    }
}