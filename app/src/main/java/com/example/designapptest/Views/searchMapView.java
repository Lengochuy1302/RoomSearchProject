package com.example.designapptest.Views;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.example.designapptest.Controller.searchMapViewController;
import com.example.designapptest.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;


public class searchMapView extends Fragment implements GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraIdleListener,
        OnMapReadyCallback {

    View layout;
    GoogleMap mMap;
    //Biến lưu tọa độ zoom đến vị trí trên map
    double latitude = 10.776927;
    double longtitude = 106.637588;

    ProgressBar progessBarLoadMap;
    ProgressBar progessBarLoadRoom;

    searchMapViewController searchMapViewController;

    RecyclerView recyclerRoom;

    CardView cardViewListRoom;

    FloatingSearchView searchView;

    FloatingActionButton mapvetinh, mapthuong;

    com.google.android.gms.maps.SupportMapFragment mapFragment;

    private boolean isFirstLoad = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layout = inflater.inflate(R.layout.fragment_search_map_view, container, false);
        initControl();
        searchMapViewController = new searchMapViewController(getContext());
        mapFragment = (com.google.android.gms.maps.SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync((OnMapReadyCallback) searchMapView.this);
        mapvetinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(mMap.MAP_TYPE_SATELLITE);
            }
        });

        mapthuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(mMap.MAP_TYPE_NORMAL);
            }
        });
        requestPermission();
        return layout;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private void initControl(){
        progessBarLoadMap= layout.findViewById(R.id.progess_bar_load_map);
        //Đổi màu progessBar

        progessBarLoadRoom=layout.findViewById(R.id.progess_bar_load_room);
        //Đổi màu progessBar
        progessBarLoadRoom.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
        //Ẩn
        progessBarLoadRoom.setVisibility(View.GONE);

        progessBarLoadMap.setVisibility(View.GONE);

        recyclerRoom = layout.findViewById(R.id.recycler_room);

        cardViewListRoom = layout.findViewById(R.id.cardViewListRoom);
        mapvetinh = layout.findViewById(R.id.mapvetinh);
        mapthuong = layout.findViewById(R.id.mapthuong);
        searchView = layout.findViewById(R.id.floating_search_view);

        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                String location = currentQuery;
                List<Address> addresses = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(getContext());
                    try {
                        addresses = geocoder.getFromLocationName(location,1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addresses.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                }
            }
        });
    }

//    //Gọi hàm lấy tọa độ từ controlller
    private void callListRoomLocation(){
        searchMapViewController.listLocationRoom(mMap);
    }
//
    private void callListInfoRoom(List<String> listRoomID){
        progessBarLoadRoom.setVisibility(View.VISIBLE);
        searchMapViewController.listInfoRoom(recyclerRoom,listRoomID,progessBarLoadRoom);
    }
//
    //Hàm ẩn/ hiện card hiện thông tin chi tiết phòng
    private void AnimationVisibilityCardView(boolean visibility){
        if (visibility){
            if(cardViewListRoom.getVisibility()!=View.VISIBLE){
                Animation slideUp = AnimationUtils.loadAnimation(getContext(),R.anim.translate_bottom_side);
                cardViewListRoom.setVisibility(View.VISIBLE);
                cardViewListRoom.startAnimation(slideUp);
            }
        }else {
            if(cardViewListRoom.getVisibility()==View.VISIBLE){
                Animation slideDown = AnimationUtils.loadAnimation(getContext(),R.anim.traslate_top_side);
                cardViewListRoom.setVisibility(View.GONE);
                cardViewListRoom.startAnimation(slideDown);
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMapType(googleMap.MAP_TYPE_TERRAIN);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
        TuiDangODau();
        callListRoomLocation();
    }

    private void TuiDangODau() {

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(10.835212, 106.6850703), 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(10.835212, 106.6850703))      // Sets the center of the map to location user
                .zoom(11)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    @Override
    public void onCameraIdle() {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        AnimationVisibilityCardView(false);

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //Lấy ra marker chạm vào

                List<String> listRoomID = new ArrayList<String>();
                listRoomID.add(marker.getSnippet());

                        if(listRoomID.size()>0){
                            //Hiển thị card hiển thị danh sách phòng
                            AnimationVisibilityCardView(true);
                            //Gọi hàm đổ dữ liệu từ controller
                            callListInfoRoom(listRoomID);
                        }
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }
}
