package com.siwuxie095.concurrent.chapter4th.example12th;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Jiajing Li
 * @date 2020-09-13 08:07:22
 */
@SuppressWarnings("all")
class BadParseDate implements Runnable {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int i = 0;

    public BadParseDate(int i) {
        this.i = i;
    }

    @Override
    public void run() {
        try {
            Date date = sdf.parse("2020-09-13 08:00:" + (i % 60));
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 1000; i++) {
            executor.execute(new BadParseDate(i));
        }
    }
}
