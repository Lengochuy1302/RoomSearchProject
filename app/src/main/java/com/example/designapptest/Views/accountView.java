package com.example.designapptest.Views;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.designapptest.Model.Device;
import com.example.designapptest.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class accountView extends Fragment implements View.OnClickListener {

    private Button btnEditAccount;
    private Button btnMyRoom;
    private Button btnMyFavoriteRoom;
    private Button btnMyFindRoom;
    private Button btnLogout, gopy, thongtin;
    DatabaseReference nodeRoot;
    FirebaseAuth firebaseAuth;

    //View để liên kết
    View layout;
    Toolbar toolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.account_view, container, false);


        initControl();

        return layout;
    }

    //Khởi tạo control
    private void initControl() {
        toolbar = (Toolbar) layout.findViewById(R.id.toolbar);
        btnEditAccount = (Button) layout.findViewById(R.id.btn_edit_account);
        btnMyRoom = layout.findViewById(R.id.btn_my_Room);

        btnEditAccount.setOnClickListener(this);
        btnMyRoom.setOnClickListener(this);

        btnMyFavoriteRoom = layout.findViewById(R.id.btn_my_favorite_room);
        btnMyFavoriteRoom.setOnClickListener(this);

        btnMyFindRoom = layout.findViewById(R.id.btn_my_find_room);
        btnMyFindRoom.setOnClickListener(this);

        btnLogout = layout.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(this);

        gopy = layout.findViewById(R.id.gopy);
        gopy.setOnClickListener(this);

        thongtin = layout.findViewById(R.id.thongtin);
        thongtin.setOnClickListener(this);
    }

    private void initData() {
        // Thiết lập toolbar
        if (toolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Tài khoản");
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_edit_account:
                Intent intent = new Intent(getContext(), personalPage.class);
                startActivity(intent);
                break;
            case R.id.btn_my_Room:
                Intent intent1 = new Intent(getContext(), roomManagementModel.class);
                startActivity(intent1);
                break;

            case R.id.btn_my_favorite_room:
                Intent intentFavoriteRooms = new Intent(getContext(), favoriteRoomsView.class);
                startActivity(intentFavoriteRooms);
                break;

            case R.id.btn_my_find_room:
                Intent intentMyFindRooms = new Intent(getContext(), FindRoomMine.class);
                startActivity(intentMyFindRooms);
                break;

            case R.id.gopy:
                Intent gopY = new Intent(getContext(), FeedbackScreen.class);
                startActivity(gopY);
                break;

            case R.id.thongtin:
                Intent profile = new Intent(getContext(), ProfileApp.class);
                startActivity(profile);
                break;

            case R.id.btn_logout:
                //Khởi tạo firebaseAuth
                firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user == null) {
                    return;
                }

                Device device = new Device();
                String iddevice = "";
                device.setID("ThietBi");
                device.setTenDevice(iddevice);
                nodeRoot = FirebaseDatabase.getInstance().getReference("users").child(user.getUid()).child("DeviceID");
                nodeRoot.child("ThietBi").setValue(device);
                //Text Đăng xuất
                firebaseAuth.signOut();
               getContext().startActivity(new Intent(getContext(), onBarLoading.class));

        }
    }
}
