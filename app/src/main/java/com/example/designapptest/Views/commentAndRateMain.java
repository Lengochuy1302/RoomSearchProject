package com.example.designapptest.Views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.designapptest.Adapters.AdapterViewPagerCommentAndRate;
import com.example.designapptest.Model.RoomModel;
import com.example.designapptest.R;

public class commentAndRateMain extends AppCompatActivity {
    ViewPager viewPagerCommentAndRate;
    TabLayout tabLayoutCommentAndRate;

    RoomModel roomModel;
    int currentPagePosition;
    SharedPreferences sharedPreferences;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_and_rate_main_room_detail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        roomModel = getIntent().getParcelableExtra("phongtro");
        currentPagePosition = getIntent().getIntExtra("currentPage", 0);
        sharedPreferences = getSharedPreferences("currentUserId", MODE_PRIVATE);

        loadControl();
    }

    public RoomModel getRoomModelInfo() {
        return roomModel;
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    private void loadControl() {
        viewPagerCommentAndRate = (ViewPager) findViewById(R.id.viewpager_commentAndRate_room_detail);
        tabLayoutCommentAndRate = (TabLayout) findViewById(R.id.tablayout_commentAndRate_room_detail);
        toolbar = findViewById(R.id.toolbar);

        // Thiết lập toolbar
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("Bình luận phòng trọ");
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        final AdapterViewPagerCommentAndRate adapterViewPagerCommentAndRate = new AdapterViewPagerCommentAndRate(fragmentManager);

        viewPagerCommentAndRate.setAdapter(adapterViewPagerCommentAndRate);
        // Linh thêm
        viewPagerCommentAndRate.setCurrentItem(currentPagePosition);

        tabLayoutCommentAndRate.setupWithViewPager(viewPagerCommentAndRate);

        viewPagerCommentAndRate.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayoutCommentAndRate));
        tabLayoutCommentAndRate.setTabsFromPagerAdapter(adapterViewPagerCommentAndRate);
        tabLayoutCommentAndRate.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPagerCommentAndRate));

        viewPagerCommentAndRate.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 1) {
                    commentAndRateStep2 fragment = (commentAndRateStep2) adapterViewPagerCommentAndRate.getItem(i);
                    fragment.setView();
                    fragment.setAdapter();
                } else if (i == 2) {
                    commentAndRateStep3 fragment = (commentAndRateStep3) adapterViewPagerCommentAndRate.getItem(i);
                    fragment.setView();
                    fragment.setAdapter();
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
