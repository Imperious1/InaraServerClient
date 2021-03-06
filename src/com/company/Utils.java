package com.company;

import com.google.gson.Gson;
import org.jsoup.Jsoup;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;

import static com.company.ParseThread.getModel;

/**
 * Created by blaze on 5/24/2016.
 */
final class Utils {

    private static int index = 1;
    private static boolean allowed = true;

    static void sendData(String data, Socket socket) {
        new Thread(() -> {
            try {
                new PrintWriter(socket.getOutputStream(), true).println(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    static String toJson(DataModel model) {
        return new Gson().toJson(model);
    }

    static RequestModel fromJson(String json) {
        return new Gson().fromJson(json, RequestModel.class);
    }

    static Connection getDbConnection(int t) throws SQLException {
        if (t == 1)
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/inara_users", "root", "TempPassword107");
        else
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/search_history", "root", "TempPassword107");
    }

    private static String downloadPage(String url) throws IOException {
        HttpsURLConnection mConnection = null;
        HttpURLConnection mHttpConnection = null;
        if (url.contains("https")) mConnection = (HttpsURLConnection) new URL(url).openConnection();
        else mHttpConnection = (HttpURLConnection) new URL(url).openConnection();
        BufferedReader br;
        StringBuilder sb = new StringBuilder();
        assert mHttpConnection != null;
        br = new BufferedReader(new InputStreamReader((mConnection == null) ? mHttpConnection.getInputStream() : mConnection.getInputStream()));
        String s;
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }
        br.close();
        if (mConnection == null)
            mHttpConnection.disconnect();
        else mConnection.disconnect();
        return sb.toString();
    }

    static void updateEntries(ArrayList<DataModel> models, Socket socket) throws SQLException, IOException {
        for (DataModel m : models) {
            System.out.println(m.getCmdrName());
            DataModel newModel = getModel(Jsoup.parse(downloadPage("http://inara.cz/cmdr/" + m.getId())).getElementsByTag("td"), 0);
            getDbConnection(1).createStatement().execute(DataModel.createUpdateString(newModel.setId(m.getId())));
            sendData(toJson(newModel.setId(m.getId())), socket);
        }
    }

    static void updateEntry(DataModel model, Socket socket) throws SQLException, IOException {
        System.out.println("User " + model.getCmdrName() + "updated.");
        DataModel newModel = getModel(Jsoup.parse(downloadPage("http://inara.cz/cmdr/" + model.getId())).getElementsByTag("td"), 0);
        getDbConnection(1).createStatement().execute(DataModel.createUpdateString(newModel.setId(model.getId())));
        sendData(toJson(newModel.setId(model.getId())), socket);
    }

    static void updateAll() throws SQLException {
        new Thread(() -> {
            try {
                while (allowed) {
                    DataModel newModel = getModel(Jsoup.parse(downloadPage("http://inara.cz/cmdr/" + index)).getElementsByTag("td"), 0).setId(String.valueOf(index));
                    if (newModel.getCmdrName().toLowerCase().contains("cmdr example") && index > 26474) {
                        System.out.println("End of commanders reached, breaking...");
                        break;
                    }
                    getDbConnection(1).createStatement().execute(DataModel.createUpdateAllString(newModel));
                    System.out.println(index);
                    index++;
                }
                index = 1;
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    static void setAllowed(boolean allowed) {
        Utils.allowed = allowed;
    }

    static String parseData(String url, int index) throws IOException, SQLException {
        DataModel model = getModel(Jsoup.parse(downloadPage(url)).getElementsByTag("td"), index);
        return toJson(insertIntoDb(model));
    }

    private static DataModel insertIntoDb(DataModel model) {
        String s = "INSERT INTO cmdrs (imageurl, id, cmdrname, rank, ship, regshipname, wing, assets, balance, role ,allegiance, power)" +
                " VALUES (" + model.getImageUrl() + "," + model.getId() + "," + model.getCmdrName() + "," + model.getRank() + "," + model.getShip()
                + "," + model.getRegShipName() + "," + model.getWing() + "," + model.getAssets() + "," + model.getBalance()
                + "," + model.getRole() + "," + model.getAllegiance() + "," + model.getPower() + ");";
        try {
            getDbConnection(1).createStatement().execute(s);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062)
                System.out.println("Error: Database already contains this user");
            else {
                System.out.println("Error: " + e.getErrorCode());
                System.out.println(e.getMessage());
            }
        }
        return model;
    }

    // form a model arraylist for group search
    static ArrayList<DataModel> formModelGroup(String groupQuery) throws SQLException {
        Statement statement = getDbConnection(1).createStatement();
        statement.execute("SELECT * FROM cmdrs WHERE wing=" + "'" + groupQuery + "'");
        ResultSet rs = statement.getResultSet();
        ArrayList<DataModel> groupPlayerList = new ArrayList<>();
        while (rs.next()) {
            groupPlayerList.add(getModelFromResultSet(rs));
        }
        System.out.println(groupPlayerList.size());
        return groupPlayerList;
    }

    //form a model for a players name
    static DataModel formModelStandard(String query) throws SQLException {
        Statement statement = getDbConnection(1).createStatement();
        statement.execute(String.format("SELECT * FROM inara_users.cmdrs WHERE cmdrname= '%s' LIMIT 1;", query.contains("cmdr ") ? query : "cmdr " + query));
        ResultSet rs = statement.getResultSet();
        if (rs.next()) {
            if (!rs.getString("cmdrname").isEmpty())
                return getModelFromResultSet(rs);
        }
        return null;
    }

    //form a list of 50 max players
    static ArrayList<DataModel> formModelListStandard(String query) throws SQLException {
        Statement statement = getDbConnection(1).createStatement();
        statement.execute(String.format("SELECT * FROM inara_users.cmdrs WHERE cmdrname LIKE '%%%s%%' LIMIT 50;", query));
        ResultSet rs = statement.getResultSet();
        ArrayList<DataModel> cmdrsList = new ArrayList<>();
        while (rs.next()) {
            if (!rs.getString("cmdrname").isEmpty())
                cmdrsList.add(getModelFromResultSet(rs));
        }
        return cmdrsList;
    }

    private static DataModel getModelFromResultSet(ResultSet rs) throws SQLException {
        return new DataModel().setImageUrl(rs.getString("imageurl")).setId(String.valueOf(rs.getInt("id")))
                .setCmdrName(rs.getString("cmdrname")).setRole(rs.getString("role"))
                .setBalance(rs.getString("balance")).setAssets(rs.getString("assets")).setAllegiance(rs.getString("allegiance"))
                .setPower(rs.getString("power")).setRank(rs.getString("rank")).setRegShipName(rs.getString("regshipname"))
                .setWing(rs.getString("wing")).setShip(rs.getString("ship"));
    }
}
