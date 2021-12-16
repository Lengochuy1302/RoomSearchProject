package com.example.designapptest.Views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.example.designapptest.Model.Device;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.designapptest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.dialogs.signal.DialogPropertiesSignal;
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Main_Menu extends AppCompatActivity {

    BottomNavigationView bottomNavigation;
    FrameLayout fragmentContainer;
    DatabaseReference nodeRoot;
    MainActivity HomeView;
    accountView AccountView;
    postRoomAdapter PostRoomView;
    FindRoom FindRoomView;
    searchMapView SearchMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__menu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        isOnline();
        initControl();
        //Chạy lần đầu tiên sẽ load vào màn hình main
        HomeView = new MainActivity();
        setFragment(HomeView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent service = new Intent(this, MyService.class);
        Main_Menu.this.startService(service);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            return;
        }

        Device device = new Device();
        String iddevice = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        device.setID("ThietBi");
        device.setTenDevice(iddevice);
        nodeRoot = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid()).child("DeviceID");
        nodeRoot.child("ThietBi").setValue(device);


    }

    //Khởi tạo control
    private void initControl(){
        fragmentContainer = findViewById(R.id.fragment_container);
        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.nav_home:
                        //Chuyển sang màn hình home
                        //HomeView = new MainActivity();
                        setFragment(HomeView);
                        return true;
                    case R.id.nav_find_room:
                        //Chuyển sang màn hình tìm kiếm ở ghép
                        FindRoomView = new FindRoom();
                        setFragment(FindRoomView);
                        return true;
                    case R.id.nav_map:
                        //Chuyển sang màn hình map
                        SearchMapView = new searchMapView();
                        setFragment(SearchMapView);
                        return true;
                    case R.id.nav_post_room:
                        //Hiển thị màn hình đăng phòng mới
                        Intent intent = new Intent(Main_Menu.this, postRoomAdapter.class);
                        startActivity(intent);
                        return true;
                    case R.id.nav_acount:
                        //Chuyển sang màn hình quản lý tài khoản
                        AccountView = new accountView();
                        setFragment(AccountView);
                        return true;

                    default:
                        return false;
                }
            }
        });
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

    //Hàm replace fragment tương ứng khi chọn vào menu
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                .setTitleText("Bạn muốn thoát ư?")
                .setCustomImage(R.drawable.door)
                .setCancelText("Không")
                .setConfirmText("Có")
                .showCancelButton(true)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();
    }
}
