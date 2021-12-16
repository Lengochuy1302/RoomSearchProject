package com.example.designapptest.Controller;

import android.content.Context;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.designapptest.Adapters.AdapterRecyclerFindRoom;
import com.example.designapptest.Adapters.AdapterRecyclerFindRoomMe;
import com.example.designapptest.Controller.Interfaces.IFindRoomModel;
import com.example.designapptest.Model.FindRoomFilterModel;
import com.example.designapptest.Model.FindRoomModel;
import com.example.designapptest.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FindRoomController {
    Context context;
    FindRoomModel findRoomModel;

    // khai báo các biến liên quan tới load more.
    int quantityFindRoomLoaded = 0;
    int quantityFindRoomEachTime = 5;

    public FindRoomController(Context context) {
        this.context = context;
        findRoomModel = new FindRoomModel();
    }

    public void ListFindRoom(RecyclerView recyclerFindRoom, TextView txtResultReturn, ProgressBar progressBarFindRoom,
                             LinearLayout lnLtTopHavResultReturnFindRoom, NestedScrollView nestedScrollFindRoomView,
                             ProgressBar progressBarLoadMoreFindRoom) {
        final List<FindRoomModel> findRoomModelist = new ArrayList<>();

        FindRoomFilterModel findRoomFilterModel = null;

        //Tạo layout cho danh sách tìm phòng trọ;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerFindRoom.setLayoutManager(layoutManager);

        //Tạo adapter cho recycle view
        AdapterRecyclerFindRoom adapterRecyclerFindRoom = new AdapterRecyclerFindRoom(context, findRoomModelist, R.layout.element_list_find_room_view);
        //Cài adapter cho recycle
        recyclerFindRoom.setAdapter(adapterRecyclerFindRoom);
        ViewCompat.setNestedScrollingEnabled(recyclerFindRoom, false);
        //End tạo layout cho danh sách tìm phòng trọ.

        //Tạo interface để truyền dữ liệu lên từ model
        IFindRoomModel iFindRoomModel = new IFindRoomModel() {
            @Override
            public void getListFindRoom(FindRoomModel valueRoom) {
                // Load ảnh nén
                valueRoom.setCompressionImageFit(Picasso.get().load(valueRoom.getFindRoomOwner().getAvatar()).fit());

                //Thêm vào trong danh sách trọ
                findRoomModelist.add(valueRoom);

                //sortListFindRoom(findRoomModelist);

                //Thông báo là đã có thêm dữ liệu
                adapterRecyclerFindRoom.notifyDataSetChanged();
            }

            @Override
            public void addSuccessNotify() {

            }

            @Override
            public void getSuccessNotify(int quantity) {
                // Hiển thị kết quả trả về.
                lnLtTopHavResultReturnFindRoom.setVisibility(View.VISIBLE);
                txtResultReturn.setText(quantity + "");
            }

            @Override
            public void setProgressBarLoadMore() {
                progressBarFindRoom.setVisibility(View.GONE);
                // Ẩn progress bar load more.
                progressBarLoadMoreFindRoom.setVisibility(View.GONE);
            }
        };

        nestedScrollFindRoomView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int i1, int i2, int i3) {
                // check xem có scroll đc ko
                View child = nestedScrollView.getChildAt(0);
                if (child != null) {
                    int childHeight = child.getHeight();
                    // Nếu scroll đc
                    if (nestedScrollView.getHeight() < childHeight + nestedScrollView.getPaddingTop() + nestedScrollView.getPaddingBottom()) {
                        View lastItemView = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);

                        if (lastItemView != null) {
                            if (i1 >= lastItemView.getMeasuredHeight() - nestedScrollView.getMeasuredHeight()) {
                                // Hiển thị progress bar
                                progressBarLoadMoreFindRoom.setVisibility(View.VISIBLE);

                                quantityFindRoomLoaded += quantityFindRoomEachTime;
                                findRoomModel.ListFindRoom(iFindRoomModel, findRoomFilterModel, "",
                                        quantityFindRoomLoaded + quantityFindRoomEachTime, quantityFindRoomLoaded);
                            }
                        }
                    }
                }
            }
        });

        //Gọi hàm lấy dữ liệu trong model
        findRoomModel.ListFindRoom(iFindRoomModel, findRoomFilterModel, "",
                quantityFindRoomLoaded + quantityFindRoomEachTime, quantityFindRoomLoaded);
    }

    public void ListFindRoomFilter(RecyclerView recyclerFindRoom, FindRoomFilterModel findRoomFilterModel,
                                   TextView txtResultReturn, ProgressBar progressBarFindRoom,
                                   LinearLayout lnLtTopHavResultReturnFindRoom, NestedScrollView nestedScrollFindRoomView,
                                   ProgressBar progressBarLoadMoreFindRoom) {
        final List<FindRoomModel> findRoomModelistFilter = new ArrayList<>();

        //Tạo layout cho danh sách tìm phòng trọ;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        //((LinearLayoutManager) layoutManager).setReverseLayout(true);
        // ((LinearLayoutManager) layoutManager).setStackFromEnd(false);
        recyclerFindRoom.setLayoutManager(layoutManager);

        //Tạo adapter cho recycle view
        final AdapterRecyclerFindRoom adapterRecyclerFindRoomFilter = new AdapterRecyclerFindRoom(context, findRoomModelistFilter, R.layout.element_list_find_room_view);


        //adapterRecyclerFindRoomFilter.clearApplications();
        //Cài adapter cho recycle
        recyclerFindRoom.setAdapter(adapterRecyclerFindRoomFilter);
        ViewCompat.setNestedScrollingEnabled(recyclerFindRoom, false);
        //End tạo layout cho danh sách tìm phòng trọ.

        //Tạo interface để truyền dữ liệu lên từ model
        IFindRoomModel iFindRoomModel = new IFindRoomModel() {
            @Override
            public void getListFindRoom(FindRoomModel valueRoom) {
                // Tăng lên số lượng kết quả trả về.
                // Load ảnh nén
                valueRoom.setCompressionImageFit(Picasso.get().load(valueRoom.getFindRoomOwner().getAvatar()).fit());

                //Thêm vào trong danh sách trọ
                findRoomModelistFilter.add(valueRoom);

                //sortListFindRoom(findRoomModelistFilter);

                //Thông báo là đã có thêm dữ liệu
                adapterRecyclerFindRoomFilter.notifyDataSetChanged();
            }

            @Override
            public void addSuccessNotify() {

            }

            @Override
            public void getSuccessNotify(int quantity) {
                // Hiển thị kết quả trả về.
                lnLtTopHavResultReturnFindRoom.setVisibility(View.VISIBLE);
                txtResultReturn.setText(quantity + "");
            }

            @Override
            public void setProgressBarLoadMore() {
                // Ẩn progress bar.
                progressBarFindRoom.setVisibility(View.GONE);

                // Ẩn progress bar load more.
                progressBarLoadMoreFindRoom.setVisibility(View.GONE);
            }
        };

        nestedScrollFindRoomView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int i1, int i2, int i3) {
                // check xem có scroll đc ko
                View child = nestedScrollView.getChildAt(0);
                if (child != null) {
                    int childHeight = child.getHeight();
                    // Nếu scroll đc
                    if (nestedScrollView.getHeight() < childHeight + nestedScrollView.getPaddingTop() + nestedScrollView.getPaddingBottom()) {
                        View lastItemView = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);

                        if (lastItemView != null) {
                            if (i1 >= lastItemView.getMeasuredHeight() - nestedScrollView.getMeasuredHeight()) {
                                // Hiển thị progress bar
                                progressBarLoadMoreFindRoom.setVisibility(View.VISIBLE);

                                quantityFindRoomLoaded += quantityFindRoomEachTime;
                                findRoomModel.ListFindRoom(iFindRoomModel, findRoomFilterModel, "",
                                        quantityFindRoomLoaded + quantityFindRoomEachTime, quantityFindRoomLoaded);
                            }
                        }
                    }
                }
            }
        });

        //Gọi hàm lấy dữ liệu trong model
        findRoomModel.ListFindRoom(iFindRoomModel, findRoomFilterModel, "",
                quantityFindRoomLoaded + quantityFindRoomEachTime, quantityFindRoomLoaded);
    }

    public void ListFindRoomMine(String UID, RecyclerView recyclerFindRoomMine,
                                 TextView txtResultReturn, ProgressBar progressBarFindRoomMine,
                                 LinearLayout lnLtTopHavResultReturnFindRoomMine, NestedScrollView nestedScrollFindRoomMineView,
                                 ProgressBar progressBarLoadMoreFindRoomMine) {
        final List<FindRoomModel> findRoomModelistFilter = new ArrayList<>();

        FindRoomFilterModel findRoomFilterModel = null;

        //Tạo layout cho danh sách tìm phòng trọ;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);

        recyclerFindRoomMine.setLayoutManager(layoutManager);

        //Tạo adapter cho recycle view

        final AdapterRecyclerFindRoomMe adapterRecyclerFindRoomFilter =
                new AdapterRecyclerFindRoomMe(context, findRoomModelistFilter, R.layout.element_list_find_me_room_view);


        //adapterRecyclerFindRoomFilter.clearApplications();
        //Cài adapter cho recycle
        recyclerFindRoomMine.setAdapter(adapterRecyclerFindRoomFilter);
        ViewCompat.setNestedScrollingEnabled(recyclerFindRoomMine, false);
        //End tạo layout cho danh sách tìm phòng trọ.

        //Tạo interface để truyền dữ liệu lên từ model
        IFindRoomModel iFindRoomModel = new IFindRoomModel() {
            @Override
            public void getListFindRoom(FindRoomModel valueRoom) {
                // Tăng lên số lượng kết quả trả về.
                // Load ảnh nén
                valueRoom.setCompressionImageFit(Picasso.get().load(valueRoom.getFindRoomOwner().getAvatar()).fit());

                //Thêm vào trong danh sách trọ
                findRoomModelistFilter.add(valueRoom);

                //sortListFindRoom(findRoomModelistFilter);

                //Thông báo là đã có thêm dữ liệu
                adapterRecyclerFindRoomFilter.notifyDataSetChanged();
            }

            @Override
            public void addSuccessNotify() {

            }

            @Override
            public void getSuccessNotify(int quantity) {
                // Hiển thị kết quả trả về.
                lnLtTopHavResultReturnFindRoomMine.setVisibility(View.VISIBLE);
                txtResultReturn.setText(quantity + "");
            }

            @Override
            public void setProgressBarLoadMore() {
                progressBarFindRoomMine.setVisibility(View.GONE);
                // Ẩn progress bar load more.
                progressBarLoadMoreFindRoomMine.setVisibility(View.GONE);
            }
        };

        nestedScrollFindRoomMineView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int i1, int i2, int i3) {
                // check xem có scroll đc ko
                View child = nestedScrollView.getChildAt(0);
                if (child != null) {
                    int childHeight = child.getHeight();
                    // Nếu scroll đc
                    if (nestedScrollView.getHeight() < childHeight + nestedScrollView.getPaddingTop() + nestedScrollView.getPaddingBottom()) {
                        View lastItemView = nestedScrollView.getChildAt(nestedScrollView.getChildCount() - 1);

                        if (lastItemView != null) {
                            if (i1 >= lastItemView.getMeasuredHeight() - nestedScrollView.getMeasuredHeight()) {
                                // Hiển thị progress bar
                                progressBarLoadMoreFindRoomMine.setVisibility(View.VISIBLE);

                                quantityFindRoomLoaded += quantityFindRoomEachTime;
                                findRoomModel.ListFindRoom(iFindRoomModel, findRoomFilterModel, UID,
                                        quantityFindRoomLoaded + quantityFindRoomEachTime, quantityFindRoomLoaded);
                            }
                        }
                    }
                }
            }
        });

        //Gọi hàm lấy dữ liệu trong model
        findRoomModel.ListFindRoom(iFindRoomModel, findRoomFilterModel, UID,
                quantityFindRoomLoaded + quantityFindRoomEachTime, quantityFindRoomLoaded);
    }


}
