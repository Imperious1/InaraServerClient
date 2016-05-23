package com.company;

import java.util.Calendar;

/**
 * Created by blaze on 5/23/2016.
 */
class UpdateMonitor extends Thread {

    @Override
    public void run() {
        int pastHour = Calendar.HOUR_OF_DAY;
        boolean waiting = true;
        while (waiting) {
            if (pastHour != Calendar.HOUR_OF_DAY) {
                new ParseThread().start();
                pastHour = Calendar.HOUR_OF_DAY;
            }
            try {
                synchronized (this) {
                    wait(60000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
