package com.example.designapptest.Views;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.designapptest.Model.Device;
import com.example.designapptest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MyService extends Service {
    private static final String NOTIFICATION_CHANNEL = "Thong_bao_Service";
    private static final int NOTIFICATION_ID = 1231;
    public static final String CHANNEL_ID = "Channel";
    private DatabaseReference reference;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("AAAA", "Khởi tạo Service");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String iddevice = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        sendNotification("AETH HOUSE!", "Tìm nhà trọ đang chạy!");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("DeviceID");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable  String previousChildName) {
                    Device device = snapshot.getValue(Device.class);
                if (device.getTenDevice().equals("")) {

                } else if (!device.getTenDevice().equals(iddevice)) {
                 sendmesss("Thông báo", "Tài khoản của bạn đã được đăng nhập ở thiết bị khác.");
                 FirebaseAuth.getInstance().signOut();
             }

            }

            @Override
            public void onChildRemoved(@NonNull  DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull  DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return START_NOT_STICKY;
    }

    private void  sendNotification(String tieude, String noidung) {
        Intent intent = new Intent(this, SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Notification notification = new NotificationCompat.Builder(MyService.this, CHANNEL_ID)
                .setContentTitle(""+ tieude +"")
                .setContentText(""+ noidung +"")
                .setLargeIcon(bitmap)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(120, notification);

    }

    private void  sendmesss(String tieude, String noidung) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MyService.this);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Service";
                String description = "Thông báo service";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, name, importance);
                channel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }

            }

        Intent intent = new Intent(this, SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notification = new NotificationCompat.Builder(MyService.this,
                    NOTIFICATION_CHANNEL)
                    .setContentTitle(""+ tieude +"")
                    .setContentText(""+ noidung +"")
                    .setSmallIcon(R.drawable.abcdefgh)
                    .setLargeIcon(bitmap)
                    .setSound(uri)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);



            if (notificationManager != null) {
                notificationManager.notify(NOTIFICATION_ID, notification.build());
            }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("AAAA", "Dừng Service");
    }
}