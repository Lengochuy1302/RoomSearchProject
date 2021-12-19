package com.example.designapptest.Views;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.designapptest.Controller.UserController;
import com.example.designapptest.R;

public class adminHostsView extends AppCompatActivity {
    RecyclerView recyclerAdminHostsView;
    UserController userController;

    SharedPreferences sharedPreferences;
    String UID;

    Toolbar toolbar;

    ProgressBar progressBarAdminHosts;
    LinearLayout lnLtQuantityTopAdminHosts;

    // Số lượng trả về.
    TextView txtQuantity;
    EditText edT_search;
    NestedScrollView nestedScrollAdminHostsView;
    ProgressBar progressBarLoadMoreAdminHosts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verified_rooms_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        LinearLayout darkd = findViewById(R.id.darkd);
        darkd.setVisibility(View.GONE);
        sharedPreferences = getSharedPreferences(LoginView.PREFS_DATA_NAME, MODE_PRIVATE);
        UID = sharedPreferences.getString(LoginView.SHARE_UID, "n1oc76JrhkMB9bxKxwXrxJld3qH2");

        initControl();

        edT_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString()!=null) {
                    userController.ListHosts(recyclerAdminHostsView, txtQuantity, progressBarAdminHosts,
                            lnLtQuantityTopAdminHosts, nestedScrollAdminHostsView, progressBarLoadMoreAdminHosts, s.toString());
                } else {
                    userController.ListHosts(recyclerAdminHostsView, txtQuantity, progressBarAdminHosts,
                            lnLtQuantityTopAdminHosts, nestedScrollAdminHostsView, progressBarLoadMoreAdminHosts, "");
                }

            }
        });
    }

    private void initControl() {
        recyclerAdminHostsView = (RecyclerView) findViewById(R.id.recycler_verified_rooms);

        toolbar = findViewById(R.id.toolbar);
        edT_search = findViewById(R.id.edT_search);
        progressBarAdminHosts = (ProgressBar) findViewById(R.id.progress_bar_verified_rooms);
        progressBarAdminHosts.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        lnLtQuantityTopAdminHosts = (LinearLayout) findViewById(R.id.lnLt_quantity_top_verified_rooms);
        txtQuantity = (TextView) findViewById(R.id.txt_quantity_verified_rooms);

        nestedScrollAdminHostsView = (NestedScrollView) findViewById(R.id.nested_scroll_verified_rooms_view);
        progressBarLoadMoreAdminHosts = (ProgressBar) findViewById(R.id.progress_bar_load_more_verified_rooms);
        progressBarLoadMoreAdminHosts.getIndeterminateDrawable().setColorFilter(Color.parseColor("#00DDFF"),
                android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void setView() {
        // Hiện progress bar.
        progressBarAdminHosts.setVisibility(View.VISIBLE);
        // Ẩn progress bar load more.
        progressBarLoadMoreAdminHosts.setVisibility(View.GONE);
        // Ẩn layout kết quả trả vể.
        lnLtQuantityTopAdminHosts.setVisibility(View.GONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        initData();

        setView();

        userController = new UserController(this);
        userController.ListHosts(recyclerAdminHostsView, txtQuantity, progressBarAdminHosts,
                lnLtQuantityTopAdminHosts, nestedScrollAdminHostsView, progressBarLoadMoreAdminHosts, "");
    }

    private void initData() {
        // Thiết lập toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Tất cả chủ phòng");
        }
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