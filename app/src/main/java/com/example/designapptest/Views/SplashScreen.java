package com.example.designapptest.Views;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.provider.Settings;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.designapptest.Model.Device;
import com.example.designapptest.Notification.NotificationApplication;
import com.example.designapptest.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.DialogPropertiesPendulum;
import org.imaginativeworld.oopsnointernet.dialogs.pendulum.NoInternetDialogPendulum;
import org.imaginativeworld.oopsnointernet.dialogs.signal.DialogPropertiesSignal;
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal;

public class SplashScreen extends AppCompatActivity {
    DatabaseReference nodeRoot;
    private static final int NOTIFICATION_ID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.timi);
        mediaPlayer.start();
        isOnline();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoom);
        ImageView anhChinh = this.findViewById(R.id.imageView2);

        anhChinh.setAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (currentUser == null) {
                            startActivity(new Intent(SplashScreen.this, onBarLoading.class));
                            finishAffinity();
                            return;
                        } else {
                            if (currentUser.getUid().equals("cwEEwMym4lP5W8A6dw7JEHz6EyL2")) {
                                Toast.makeText(SplashScreen.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), adminView.class);
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                nodeRoot = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
                                nodeRoot.child("phoneNumber").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {

                                       Object phone = task.getResult().getValue();
                                            if (phone == null) {
                                                Intent intent = new Intent(getApplicationContext(), AuthPhoneUserNewOTP.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(SplashScreen.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), Main_Menu.class);
                                                startActivity(intent);
                                                finishAffinity();
                                            }
                                    }
                                });
                            }

                        }
                    }
                },2000);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void  sendNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(SplashScreen.this, NotificationApplication.CHANNEL_ID)
                .setContentTitle("Chào bạn nhé!")
                .setContentText("Hôm nay thật là tuyệt vời khi được gặp bạn! Chúc bạn có một ngày thật vui vẻ!")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Hôm nay thật là tuyệt vời khi được gặp bạn! Chúc bạn có một ngày thật vui vẻ!"))
                .setSmallIcon(R.drawable.abcdefgh)
                .setLargeIcon(bitmap)
                .setSound(uri)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, notification);
        }

    }

    public void isOnline() {
        NoInternetDialogSignal.Builder builder = new NoInternetDialogSignal.Builder(
                this,
                getLifecycle()
        );

        DialogPropertiesSignal properties = builder.getDialogProperties();

        properties.setConnectionCallback(new ConnectionCallback() { // Optional
            @Override
            public void hasActiveConnection(boolean hasActiveConnection) {
                // ...
            }
        });

        properties.setCancelable(false); // Optional
        properties.setNoInternetConnectionTitle("Không có internet!"); // Optional
        properties.setNoInternetConnectionMessage("Ôi không! người ngoài hành tinh đã cản trở kết nối của chúng ta, hãy kiểm tra và thử lại!"); // Optional
        properties.setShowInternetOnButtons(true); // Optional
        properties.setPleaseTurnOnText("Hãy bật chức năng internet của bạn lên"); // Optional
        properties.setWifiOnButtonText("Wifi"); // Optional
        properties.setMobileDataOnButtonText("Mobile data"); // Optional

        properties.setOnAirplaneModeTitle("Không có internet!"); // Optional
        properties.setOnAirplaneModeMessage("Ôi không! người ngoài hành tinh đã cản trở kết nối của chúng ta, hãy kiểm tra và thử lại!"); // Optional
        properties.setPleaseTurnOffText("Có phải bạn chưa tắt chế độ máy bay?"); // Optional
        properties.setAirplaneModeOffButtonText("Tắt chế độ máy bay"); // Optional
        properties.setShowAirplaneModeOffButtons(true); // Optional

        builder.build();

    }
}