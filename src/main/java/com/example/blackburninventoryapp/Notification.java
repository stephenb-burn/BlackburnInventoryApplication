package com.example.blackburninventoryapp;

import  android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

public class Notification extends Application {
    public static final String CHANNEL_1_ID = "channel1";

    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        NotificationChannel channel1 = new NotificationChannel(
                CHANNEL_1_ID,
                "Channel 1",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel1.setDescription("This is channel 1");

        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel1);
    }
}
