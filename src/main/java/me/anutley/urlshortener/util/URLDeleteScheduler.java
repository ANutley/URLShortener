package me.anutley.urlshortener.util;

import me.anutley.urlshortener.database.Database;
import me.anutley.urlshortener.database.URL;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class URLDeleteScheduler {

    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    public static void run() {

        Runnable initialiseReminders = () -> {

            if (Long.parseLong(Config.getInstance().get("ExpirationDays")) == -1) return;

            for (URL url : Database.getAllURLs()) {

                if (url.isPermanent()) return;

                if (System.currentTimeMillis() >= url.getTimeCreated() + TimeUnit.DAYS.toMillis(Long.parseLong(Config.getInstance().get("ExpirationDays")))) {
                    URL.removeURLById(url.getCode());
                }
            }
        };
        executor.scheduleAtFixedRate(initialiseReminders, 0, 30, TimeUnit.MINUTES);
    }
}
