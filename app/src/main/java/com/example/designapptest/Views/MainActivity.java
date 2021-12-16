package com.example.designapptest.Views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.example.designapptest.Controller.MainActivityController;
import com.example.designapptest.Model.RoomModel;
import com.example.designapptest.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.MODE_PRIVATE;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class MainActivity extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    RecyclerView recyclerMainRoom;
    RecyclerView recyclerGridMainRoom;
    MainActivityController mainActivityController;
    ProgressBar progressBarMain;

    NestedScrollView nestedScrollMainView;
    Button btnLoadMoreVerifiedRooms;
    ProgressBar progressBarLoadMoreGridMainRoom;

   // GridView grVRoom;
    GridView grVLocation;

    EditText edTSearch;

    //Them vao de test
    FusedLocationProviderClient client;
    SwipeRefreshLayout swipeRefreshLayout;

    SharedPreferences sharedPreferences;
    String UID;

    CollapsingToolbarLayout collapsingToolbarLayout;

    FloatingActionButton btnFabSearch;

    //Layout
    View layout;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    private List<Photo> photos;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (viewPager.getCurrentItem() == photos.size() - 1) {
                viewPager.setCurrentItem(0);
            } else {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
            }
        }
    };
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(LoginView.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginView.SHARE_UID,"n1oc76JrhkMB9bxKxwXrxJld3qH2");
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.activity_main, container, false);

        initControl();
        swipeRefreshLayout = layout.findViewById(R.id.Swiperefreshlayout);
        viewPager = layout.findViewById(R.id.view_pager);
        circleIndicator = layout.findViewById(R.id.cicer);
        photos = getPhotos();
        photoViewPager pager = new photoViewPager(photos);
        viewPager.setAdapter(pager);
        circleIndicator.setViewPager(viewPager);

        handler.postDelayed(runnable, 4000);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 4000);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        clickSearchRoom();
        clickLoadMoreVerifiedRooms();

        return layout;
    }
    private List<Photo> getPhotos() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.a03));
        list.add(new Photo(R.drawable.a00012));
        list.add(new Photo(R.drawable.cccaaa111));
        list.add(new Photo(R.drawable.a02));
        list.add(new Photo(R.drawable.logoo1));
        list.add(new Photo(R.drawable.ab119d66f4ba415fd5a));
        return list;
    }



    private void initControl() {
        grVLocation = (GridView) layout.findViewById(R.id.grV_location);

        edTSearch = (EditText) layout.findViewById(R.id.edT_search);
        nestedScrollMainView = (NestedScrollView) layout.findViewById(R.id.nested_scroll_main_view);
        btnLoadMoreVerifiedRooms = (Button) layout.findViewById(R.id.btn_load_more_verified_rooms);
        progressBarLoadMoreGridMainRoom = (ProgressBar) layout.findViewById(R.id.progress_bar_grid_main_rooms);
        progressBarLoadMoreGridMainRoom.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        recyclerMainRoom = (RecyclerView)layout.findViewById(R.id.recycler_Main_Room);
        recyclerGridMainRoom = (RecyclerView)layout.findViewById(R.id.recycler_Grid_Main_Room);
        progressBarMain = (ProgressBar)layout.findViewById(R.id.Progress_Main);
        progressBarMain.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        btnFabSearch = layout.findViewById(R.id.btn_fab_search);
        btnFabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("checkclick", "onClick: ");
                Intent intentSearchLocation = new Intent(getContext(),location_search.class);
                startActivity(intentSearchLocation);
            }
        });
    }

    private void setView() {
        // Hiển thị progress bar main
        progressBarMain.setVisibility(View.VISIBLE);
        // Ẩn nút Xem thêm phòng đã xác nhận
        btnLoadMoreVerifiedRooms.setVisibility(View.GONE);
        // Ẩn progress bar load more grid main rooms
        progressBarLoadMoreGridMainRoom.setVisibility(View.GONE);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(getActivity(),new String[]{ACCESS_FINE_LOCATION},1);
    }


    private void clickSearchRoom(){
        edTSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("checkclick", "onClick: ");
                Intent intentSearchLocation = new Intent(getContext(),location_search.class);
                startActivity(intentSearchLocation);
            }
        });
    }

    private void clickLoadMoreVerifiedRooms() {
        btnLoadMoreVerifiedRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVerifiedRooms = new Intent(getContext(), verifiedRoomsView.class);
                startActivity(intentVerifiedRooms);
            }
        });
    }

    //Load dữ liệu vào List danh sách trong lần đầu chạy
    @Override
    public void onStart() {
        super.onStart();

        setView();

        // detail room dùng
        RoomModel.getListFavoriteRoomsId(UID);

        mainActivityController = new MainActivityController(getContext(), UID);
        mainActivityController.ListMainRoom(recyclerMainRoom, recyclerGridMainRoom, progressBarMain,
                nestedScrollMainView, btnLoadMoreVerifiedRooms, progressBarLoadMoreGridMainRoom);

        //Load top địa điểm nhiều phòng
        mainActivityController.loadTopLocation(grVLocation);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerMainRoom.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerMainRoom);


        ItemTouchHelper itemTouchHelperu = new ItemTouchHelper(simpleCallback);
        itemTouchHelperu.attachToRecyclerView(recyclerGridMainRoom);

        swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) MainActivity.this);
    }
    //End load dữ liệu vào danh sách trong lần đầu chạy


    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 4000);
    }

    @Override
    public void onRefresh() {
        setView();

        // detail room dùng
        RoomModel.getListFavoriteRoomsId(UID);

        mainActivityController = new MainActivityController(getContext(), UID);
        mainActivityController.ListMainRoom(recyclerMainRoom, recyclerGridMainRoom, progressBarMain,
                nestedScrollMainView, btnLoadMoreVerifiedRooms, progressBarLoadMoreGridMainRoom);

        //Load top địa điểm nhiều phòng
        mainActivityController.loadTopLocation(grVLocation);
        swipeRefreshLayout.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) MainActivity.this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }
}
