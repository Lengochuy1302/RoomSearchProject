package com.example.designapptest.Views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.designapptest.ClassOther.myFilter;
import com.example.designapptest.Controller.DetailRoomController;
import com.example.designapptest.Controller.MainActivityController;
import com.example.designapptest.Controller.UserController;
import com.example.designapptest.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;

import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.dialogs.signal.DialogPropertiesSignal;
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal;

import java.util.ArrayList;
import java.util.List;

public class adminView extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sharedPreferences;
    String UID;
    FirebaseAuth firebaseAuth;

    TextView txtSumViewsAdminView, txtSumHostsAdminView, txtSumRoomsAdminView, logout;

    LinearLayout lnLtRoomsAdminView, lnLtHostsAdminView, lnLtReportsAdminView, lnLtRoomsWaitForApprovalAdminView;
    
    PieChart bieudo;

    UserController userController;
    MainActivityController mainActivityController;
    DetailRoomController detailRoomController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_view);
        isOnline();
        sharedPreferences = getSharedPreferences(LoginView.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginView.SHARE_UID, "n1oc76JrhkMB9bxKxwXrxJld3qH2");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initControl();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.signOut();
              startActivity(new Intent(adminView.this, onBarLoading.class));
                Toast.makeText(adminView.this, "Đã đang xuất", Toast.LENGTH_SHORT).show();
            }
        });
        setClick();
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

    private void initControl() {
        txtSumViewsAdminView = (TextView) findViewById(R.id.txt_sum_views_admin_view);
        txtSumHostsAdminView = (TextView) findViewById(R.id.txt_sum_hosts_admin_view);
        txtSumRoomsAdminView = (TextView) findViewById(R.id.txt_sum_rooms_admin_view);

        lnLtRoomsAdminView = (LinearLayout) findViewById(R.id.lnLt_rooms_admin_view);
        lnLtHostsAdminView = (LinearLayout) findViewById(R.id.lnLt_hosts_admin_view);
        lnLtReportsAdminView = (LinearLayout) findViewById(R.id.lnLt_reports_admin_view);
        lnLtRoomsWaitForApprovalAdminView = (LinearLayout) findViewById(R.id.lnLt_rooms_wait_for_approval_admin_view);
        bieudo = (PieChart) findViewById(R.id.bieudo);
        logout = (TextView)  findViewById(R.id.logout);
    }

    private void setClick() {
        lnLtRoomsAdminView.setOnClickListener(this);
        lnLtHostsAdminView.setOnClickListener(this);
        lnLtReportsAdminView.setOnClickListener(this);
        lnLtRoomsWaitForApprovalAdminView.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        userController = new UserController(this);
        userController.getSumHosts(txtSumHostsAdminView);

        mainActivityController = new MainActivityController(this, UID);
        mainActivityController.getSumRooms(txtSumRoomsAdminView);

        List<myFilter> filterList = new ArrayList<>();
        detailRoomController = new DetailRoomController(this, "", filterList, UID);
        detailRoomController.getSumViews(txtSumViewsAdminView);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bieudotron(Integer.parseInt(txtSumRoomsAdminView.getText().toString().trim()), Integer.parseInt(txtSumHostsAdminView.getText().toString().trim()), Integer.parseInt(txtSumViewsAdminView.getText().toString().trim()));
            }
        }, 1800);

        
    }


    public void  bieudotron(int tongChi, int tongThu, int luotxem) {
        int time[] = {tongChi,tongThu, luotxem};
        String activity[] ={"Số phòng","Chủ phòng", "Lượt xem"};
        List<PieEntry> pieEntires = new ArrayList<>();
        for( int i = 0 ; i<time.length;i++){
            pieEntires.add(new PieEntry(time[i],activity[i]));
        }
        PieDataSet dataSet = new PieDataSet(pieEntires,"");
        dataSet.setColors(ColorTemplate.PASTEL_COLORS);
        PieData data = new PieData(dataSet);
        data.setValueTextSize(15);
        dataSet.setValueTextColor(Color.rgb(255,255,255));
        bieudo.setData(data);
        bieudo.setCenterTextSize(15);
        bieudo.setCenterTextColor(Color.rgb(62,68,103));
        bieudo.setCenterText("AETH HOUSE");
        bieudo.setDrawHoleEnabled (true);
        bieudo.setHoleRadius(43);
        bieudo.getDescription().setEnabled(false);
        bieudo.animateXY(1000, 1000);
        bieudo.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                String sales = String.valueOf(e.getY());
                Toast.makeText(adminView.this, "Giá trị là "+sales,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.lnLt_rooms_admin_view:
                Intent iRooms = new Intent(adminView.this, adminRoomsView.class);
                startActivity(iRooms);
                break;
            case R.id.lnLt_hosts_admin_view:
                Intent iHosts = new Intent(adminView.this, adminHostsView.class);
                startActivity(iHosts);
                break;
            case R.id.lnLt_reports_admin_view:
                Intent iReports = new Intent(adminView.this, adminReportRoomView.class);
                startActivity(iReports);
                break;
            case R.id.lnLt_rooms_wait_for_approval_admin_view:
                Intent iRoomsWaitForApproval = new Intent(adminView.this, adminRoomsWaitForApprovalView.class);
                startActivity(iRoomsWaitForApproval);
                break;
        }
    }
}