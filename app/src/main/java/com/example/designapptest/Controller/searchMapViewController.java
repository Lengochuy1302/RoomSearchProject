package com.example.designapptest.Controller;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.designapptest.Adapters.AdapterRecyclerMainRoom;
import com.example.designapptest.Controller.Interfaces.IMapRoomModel;
import com.example.designapptest.Controller.Interfaces.IMapViewModel;
import com.example.designapptest.Controller.Interfaces.IStringCallBack;
import com.example.designapptest.Model.LocationModel;
import com.example.designapptest.Model.MapRoomModel;
import com.example.designapptest.Model.RoomModel;
import com.example.designapptest.R;
import com.example.designapptest.Views.searchMapView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapMarker;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class searchMapViewController {

    LocationModel locationModel;
    MapRoomModel mapRoomModel;
    Context context;
    RoomModel roomModel;
    Bitmap bitmap;
    public searchMapViewController(Context context){
        mapRoomModel = new MapRoomModel();
        roomModel = new RoomModel();
        this.context=context;
        locationModel = new LocationModel();
    }

    public void listLocationRoom(GoogleMap map){
        IMapRoomModel iMapRoomModel = new IMapRoomModel() {
            @Override
            public void getListLocationRoom(RoomModel valueRoom) {

                //Custom marker with image

                String typeRoom = valueRoom.getTypeID();
                switch (typeRoom){
                    case "RTID0":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.offices);
                        break;
                    case "RTID1":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ihome);
                        break;
                    case "RTID2":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.aaaaaaaaaaaaaaaaaa);
                        break;
                    case "RTID3":
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tent);
                        break;
                }

                //Thêm vào marker tương ứng với mỗi tọa độ trên map
                LatLng roomLocation = new LatLng(valueRoom.getLatitude(), valueRoom.getLongtitude());
                String longAddress = valueRoom.getApartmentNumber() + " " + valueRoom.getStreet() + ", "
                        + valueRoom.getWard() + ", " + valueRoom.getCounty() + ", " + valueRoom.getCity();
                MarkerOptions defaultMarker = new MarkerOptions();
                defaultMarker.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                defaultMarker.title(longAddress);
                defaultMarker.snippet(valueRoom.getRoomID());
                defaultMarker.position(roomLocation);
                map.addMarker(defaultMarker);

            }
        };

        mapRoomModel.listLocationRoom(iMapRoomModel);
    }

    public void listInfoRoom(RecyclerView recyclerView, List<String> listRoomID, ProgressBar progressBar){
        final List<RoomModel> roomModelList = new ArrayList<>();

        //Tạo layout cho danh sách trọ
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        //Tạo adapter cho recycle view
        final AdapterRecyclerMainRoom adapterRecyclerMainRoom = new AdapterRecyclerMainRoom(context, roomModelList, R.layout.room_element_list_view,"");
        //Cài adapter cho recycle
        recyclerView.setAdapter(adapterRecyclerMainRoom);

        IMapViewModel iMapViewModel = new IMapViewModel() {
            @Override
            public void getListRoom(RoomModel valueRoom) {
                i++;
                valueRoom.setCompressionImageFit(Picasso.get().load(valueRoom.getCompressionImage()).fit());
                roomModelList.add(valueRoom);
                adapterRecyclerMainRoom.notifyDataSetChanged();
                if(i==listRoomID.size()){
                    progressBar.setVisibility(View.GONE);
                }
            }

            int i = 0;

        };

        roomModel.SendData_NoLoadMore(listRoomID,iMapViewModel);
    }

//    public void TopLocation(Fragment currentFragment){
//        Log.d("check3", "contro");
//        IStringCallBack iStringCallBack = new IStringCallBack() {
//            @Override
//            public void sendString(String value) {
//                //Zoom map đến vị trí có nhiều
//                ((searchMapView)currentFragment).Search(value);
//            }
//        };
//
//        locationModel.Top_1_Location(iStringCallBack);
//    }

}
