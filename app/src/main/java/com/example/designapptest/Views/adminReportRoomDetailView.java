package com.example.designapptest.Views;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.designapptest.Model.ReportedRoomModel;
import com.example.designapptest.Model.RoomModel;
import com.example.designapptest.Model.UserModel;
import com.example.designapptest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class adminReportRoomDetailView extends AppCompatActivity {
    Toolbar toolbar;
    DatabaseReference nodeRoot;
    ReportedRoomModel reportedRoomModel;
    CircleImageView img_avt_comment_room_detail;
    TextView txtRoomType, txtRoomName, txtRoomAddress, txtRoomPhoneNumber, txtTimeReport, txtUserReport,
    txtReasonReport, txtDetailReport, deleteroom, ten_repost, so_dt, tenchu;
    List<UserModel> UserModelList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reported_room_detail_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        reportedRoomModel = getIntent().getParcelableExtra("reportRoom");

        initControl();

        tenchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nodeRoot = FirebaseDatabase.getInstance().getReference("Users").child(reportedRoomModel.getReportedRoom().getOwner());
                nodeRoot.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        Intent intent = new Intent(adminReportRoomDetailView.this, HostRoomUser.class);
                        intent.putExtra("Key_1", ""+ reportedRoomModel.getReportedRoom().getOwner());
                        intent.putExtra("Key_2", ""+ name);
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        deleteroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(adminReportRoomDetailView.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Bạn muốn xóa phòng này?")
                        .setContentText("Có phải bạn đang rất bực bội khi phải làm hành động này, đừng nóng chúng ta có thể xem xét lại!")
                        .setCancelText("Hủy bỏ")
                        .setConfirmText("Xóa")
                        .showCancelButton(true)
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                RoomModel modelFunction = new RoomModel();
                                modelFunction.DeleteRoom(reportedRoomModel.getReportedRoom().getRoomID());
                                SweetAlertDialog pDialog = new SweetAlertDialog(adminReportRoomDetailView.this, SweetAlertDialog.PROGRESS_TYPE);
                                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                                pDialog.setTitleText("Đang xóa!");
                                pDialog.setCancelable(false);
                                pDialog.show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pDialog.dismiss();
                                        new SweetAlertDialog(adminReportRoomDetailView.this, SweetAlertDialog.SUCCESS_TYPE)
                                                .setTitleText("Xóa thành công")
                                                .show();
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(adminReportRoomDetailView.this, adminReportRoomView.class);
                                                startActivity(intent);
                                            }
                                        }, 1000);

                                    }
                                }, 3000);

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
        });
    }

    private void initControl() {
        toolbar = findViewById(R.id.toolbar);
        deleteroom = (TextView) findViewById(R.id.deleteroom);
        txtRoomType = (TextView) findViewById(R.id.txt_roomType);
        txtRoomName = (TextView) findViewById(R.id.txt_roomName);
        txtRoomAddress = (TextView) findViewById(R.id.txt_roomAddress);
        txtRoomPhoneNumber = (TextView) findViewById(R.id.txt_roomPhoneNumber);
        txtTimeReport = (TextView) findViewById(R.id.txt_time_report);
        txtUserReport = (TextView) findViewById(R.id.txt_user_report);
        txtReasonReport = (TextView) findViewById(R.id.txt_reason_report);
        txtDetailReport = (TextView) findViewById(R.id.txt_detail_report);
        ten_repost = (TextView) findViewById(R.id.ten_repost);
        so_dt = (TextView) findViewById(R.id.so_dt);
        tenchu = (TextView) findViewById(R.id.tenchu);
        img_avt_comment_room_detail = findViewById(R.id.img_avt_comment_room_detail);
    }


    @Override
    protected void onStart() {
        super.onStart();

        initData();


    }

    private void initData() {
        // Thiết lập toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Chi tiết báo cáo phòng trọ");
        }
        txtRoomType.setText(reportedRoomModel.getReportedRoom().getRoomType());
        txtRoomName.setText(reportedRoomModel.getReportedRoom().getName());


        //Set address for room
        String longAddress = reportedRoomModel.getReportedRoom().getApartmentNumber() + " "
                + reportedRoomModel.getReportedRoom().getStreet() + ", "
                + reportedRoomModel.getReportedRoom().getWard() + ", "
                + reportedRoomModel.getReportedRoom().getCounty() + ", "
                + reportedRoomModel.getReportedRoom().getCity();
        txtRoomAddress.setText(longAddress);
        //End Set address for room

        txtRoomPhoneNumber.setText(reportedRoomModel.getReportedRoom().getRoomOwner().getPhoneNumber());
        txtUserReport.setText(reportedRoomModel.getUserReport().getEmail());
        txtTimeReport.setText(reportedRoomModel.getTime());
        so_dt.setText(reportedRoomModel.getUserReport().getPhoneNumber());
        ten_repost.setText(reportedRoomModel.getUserReport().getName());
        txtReasonReport.setText(reportedRoomModel.getReason());
        txtDetailReport.setText(reportedRoomModel.getDetail());

        nodeRoot = FirebaseDatabase.getInstance().getReference("Users").child(reportedRoomModel.getReportedRoom().getOwner());
        nodeRoot.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String photoUrl = dataSnapshot.child("avatar").getValue(String.class);
                if (photoUrl != null) {
                    Picasso.get().load(photoUrl).into(img_avt_comment_room_detail);
                }
                tenchu.setText(name);
            }
            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
