package com.example.designapptest.Mes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.designapptest.Notification.NotificationApplication;
import com.example.designapptest.R;
import com.example.designapptest.Views.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private Object NotificationManager;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        if (notification == null) {
            return;
        }
        String strTitle = notification.getTitle();
        String strmess = notification.getBody();


        sendNotification(strTitle, strmess);

    }

    private void sendNotification(String strTitle, String strmess) {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[]{intent}, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifitionBuilder = new NotificationCompat.Builder(this, NotificationApplication.CHANNEL_ID)
                .setContentTitle(strTitle)
                .setContentText(strmess)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(strmess))
                .setSmallIcon(R.drawable.ic_svg_home_24)
                .setContentIntent(pendingIntent);

        Notification notification = notifitionBuilder.build();
        NotificationManager notificationManager = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }
    }
}
