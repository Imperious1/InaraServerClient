package com.company;

import java.util.ArrayList;

/**
 * Created by blaze on 5/22/2016.
 */
class Singleton {

    private static ArrayList<DataModel> parsedList = new ArrayList<>();

    static void addToList(DataModel model) {
        parsedList.add(model);
    }

    static ArrayList<DataModel> getParsedList() {
        return parsedList;
    }

    private static Singleton ourInstance = new Singleton();

    static Singleton getInstance() {
        return ourInstance;
    }

    private Singleton() {
    }
}
