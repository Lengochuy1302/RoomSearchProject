package com.example.designapptest.Views;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.designapptest.Controller.FindRoomController;
import com.example.designapptest.Model.FindRoomFilterModel;
import com.example.designapptest.Model.FindRoomModel;
import com.example.designapptest.R;

import java.util.ArrayList;
import java.util.List;

public class FindRoom extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    // ID object truyen qua find room
    public final static String SHARE_FINDROOM = "FINDROOMMAIN";
    private static final int REQUEST_CODE_FILTER = 0x9345;

    ImageButton btnFindRoomAdd, btnFindRoomFilter;

    RecyclerView recyclerFindRoom;

    FindRoomController findRoomController;

    FindRoomFilterModel findRoomFilterModel;

    ProgressBar progressBarFindRoom;
    LinearLayout lnLtTopHavResultReturnFindRoom;

    // Số lượng trả về.
    TextView txtResultReturn;

    NestedScrollView nestedScrollFindRoomView;
    ProgressBar progressBarLoadMoreFindRoom;
    SwipeRefreshLayout swipeRefreshLayout;

    Toolbar toolbar;
    MenuItem menuItemFilter;

    // Biến báo load lại find room.
    boolean flagFindRoom = true;
    List<FindRoomModel> findRoomModelist = new ArrayList<>();
    //Layout
    View layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.find_room_view);
        //Hỗ trợ việc có menu cho fragment
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.find_room_view, container, false);
        swipeRefreshLayout = layout.findViewById(R.id.Swiperefreshlayout);
        initControl();
        clickFindRoomAdd();

        return layout;
    }

    private void setView() {
        // Hiện progress bar.
        progressBarFindRoom.setVisibility(View.VISIBLE);
        // Ẩn progress bar load more.
        progressBarLoadMoreFindRoom.setVisibility(View.GONE);
        // Ẩn layout kết quả trả vể.
        lnLtTopHavResultReturnFindRoom.setVisibility(View.GONE);
    }

    //Load dữ liệu vào List danh sách trong lần đầu chạy
    @Override
    public void onStart() {
        super.onStart();

        initData();

        if (flagFindRoom == true) {
            setView();

            findRoomController = new FindRoomController(getContext());
            findRoomController.ListFindRoom(recyclerFindRoom, txtResultReturn, progressBarFindRoom, lnLtTopHavResultReturnFindRoom,
                    nestedScrollFindRoomView, progressBarLoadMoreFindRoom);
            swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) FindRoom.this);

        }
    }
    //End load dữ liệu vào danh sách trong lần đầu chạy


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.find_room_menu, menu);

        menuItemFilter = menu.findItem(R.id.menu_item_filter);

        // Set trọ yêu thích ?
        menuItemFilter.setIcon(R.drawable.ic_svg_filter_24);

        menuItemFilter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                clickFindRoomFilter();

                return false;
            }
        });

    }

    private void initControl() {
        toolbar = layout.findViewById(R.id.toolbar);

        recyclerFindRoom = (RecyclerView) layout.findViewById(R.id.recycler_find_room);
        btnFindRoomAdd = (ImageButton) layout.findViewById(R.id.btn_findRooomAdd);

        progressBarFindRoom = (ProgressBar) layout.findViewById(R.id.progress_bar_find_room);
        progressBarFindRoom.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        lnLtTopHavResultReturnFindRoom = (LinearLayout) layout.findViewById(R.id.lnLt_top_haveResultReturn_find_room);
        txtResultReturn = (TextView) layout.findViewById(R.id.txt_resultReturn_find_room);

        nestedScrollFindRoomView = (NestedScrollView) layout.findViewById(R.id.nested_scroll_find_room_view);
        progressBarLoadMoreFindRoom = (ProgressBar) layout.findViewById(R.id.progress_bar_load_more_find_room);
        progressBarLoadMoreFindRoom.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void initData() {
        // Thiết lập toolbar
        if (toolbar != null) {
            ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Tìm ở ghép");
        }
    }

    // Hiển thị màn hình thêm mới tìm ở ghép
    private void clickFindRoomAdd() {
        btnFindRoomAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Phải load lại find room.
                flagFindRoom = true;

                Intent iFindRoomAdd = new Intent(getContext(), FindRoomAdd.class);
                startActivity(iFindRoomAdd);
            }
        });
    }

    // Hiển thị màn hình bộ lọc tìm phòng ở ghép
    private void clickFindRoomFilter() {
        //Bắt đầu activity find room filter
        Intent iFindRoomFilter = new Intent(getContext(), FindRoomFilter.class);
        startActivityForResult(iFindRoomFilter, REQUEST_CODE_FILTER);
    }

    // Khi kết quả được trả về từ Activity find room filter, hàm onActivityResult sẽ được gọi.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Kiểm tra requestCode có trùng với REQUEST_CODE vừa dùng
        if (requestCode == REQUEST_CODE_FILTER) {
            setView();

            // resultCode được set bởi DetailActivity
            // RESULT_OK chỉ ra rằng kết quả này đã thành công
            if (resultCode == AppCompatActivity.RESULT_OK) {
                flagFindRoom = false;
                // Nhận dữ liệu từ Intent bên find room filer
                findRoomFilterModel = data.getParcelableExtra(FindRoom.SHARE_FINDROOM);

                findRoomController = new FindRoomController(getContext());
                findRoomController.ListFindRoomFilter(recyclerFindRoom, findRoomFilterModel, txtResultReturn,
                        progressBarFindRoom, lnLtTopHavResultReturnFindRoom, nestedScrollFindRoomView, progressBarLoadMoreFindRoom);


            } else {
                // Hiện progress bar.
                progressBarFindRoom.setVisibility(View.GONE);
                // Ẩn progress bar load more.
                progressBarLoadMoreFindRoom.setVisibility(View.GONE);
                // Ẩn layout kết quả trả vể.
                lnLtTopHavResultReturnFindRoom.setVisibility(View.VISIBLE);

                txtResultReturn.setText(0 + "");

                // DetailActivity không thành công, không có data trả về.
            }
        }

    }

    @Override
    public void onRefresh() {
        findRoomController = new FindRoomController(getContext());
        findRoomController.ListFindRoom(recyclerFindRoom, txtResultReturn, progressBarFindRoom, lnLtTopHavResultReturnFindRoom,
                nestedScrollFindRoomView, progressBarLoadMoreFindRoom);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
}
