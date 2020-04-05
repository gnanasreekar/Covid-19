package com.rgs.covid_19;

import android.app.Notification;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseInstanceIDService extends FirebaseMessagingService  {

    private final String CHANNEL_ID = "simple notification";
    private final int NOTIFICATION_ID = 1000;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("Firebaseeeeeeeeeeee", s);
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification().getBody() != null) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
            builder.setSmallIcon(R.drawable.ic_virus);
            builder.setContentTitle(remoteMessage.getNotification().getTitle());
            builder.setContentText(remoteMessage.getNotification().getBody());
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
        }

//        Notification notification = new NotificationCompat.Builder(this)
//                .setContentTitle(remoteMessage.getNotification().getTitle())
//                .setContentText(remoteMessage.getNotification().getBody())
//                .setSmallIcon(R.drawable.ic_virus)
//                .build();
//        NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
//        manager.notify(123, notification);
        Log.i("PVL", "MESSAGE RECEIVED!!");
//        if (remoteMessage.getData() != null) {
//            String title = remoteMessage.getData().get("title");
//            String body = remoteMessage.getData().get("body");
//
//            Log.i("PVL", title);
//            Log.i("PVL", body);
//        }
        if (remoteMessage.getNotification().getBody() != null) {
            Log.i("PVL1", "RECEIVED MESSAGE: " + remoteMessage.getNotification().getTitle());
            Log.i("PVL2", "RECEIVED MESSAGE: " + remoteMessage.getNotification().getBody());
        } else {
            Log.i("PVL3", "RECEIVED MESSAGE2: " + remoteMessage.getData().get("message"));
        }
    }
}
