package com.rgs.covid_19;

import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService  {

    private final String CHANNEL_ID = "simple notification";
    private final int NOTIFICATION_ID = 1000;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification().getBody() != null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            builder.setSmallIcon(R.drawable.ic_virus);
            builder.setContentTitle(remoteMessage.getNotification().getTitle());
            builder.setContentText(remoteMessage.getNotification().getBody());
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
        }

        Log.i("PVL", "MESSAGE RECEIVED!!");
        if (remoteMessage.getData().get("title") != null) {
            String title = remoteMessage.getData().get("title");
          //  String body = remoteMessage.getData().get("body");

            Log.i("PVL", title);
           // Log.i("PVL", body);
        }
    }
}
