package com.example.designapptest.Views;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.example.designapptest.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapGesture;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.SupportMapFragment;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.GeocodeRequest;
import com.here.android.mpa.search.GeocodeResult;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.ReverseGeocodeRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;

public class PopupChooseLocation extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener, GoogleMap.OnMapLongClickListener,
        OnMapReadyCallback {

    Button btnExit;
    com.google.android.gms.maps.SupportMapFragment mapFragment;
    GoogleMap mMap;
    FloatingSearchView searchView;
    FloatingActionButton mapvetinh, mapthuong;
    //Biến lưu tọa độ để truyền về lại màn hình đăng phòng
    double latitude = 10.776927;
    double longtitude = 106.637588;

    //Biến lưu địa chỉ để truyền về lại màn hình đăng phòng
    //Quận
    String District = "";

    //Đường
    String Street = "";

    //Thành phố
    String City = "";

    //Số nhà
    String No = "";
    MarkerOptions marker;

    TextView txtStreet, txtWard, txtDistrict, txtNo;
    Geocoder geocoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_choose_location);
        //Thay đổi kích thước của màn hình
        initControl();
        mapFragment = (com.google.android.gms.maps.SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync((OnMapReadyCallback) PopupChooseLocation.this);
        //Lấy ra Intent
        Intent intent = getIntent();
        if (intent != null) {
            //Lấy ra tọa độ
            latitude = intent.getDoubleExtra(postRoomStep1.SHARE_LATITUDE, 0.0);
            longtitude = intent.getDoubleExtra(postRoomStep1.SHARE_LONGTITUDE, 0.0);
        }
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

//        checkPermissions();

    }

    private void updateView() {
        txtDistrict.setText(District.equals("") ? "Không rõ" : District);
        txtWard.setText("Không rõ");
        txtNo.setText(No.equals("") ? "Không rõ" : No);
        txtStreet.setText(Street.equals("") ? "Không rõ" : Street);
    }

    private void initControl() {
        txtDistrict = findViewById(R.id.txt_district);
        txtNo = findViewById(R.id.txt_no);
        txtStreet = findViewById(R.id.txt_street);
        txtWard = findViewById(R.id.txt_ward);
        mapthuong = findViewById(R.id.mapthuong);
        mapvetinh = findViewById(R.id.mapvetinh);
        btnExit = findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Kiểm tra nếu người dùng không chọn vào Thành phố Hồ chí minh
                if (City.contains("Hồ Chí Minh")) {
                    Intent intent = new Intent();
//                    latitude = marker.getCoordinate().getLatitude();
//                    longtitude = marker.getCoordinate().getLongitude();

                    //Truyền về thông tin hiện tại trên map
                    intent.putExtra(postRoomStep1.SHARE_LATITUDE, latitude);
                    intent.putExtra(postRoomStep1.SHARE_LONGTITUDE, longtitude);

                    //Thông tin về vị trí vật lý
                    intent.putExtra(postRoomStep1.SHARE_DISTRICT, District);
                    intent.putExtra(postRoomStep1.SHARE_STREET, Street);
                    intent.putExtra(postRoomStep1.SHARE_NO, No);

                    //End truyền về thông tin hiện tại trên map

                    setResult(AppCompatActivity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(PopupChooseLocation.this, "Vui lòng chọn các địa chỉ ở HCM", Toast.LENGTH_LONG).show();
                }

            }
        });

        searchView = findViewById(R.id.floating_search_view);

        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {

            }

            @Override
            public void onSearchAction(String currentQuery) {
                String location = currentQuery;
                List<Address> addresses = null;
                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(PopupChooseLocation.this);
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

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(PopupChooseLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PopupChooseLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        mMap.setOnMyLocationClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapLongClickListener(this);
        mMap.setMapType(googleMap.MAP_TYPE_TERRAIN);
        TuiDangODau();
    }

    private void TuiDangODau() {

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(10.835212, 106.6850703), 14));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(10.835212, 106.6850703))      // Sets the center of the map to location user
                    .zoom(11)                   // Sets the zoom
                    .bearing(40)                // Sets the orientation of the camera to east
                    .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        Geocoder geocoder = new Geocoder(PopupChooseLocation.this, Locale.getDefault());
        try {
            List<Address> addressesc = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            MarkerOptions option = new MarkerOptions();
            option.position(latLng);
            option.title(addressesc.get(0).getAddressLine(0));
            Marker currentMarker= mMap.addMarker(option);
            currentMarker.showInfoWindow();
            try {
                List<Address> addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                City = addresses.get(0).getAdminArea();
                longtitude = latLng.longitude;
                latitude = latLng.latitude;
                updateView();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
