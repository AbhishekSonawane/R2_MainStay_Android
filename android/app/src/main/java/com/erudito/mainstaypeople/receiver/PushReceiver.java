package com.erudito.mainstaypeople.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.android.main_stay.R;
import com.erudito.mainstaypeople.Classes.TabActivity;

public class PushReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "MainStay_Channel"; // Unique channel ID

    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationTitle = "MainStay";
        String notificationText = "Test notification";

        // Retrieve the message from the intent
        if (intent.hasExtra("message")) {
            notificationText = intent.getStringExtra("message");
        }

        // Create the PendingIntent with FLAG_IMMUTABLE for Android 12+
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                new Intent(context, TabActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Create the NotificationCompat.Builder
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.push_notif_logo)
                .setAutoCancel(true)
                .setContentTitle(notificationTitle)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText))
                .setContentText(notificationText)
                .setLights(Color.RED, 1000, 1000)
                .setVibrate(new long[]{0, 400, 250, 400})
                .setDefaults(NotificationCompat.DEFAULT_SOUND)
                .setContentIntent(pendingIntent);

        // Ensure the notification channel exists for Android 8.0+ (API 26+)
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "MainStay Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Channel for MainStay notifications");
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 400, 250, 400});
            notificationManager.createNotificationChannel(channel);
        }

        // Display the notification
        notificationManager.notify(1, mBuilder.build());
    }
}
