package com.erudito.main_stay.receiver;

/**
 * Created by nonstop on 7/3/17.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import androidx.core.app.NotificationCompat;

import com.erudito.main_stay.Classes.TabActivity;
import com.android.main_stay.R;


public class PushReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationTitle = "NexoR2";
        String notificationText = "Test notification";

        notificationText = intent.getStringExtra("message");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.push_notif_logo)
                        .setAutoCancel(true)
                        .setContentTitle(notificationTitle)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText))
                        .setContentText(notificationText)
                        .setLights(Color.RED, 1000, 1000)
                        .setVibrate(new long[]{0, 400, 250, 400})
                        .setDefaults(NotificationCompat.DEFAULT_SOUND)
                        .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, TabActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));

                // Get an instance of the NotificationManager service
                NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                // Build the notification and display it
                mNotifyMgr.notify(1, mBuilder.build());

    }
}
