package com.example.designapptest.Views;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.designapptest.Adapters.AdapterRecyclerComment;
import com.example.designapptest.Adapters.AdapterRecyclerConvenient;
import com.example.designapptest.Adapters.AdapterRecyclerRoomPrice;
import com.example.designapptest.Adapters.AdapterViewPagerImageShow;
import com.example.designapptest.ClassOther.GenderFilter;
import com.example.designapptest.ClassOther.TypeFilter;
import com.example.designapptest.ClassOther.classFunctionStatic;
import com.example.designapptest.ClassOther.myFilter;
import com.example.designapptest.Controller.CommentController;
import com.example.designapptest.Controller.DetailRoomController;
import com.example.designapptest.Controller.MainActivityController;
import com.example.designapptest.Controller.ReportedRoomController;
import com.example.designapptest.Model.CommentModel;
import com.example.designapptest.Model.ReportedRoomModel;
import com.example.designapptest.Model.RoomModel;
import com.example.designapptest.Model.RoomViewsModel;
import com.example.designapptest.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.imaginativeworld.oopsnointernet.callbacks.ConnectionCallback;
import org.imaginativeworld.oopsnointernet.dialogs.signal.DialogPropertiesSignal;
import org.imaginativeworld.oopsnointernet.dialogs.signal.NoInternetDialogSignal;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class detailRoom extends AppCompatActivity implements ReportRoomDialog.ReportRoomDialogListener {
    TextView txtRoomType, txtRoomMaxNumber, txtQuantityComment, txtRoomName,
            txtRoomPrice, txtRoomStatus, txtRoomArea, txtRoomAddress, txtRoomDescription,
            txtRoomGreatReview, txtRoomPrettyGoodReview, txtRoomMediumReview, txtRoomBadReview,
            txtQuantityComment_2, txtRoomPhoneNumber, txtExpandConvenients, txtExpandDescription,
            txtCapacityCramped, txtCapacityMedium, txtCapacitySpacious, tenchu;

    Button btnCallPhone, btnDirectMap, btnPostComment, btnViewAll;

    ImageView imgRoomGender, imgRoom1, imgRoom2, imgRoom3, imgRoom4, imgFavorite;
    CircleImageView img_avt_comment_room_detail;
    List<ImageView> listImageRoom;
    final NumberFormat fmd = new DecimalFormat("#,###");
    // Các recycler.
    RecyclerView recyclerCommentRoomDetail;
    AdapterRecyclerComment adapterRecyclerComment;

    RecyclerView recyclerConvenientsRoomDetail;
    AdapterRecyclerConvenient adapterRecyclerConvenient;

    // same room
    RecyclerView recyclerSameDetailRoom;
    ProgressBar progressBarSameDetailRoom;
    LinearLayout lnLtQuantityTopSameDetailRoom;

    // Số lượng trả về.
    TextView txtQuantitySameDetailRoom;

    NestedScrollView nestedScrollSameDetailRoomView;
    ProgressBar progressBarLoadMoreSameDetailRoom;
    // end same room

    RecyclerView recyclerPriceRoomDetail;
    AdapterRecyclerRoomPrice adapterRecyclerRoomPrice;

    RoomModel roomModel;

    // Hiển thị hình ảnh phòng trọ chế độ xem.
    Dialog dialogShowImage;
    Button btnCloseShowImage;
    ViewPager viewPagerShowImage;
    TextView txtPositionImage, txtMoreImg;

    int maxImageInRoom;
    int indexImage;

    SharedPreferences sharedPreferences;
    String UID;
    Boolean isAdmin;
    CommentController commentController;
    MainActivityController mainActivityController;
    ReportedRoomController reportedRoomController;

    FrameLayout frLoutContain;
    LinearLayout lnLtExpandConvenients;
    LinearLayout lnLtExpandDescription;

    Toolbar toolbar;
    MenuItem menuItemFavorite;
    MenuItem menuItemReport;
    DatabaseReference nodeRoot;
    String District;
    String CurrentRoomID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_detail_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        roomModel = getIntent().getParcelableExtra("phongtro");
        District = roomModel.getCounty();
        CurrentRoomID = roomModel.getRoomID();


        initControl();
        isOnline();

        loadProgress();

        setMoreImageForLastCard();
        tenchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(detailRoom.this, HostRoomUser.class);
                intent.putExtra("Key_1", ""+ roomModel.getOwner());
                intent.putExtra("Key_2", ""+ roomModel.getRoomOwner().getName());
                startActivity(intent);

            }
        });
        sharedPreferences = getSharedPreferences(LoginView.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginView.SHARE_UID, "n1oc76JrhkMB9bxKxwXrxJld3qH2");
        isAdmin = sharedPreferences.getBoolean(LoginView.IS_ADMIN, false);
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

    @Override
    protected void onStart() {
        super.onStart();

        initData();

        clickCallPhone();

        clickDirectMap();

        clickPostComment();

        clickShowImage();

        setViewSameRoom();

        loadTheSameRoom();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.room_detail_menu, menu);

        menuItemFavorite = menu.findItem(R.id.menu_item_favorite);
        menuItemReport = menu.findItem(R.id.menu_item_report);

        // Set trọ yêu thích ?
        menuItemFavorite.setIcon(R.drawable.ic_favorite_border_white);
        for (String roomId : RoomModel.ListFavoriteRoomsID) {
            if (roomId.equals(roomModel.getRoomID())) {
                menuItemFavorite.setIcon(R.drawable.ic_favorite_full_white);
                break;
            }
        }

        menuItemReport.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                openReportDialog();

                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_report:
                return true;
            case R.id.menu_item_favorite:
                clickAddToFavorite();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openReportDialog() {
        ReportRoomDialog reportRoomDialog = new ReportRoomDialog();
        reportRoomDialog.show(getSupportFragmentManager(), "report room dialog");
    }

    @Override
    public void applyText(String reasonReportRoom, String detailedReasonReportRoom) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());

        ReportedRoomModel reportedRoomModel = new ReportedRoomModel();
        reportedRoomModel.setReason(reasonReportRoom);
        reportedRoomModel.setDetail(detailedReasonReportRoom);
        reportedRoomModel.setTime(date);
        reportedRoomModel.setUserID(UID);

        reportedRoomController.addReport(reportedRoomModel, roomModel.getRoomID());
    }

    private void loadProgress() {
        for (ImageView imageView : listImageRoom)
            classFunctionStatic.showProgress(this, imageView);
    }

    // Khởi tạo các control trong room detail.
    private void initControl() {
        commentController = new CommentController(this, UID);
        reportedRoomController = new ReportedRoomController(this);
        mainActivityController = new MainActivityController(this, UID);

        toolbar = findViewById(R.id.toolbar);
        img_avt_comment_room_detail = findViewById(R.id.img_avt_comment_room_detail);
        tenchu = findViewById(R.id.tenchu);
        txtRoomType = (TextView) findViewById(R.id.txt_roomType);
        txtRoomMaxNumber = (TextView) findViewById(R.id.txt_roomMaxNumber);
        txtQuantityComment = (TextView) findViewById(R.id.txt_quantityComment);
        txtRoomName = (TextView) findViewById(R.id.txt_roomName);
        txtRoomPrice = (TextView) findViewById(R.id.txt_roomPrice);
        txtRoomStatus = (TextView) findViewById(R.id.txt_roomStatus);
        txtRoomArea = (TextView) findViewById(R.id.txt_roomArea);
        txtRoomAddress = (TextView) findViewById(R.id.txt_roomAddress);
        txtRoomDescription = (TextView) findViewById(R.id.txt_roomDescription);
        txtRoomGreatReview = (TextView) findViewById(R.id.txt_roomGreatReview);
        txtRoomPrettyGoodReview = (TextView) findViewById(R.id.txt_roomPrettyGoodReview);
        txtRoomMediumReview = (TextView) findViewById(R.id.txt_roomMediumReview);
        txtRoomBadReview = (TextView) findViewById(R.id.txt_roomBadReview);
        txtQuantityComment_2 = (TextView) findViewById(R.id.txt_quantityComment_2);
        txtRoomPhoneNumber = (TextView) findViewById(R.id.txt_room_phonenumber);
        txtExpandConvenients = (TextView) findViewById(R.id.txt_expand_convenients);
        txtExpandDescription = (TextView) findViewById(R.id.txt_expand_description);
        txtCapacityCramped = (TextView) findViewById(R.id.txt_capacity_cramped);
        txtCapacityMedium = (TextView) findViewById(R.id.txt_capacity_medium);
        txtCapacitySpacious = (TextView) findViewById(R.id.txt_capacity_spacious);

        btnCallPhone = (Button) findViewById(R.id.btn_callPhone);
        btnDirectMap = (Button) findViewById(R.id.btn_directMap);
        btnPostComment = (Button) findViewById(R.id.btn_postComment);
        btnViewAll = (Button) findViewById(R.id.btn_viewAll);

        imgRoomGender = (ImageView) findViewById(R.id.img_roomGender);
        imgRoom1 = (ImageView) findViewById(R.id.img_room1);
        imgRoom2 = (ImageView) findViewById(R.id.img_room2);
        imgRoom3 = (ImageView) findViewById(R.id.img_room3);
        imgRoom4 = (ImageView) findViewById(R.id.img_room4);

        //imgFavorite = (ImageView) findViewById(R.id.img_favorite);

        txtMoreImg = findViewById(R.id.txt_more_img);
        frLoutContain = findViewById(R.id.fr_lout_contain);

        lnLtExpandConvenients = findViewById(R.id.lnLt_expand_convenients);
        lnLtExpandDescription = findViewById(R.id.lnLt_expand_description);

        listImageRoom = new ArrayList<ImageView>();

        listImageRoom.add(imgRoom1);
        listImageRoom.add(imgRoom2);
        listImageRoom.add(imgRoom3);
        listImageRoom.add(imgRoom4);

        recyclerCommentRoomDetail = (RecyclerView) findViewById(R.id.recycler_comment_room_detail);
        recyclerConvenientsRoomDetail = (RecyclerView) findViewById(R.id.recycler_convenients_room_detail);
        recyclerPriceRoomDetail = (RecyclerView) findViewById(R.id.recycler_price_room_detail);
        recyclerSameDetailRoom = (RecyclerView) findViewById(R.id.recycler_same_detail_room);

        progressBarSameDetailRoom = (ProgressBar) findViewById(R.id.progress_bar_same_detail_room);
        progressBarSameDetailRoom.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        lnLtQuantityTopSameDetailRoom = (LinearLayout) findViewById(R.id.lnLt_quantity_top_same_detail_room);
        txtQuantitySameDetailRoom = (TextView) findViewById(R.id.txt_quantity_same_detail_room);

        nestedScrollSameDetailRoomView = (NestedScrollView) findViewById(R.id.nested_scroll_same_detail_room_view);
        progressBarLoadMoreSameDetailRoom = (ProgressBar) findViewById(R.id.progress_bar_load_more_same_detail_rooms);
        progressBarLoadMoreSameDetailRoom.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    // Khởi tạo các giá trị cho các control.
    private void initData() {
        // Thiết lập toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Chi tiết phòng");
        }

        //Gán các giá trị vào giao diện
        txtRoomType.setText(roomModel.getRoomType());
        txtRoomMaxNumber.setText(String.valueOf((int) roomModel.getMaxNumber()));
        txtQuantityComment.setText("0");
        txtQuantityComment_2.setText("0");
        txtRoomName.setText(roomModel.getName());

        int num = roomModel.getRentalCosts();
        int count = 0;
        int temp = num;
        int digit;
        int so1 = 0, so2 = 0, so3 = 0, so4 = 0;
        while(num > 0)
        {
            num = num / 10;
            count++;
        }
        while(temp > 0)
        {
            digit = temp % 10;
            if (count == 3) {
                so4 = digit;
            }
            if (count == 3) {
                so3 = digit;
            }
            if (count == 2) {
                 so2 = digit;
            }
            if (count == 1) {
                 so1 = digit;
            }
            temp = temp / 10;
            count--;
        }


        if (roomModel.getRentalCosts() < 100000) {
            txtRoomPrice.setText(so1+""+so2+"k/ phòng");
        } else if (100000 <= roomModel.getRentalCosts() && roomModel.getRentalCosts() < 1000000) {
            txtRoomPrice.setText(so1+""+so2+""+so3+"k/ phòng");
        } else if (1000000 <= roomModel.getRentalCosts()) {
            txtRoomPrice.setText(so1 + "."+ so2 +"tr/ phòng");
        }

        txtRoomPhoneNumber.setText(roomModel.getRoomOwner().getPhoneNumber());

        if (((int) roomModel.getCurrentNumber()) < ((int) roomModel.getMaxNumber())) {
            txtRoomStatus.setText("Còn");
        } else {
            txtRoomStatus.setText("Hết");
        }
        int dientich = (int) Math.round(roomModel.getLength() * roomModel.getWidth());
        txtRoomArea.setText(dientich+ "m²");
        txtRoomDescription.setText(roomModel.getDescribe());
        txtRoomGreatReview.setText(roomModel.getGreat() + "");
        txtRoomPrettyGoodReview.setText(roomModel.getPrettyGood() + "");
        txtRoomMediumReview.setText(roomModel.getMedium() + "");
        txtRoomBadReview.setText(roomModel.getBad() + "");
        tenchu.setText(roomModel.getRoomOwner().getName());
        Picasso.get().load(roomModel.getRoomOwner().getAvatar()).into(img_avt_comment_room_detail);

        // expand/ collapse txtRoomDescription
        expandRoomDescription();

        // load số lượng người vào 3 hình tròn
        setRoomCapacity();

        //Set address for room
        String longAddress = roomModel.getApartmentNumber() + " " + roomModel.getStreet() + ", "
                + roomModel.getWard() + ", " + roomModel.getCounty() + ", " + roomModel.getCity();
        txtRoomAddress.setText(longAddress);
        //End set address for room


        //Gán hình cho giới tính
        if (roomModel.isGender()) {
            imgRoomGender.setImageResource(R.drawable.ic_svg_male_100);
        } else {
            imgRoomGender.setImageResource(R.drawable.ic_svg_female_100);
        }
        //End Gán giá trị cho giới tính


        //Gán giá trị cho số lượt bình luận
        if (roomModel.getListCommentRoom().size() > 0) {
            txtQuantityComment.setText(roomModel.getListCommentRoom().size() + "");
            txtQuantityComment_2.setText(roomModel.getListCommentRoom().size() + "");
        }
        //End gán giá trị cho số lượng bình luận


        //Download hình ảnh cho room
        for (int i = 0; i < 4; i++) {
            downloadImageForImageControl(listImageRoom.get(i), i);
        }
        // End download hình ảnh cho room


        // Load số lượng bình luận thuộc từng loại điểm
        long great, prettyGood, medium, bad;
        great = prettyGood = medium = bad = 0;
        for (CommentModel commentModel : roomModel.getListCommentRoom()) {
            long stars = commentModel.getStars();

            if (stars < 4) {
                bad += 1;
            } else if (stars < 7) {
                medium += 1;
            } else if (stars < 9) {
                prettyGood += 1;
            } else {
                great += 1;
            }
        }

        txtRoomBadReview.setText(bad + "");
        txtRoomMediumReview.setText(medium + "");
        txtRoomPrettyGoodReview.setText(prettyGood + "");
        txtRoomGreatReview.setText(great + "");
        // End load số lượng bình luận thuộc từng loại điểm


        // Load danh sách bình luận của phòng trọ
        commentController.sortListComments(roomModel.getListCommentRoom());
        downloadImageFitForComment();

        RecyclerView.LayoutManager layoutManagerComment = new LinearLayoutManager(this);
        recyclerCommentRoomDetail.setLayoutManager(layoutManagerComment);
        adapterRecyclerComment = new AdapterRecyclerComment(this, R.layout.comment_element_grid_room_detail_view,
                roomModel.getListCommentRoom(), roomModel.getRoomID(), UID, false);
        recyclerCommentRoomDetail.setAdapter(adapterRecyclerComment);
        adapterRecyclerComment.notifyDataSetChanged();


        // Load danh sách tiện nghi của phòng trọ
        expandRoomConvenients();

        RecyclerView.LayoutManager layoutManagerConvenient = new GridLayoutManager(this, 3);
        recyclerConvenientsRoomDetail.setLayoutManager(layoutManagerConvenient);
        adapterRecyclerConvenient = new AdapterRecyclerConvenient(this, getApplicationContext(),
                R.layout.utility_element_grid_room_detail_view, roomModel.getListConvenientRoom());
        recyclerConvenientsRoomDetail.setAdapter(adapterRecyclerConvenient);
        recyclerConvenientsRoomDetail.setNestedScrollingEnabled(false);
        adapterRecyclerConvenient.notifyDataSetChanged();


        // Load danh sách giá của phòng trọ
        RecyclerView.LayoutManager layoutManagerRoomPrice = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerPriceRoomDetail.setLayoutManager(layoutManagerRoomPrice);
        adapterRecyclerRoomPrice = new AdapterRecyclerRoomPrice(this, getApplicationContext(),
                R.layout.room_price_element_grid_room_detail_view, roomModel.getListRoomPrice());
        recyclerPriceRoomDetail.setAdapter(adapterRecyclerRoomPrice);
        adapterRecyclerRoomPrice.notifyDataSetChanged();
    }

    private void setRoomCapacity() {

        txtCapacitySpacious.setText( roomModel.getMaxNumber() - 1 + " người -");
        txtCapacityMedium.setText( roomModel.getMaxNumber() + " người");
        txtCapacityCramped.setText( roomModel.getMaxNumber() + 1 + " người +");
    }

    private void expandRoomDescription() {
        txtRoomDescription.post(new Runnable() {
            @Override
            public void run() {
                if (txtRoomDescription.getLineCount() <= 2) {
                    lnLtExpandDescription.setVisibility(View.GONE);
                } else {
                    txtExpandDescription.setText(R.string.stringExpand);
                    collapseTextView(txtRoomDescription, 2);
                }
            }
        });

        lnLtExpandDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtExpandDescription.getText().toString().equals(getString(R.string.stringCollapse))) {
                    collapseTextView(txtRoomDescription, 2);
                    txtExpandDescription.setText(R.string.stringExpand);
                } else {
                    expandTextView(txtRoomDescription);
                    txtExpandDescription.setText(R.string.stringCollapse);
                }
            }
        });
    }

    private void expandTextView(TextView tv) {
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines", tv.getLineCount());
        animation.setDuration(200).start();
    }

    private void collapseTextView(TextView tv, int numLines) {
        ObjectAnimator animation = ObjectAnimator.ofInt(tv, "maxLines", numLines);
        animation.setDuration(200).start();
    }

    private void expandRoomConvenients() {
        int convenientRoomSize = roomModel.getListConvenientRoom().size();
        int rowConvenientHeight = 203;
        int fullRowConvenientHeight = (convenientRoomSize % 3 == 0) ?
                (convenientRoomSize / 3) * rowConvenientHeight : (convenientRoomSize / 3) * rowConvenientHeight + rowConvenientHeight;

        txtExpandConvenients.setText(R.string.stringExpand);

        if (convenientRoomSize > 3) {
            resizeRecyclerConvenientsRoomDetailAnimation(recyclerConvenientsRoomDetail.getHeight(), rowConvenientHeight);
        } else {
            lnLtExpandConvenients.setVisibility(View.GONE);
        }

        lnLtExpandConvenients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtExpandConvenients.getText().toString().equals(getString(R.string.stringCollapse))) {
                    resizeRecyclerConvenientsRoomDetailAnimation(recyclerConvenientsRoomDetail.getHeight(), rowConvenientHeight);
                    txtExpandConvenients.setText(R.string.stringExpand);
                } else {
                    resizeRecyclerConvenientsRoomDetailAnimation(recyclerConvenientsRoomDetail.getHeight(), fullRowConvenientHeight);
                    txtExpandConvenients.setText(R.string.stringCollapse);
                }
            }
        });
    }

    private void resizeRecyclerConvenientsRoomDetailAnimation(int fromHeight, int toHeight) {
        ValueAnimator anim = ValueAnimator.ofInt(fromHeight, toHeight);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = recyclerConvenientsRoomDetail.getLayoutParams();
                layoutParams.height = val;

                recyclerConvenientsRoomDetail.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(200);
        anim.start();
    }

    // Hàm tải ảnh từ firebase về theo image control và vị trí ảnh cần lấy trên firebase.
    private void downloadImageForImageControl(final ImageView imageDownload, final int positionDownload) {
        Picasso.get().load(roomModel.getListImageRoom().get(positionDownload)).fit().centerCrop().into(imageDownload);
    }

    private void downloadImageFitForComment() {
        for (CommentModel commentModel : roomModel.getListCommentRoom()) {
            // Load ảnh nén
            commentModel.setCompressionImageFit(Picasso.get().load(commentModel.getUserComment().getAvatar()).fit());
        }
    }

    // Hàm gọi điện thoại cho chủ phòng trọ.
    private void clickCallPhone() {
        btnCallPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strPhoneNumbet = roomModel.getRoomOwner().getPhoneNumber();
                Intent intentCall = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", strPhoneNumbet, null));
                startActivity(intentCall);
            }
        });
    }

    // Hàm hiển thị bản đồ chỉ đường.
    private void clickDirectMap() {
        btnDirectMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringRoomName = roomModel.getName();

                Intent intentDirectMap = new Intent(detailRoom.this, directMapRoomDetail.class);
                intentDirectMap.putExtra("phongtro", roomModel);
                startActivity(intentDirectMap);
            }
        });
    }

    // Hàm viết hiển thị màn hình viết bình luận.
    private void clickPostComment() {
        btnPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCommentAndRate = new Intent(detailRoom.this, commentAndRateMain.class);
                intentCommentAndRate.putExtra("phongtro", roomModel);
                intentCommentAndRate.putExtra("currentPage", 0);
                startActivity(intentCommentAndRate);
            }
        });

        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCommentAndRate = new Intent(detailRoom.this, commentAndRateMain.class);
                intentCommentAndRate.putExtra("phongtro", roomModel);
                intentCommentAndRate.putExtra("currentPage", 1);
                startActivity(intentCommentAndRate);
            }
        });
    }

    // Hàm hiển thị ảnh phòng trọ chế độ xem.
    private void clickShowImage() {
        imgRoom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialogCustom();
                indexImage = 0;
            }
        });

        imgRoom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialogCustom();
                indexImage = 1;
            }
        });

        imgRoom3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialogCustom();
                indexImage = 2;
            }
        });

        imgRoom4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageDialogCustom();
                indexImage = 3;
            }
        });
    }

    // Hàm hiển thị vị trí ảnh trên tống số.
    private void showPostionImage(int position) {
        txtPositionImage.setText(String.valueOf(position) + "/" + String.valueOf(maxImageInRoom));
    }

    //Hàm hiển thị cộng thêm hình ở thẻ cuối
    private void setMoreImageForLastCard() {
        int size = roomModel.getListImageRoom().size();
        if (size > 4) {
            txtMoreImg.setText("+" + (size - 4));
            frLoutContain.setBackgroundResource(R.color.colorTransParrent);
        } else {
            txtMoreImg.setText("");
            frLoutContain.setBackgroundResource(R.color.colorWhile100);
        }
    }

    // Hàm tạo ra custom dialong và các tác vụ liên quan.
    private void showImageDialogCustom() {
        maxImageInRoom = roomModel.getListImageRoom().size();

        dialogShowImage = new Dialog(detailRoom.this);
        dialogShowImage.setContentView(R.layout.dialog_show_image_detail_room);

        // Các control chế độ xem ảnh phòng trọ.
        viewPagerShowImage = (ViewPager) dialogShowImage.findViewById(R.id.viewPager_showImage_detail_room);
        btnCloseShowImage = (Button) dialogShowImage.findViewById(R.id.btn_closeShowImage_detail_room);
        txtPositionImage = (TextView) dialogShowImage.findViewById(R.id.txt_positionImage_detail_room);

        // Truyền dữ liệu qua view pager show image.
        AdapterViewPagerImageShow adapterViewPagerImageShow = new AdapterViewPagerImageShow(this,
                roomModel.getListImageRoom());
        viewPagerShowImage.setAdapter(adapterViewPagerImageShow);

        // Hiển thị lúc ban đầu.
        showPostionImage(1);

        viewPagerShowImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                int index = viewPagerShowImage.getCurrentItem();
                showPostionImage(index + 1);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        // Kết thúc chế độ xem ảnh phòng trọ.
        btnCloseShowImage.setEnabled(true);
        btnCloseShowImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogShowImage.cancel();
            }
        });


        // Tùy chính lại dialog gồm giao diện, match parent width, height và chuyển background trong suốt.
        dialogShowImage.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogShowImage.getWindow().setDimAmount(0.9f);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialogShowImage.getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);

        dialogShowImage.show();
    }

    private void clickAddToFavorite() {
        String roomId = roomModel.getRoomID();

        if (menuItemFavorite.getIcon().getConstantState().equals(
                getResources().getDrawable(R.drawable.ic_favorite_border_white).getConstantState()
        )) {
            mainActivityController.addToFavoriteRooms(roomId, detailRoom.this, menuItemFavorite);
        } else {
            mainActivityController.removeFromFavoriteRooms(roomId, detailRoom.this, menuItemFavorite);
        }
    }

    // Hàm load các phòng trọ cùng tiêu chí
    private void loadTheSameRoom() {
        // Load phòng cùng tiêu chí
        List<myFilter> myFilters = new ArrayList<myFilter>();
        //Thêm vào tiêu chí số người
        GenderFilter genderFilter = new GenderFilter((int)roomModel.getMaxNumber(),roomModel.isGender());
        myFilters.add(genderFilter);
        //Thêm vào tiêu chí loại phòng trọ
        TypeFilter typeFilter = new TypeFilter("",roomModel.getTypeID());
        myFilters.add(typeFilter);
        //Thêm vào tiêu chí tiện ích của phòng

        DetailRoomController detailRoomController = new DetailRoomController(this, District, myFilters, UID);
        detailRoomController.loadSearchRoom(recyclerSameDetailRoom, CurrentRoomID, progressBarSameDetailRoom,
                txtQuantitySameDetailRoom, lnLtQuantityTopSameDetailRoom,
                nestedScrollSameDetailRoomView, progressBarLoadMoreSameDetailRoom);

        if (!isAdmin) {
            //Gọi hàm thêm vào lượng view từ controller
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String date = df.format(Calendar.getInstance().getTime());
            RoomViewsModel data = new RoomViewsModel(date, UID, roomModel.getRoomID());
            detailRoomController.addViews(data);
        }
    }

    private void setViewSameRoom() {
        // Hiện progress bar.
        progressBarSameDetailRoom.setVisibility(View.VISIBLE);
        // Ẩn progress bar load more.
        progressBarLoadMoreSameDetailRoom.setVisibility(View.GONE);
        // Ẩn layout kết quả trả vể.
        lnLtQuantityTopSameDetailRoom.setVisibility(View.GONE);
    }
}
