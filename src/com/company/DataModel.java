package com.company;

/**
 * Created by blaze on 5/22/2016.
 */
class DataModel {
    private String imageUrl;
    private String id;
    private String cmdrName;
    private String rank;
    private String ship;
    private String regShipName;
    private String wing;
    private String assets;
    private String balance;
    private String role;
    private String allegiance;
    private String power;
    private boolean isError;

    String getImageUrl() {
        return imageUrl;
    }

    DataModel setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    static String createUpdateString(DataModel newModel) {
        return String.format("UPDATE cmdrs SET allegiance=%s, assets=%s, balance=%s, cmdrname=%s, imageurl=%s, power=%s, rank=%s, regshipname=%s, role=%s, ship=%s, wing=%s WHERE id=%s;",
                newModel.getAllegiance(), newModel.getAssets(), newModel.getBalance(), newModel.getCmdrName(),
                newModel.getImageUrl(), newModel.getPower(), newModel.getRank() , newModel.getRegShipName()
                , newModel.getRole() , newModel.getShip(),newModel.getWing(), newModel.getId());
    }

    static String createUpdateAllString(DataModel newModel) {
        return String.format("UPDATE cmdrs SET allegiance=%s, assets=%s, balance=%s, cmdrname=%s, imageurl=%s, power=%s, rank=%s, regshipname=%s, role=%s, ship=%s, wing=%s WHERE id=%s;",
                newModel.getAllegiance(), newModel.getAssets(), newModel.getBalance(), newModel.getCmdrName(),
                newModel.getImageUrl(), newModel.getPower(), newModel.getRank() , newModel.getRegShipName(),
                newModel.getRole(), newModel.getShip(),newModel.getWing(), newModel.getId());
    }

    String getId() {
        return id;
    }

    DataModel setId(String id) {
        this.id = id;
        return this;
    }

    String getCmdrName() {
        return cmdrName;
    }

    DataModel setCmdrName(String cmdrName) {
        this.cmdrName = cmdrName;
        return this;
    }

    String getRank() {
        return rank;
    }

    DataModel setRank(String rank) {
        this.rank = rank;
        return this;
    }

    String getShip() {
        return ship;
    }

    DataModel setShip(String ship) {
        this.ship = ship;
        return this;
    }

    String getRegShipName() {
        return regShipName;
    }

    DataModel setRegShipName(String regShipName) {
        this.regShipName = regShipName;
        return this;
    }

    String getWing() {
        return wing;
    }

    DataModel setWing(String wing) {
        this.wing = wing;
        return this;
    }

    String getAssets() {
        return assets;
    }

    DataModel setAssets(String assets) {
        this.assets = assets;
        return this;
    }

    String getBalance() {
        return balance;
    }

    DataModel setBalance(String balance) {
        this.balance = balance;
        return this;
    }

    String getRole() {
        return role;
    }

    DataModel setRole(String role) {
        this.role = role;
        return this;
    }

    String getAllegiance() {
        return allegiance;
    }

    DataModel setAllegiance(String allegiance) {
        this.allegiance = allegiance;
        return this;
    }

    String getPower() {
        return power;
    }

    DataModel setPower(String power) {
        this.power = power;
        return this;
    }

    boolean isError() {
        return isError;
    }

    DataModel setError(boolean error) {
        isError = error;
        return this;
    }

    String[] getAll() {
        String[] stuff = new String[11];
        stuff[0] = imageUrl;
        stuff[1] = cmdrName;
        stuff[2] = rank;
        stuff[3] = ship;
        stuff[4] = regShipName;
        stuff[5] = wing;
        stuff[6] = assets;
        stuff[7] = balance;
        stuff[8] = role;
        stuff[9] = allegiance;
        stuff[10] = power;
        return stuff;
    }

}