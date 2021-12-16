package com.example.designapptest.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.designapptest.Controller.FindRoomController;
import com.example.designapptest.Model.FindRoomFilterModel;
import com.example.designapptest.R;
import com.github.clans.fab.FloatingActionButton;

public class FindRoomMine extends AppCompatActivity {
    // ID object truyen qua find room

    RecyclerView recyclerFindRoomMine;

    FindRoomController findRoomController;

    FindRoomFilterModel findRoomFilterModel;

    ProgressBar progressBarFindRoomMine;
    LinearLayout lnLtTopHavResultReturnFindRoomMine;

    // Số lượng trả về.
    TextView txtResultReturn;
    FloatingActionButton actionButton;

    NestedScrollView nestedScrollFindRoomMineView;
    ProgressBar progressBarLoadMoreFindRoom;

    Toolbar toolbar;

    // Biến báo load lại find room.
    boolean flagFindRoom = true;

    SharedPreferences sharedPreferences;
    String UID;

    //Layout
    View layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_room_mine_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Lấy id của người dùng hiện tại
        sharedPreferences = getSharedPreferences(LoginView.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginView.SHARE_UID, "n1oc76JrhkMB9bxKxwXrxJld3qH2");

        initControl();
        clickFindRoomAdd();
    }

    private void setView() {
        // Hiện progress bar.
        progressBarFindRoomMine.setVisibility(View.VISIBLE);
        // Ẩn progress bar load more.
        progressBarLoadMoreFindRoom.setVisibility(View.GONE);
        // Ẩn layout kết quả trả vể.
        lnLtTopHavResultReturnFindRoomMine.setVisibility(View.GONE);
    }

    //Load dữ liệu vào List danh sách trong lần đầu chạy
    @Override
    public void onStart() {
        super.onStart();

        initData();

        setView();

        findRoomController = new FindRoomController(this);
        findRoomController.ListFindRoomMine(UID, recyclerFindRoomMine, txtResultReturn, progressBarFindRoomMine,
                lnLtTopHavResultReturnFindRoomMine, nestedScrollFindRoomMineView, progressBarLoadMoreFindRoom);
    }
    //End load dữ liệu vào danh sách trong lần đầu chạy

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void loadlisst() {
        findRoomController = new FindRoomController(this);
        findRoomController.ListFindRoomMine(UID, recyclerFindRoomMine, txtResultReturn, progressBarFindRoomMine,
                lnLtTopHavResultReturnFindRoomMine, nestedScrollFindRoomMineView, progressBarLoadMoreFindRoom);
    }

    private void initControl() {
       toolbar = (Toolbar) findViewById(R.id.toolbar);


        recyclerFindRoomMine = (RecyclerView) findViewById(R.id.recycler_find_room_mine);
        actionButton = (FloatingActionButton) findViewById(R.id.themoghep);
        progressBarFindRoomMine = (ProgressBar) findViewById(R.id.progress_bar_find_room_mine);
        progressBarFindRoomMine.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        lnLtTopHavResultReturnFindRoomMine = (LinearLayout) findViewById(R.id.lnLt_top_haveResultReturn_find_room_mine);
        txtResultReturn = (TextView) findViewById(R.id.txt_resultReturn_find_room_mine);

        nestedScrollFindRoomMineView = (NestedScrollView) findViewById(R.id.nested_scroll_find_room_mine_view);
        progressBarLoadMoreFindRoom = (ProgressBar) findViewById(R.id.progress_bar_load_more_find_room_mine);
        progressBarLoadMoreFindRoom.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void clickFindRoomAdd() {
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Phải load lại find room.

                Intent iFindRoomAdd = new Intent(FindRoomMine.this, FindRoomAdd.class);
                startActivity(iFindRoomAdd);
            }
        });
    }

    private void initData() {
        // Gán các giá trị toolbar.
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Tìm ở ghép của tôi");
        }
    }

}
