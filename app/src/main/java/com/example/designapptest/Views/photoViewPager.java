package com.example.designapptest.Views;


import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.designapptest.R;

import java.util.List;

public class photoViewPager extends PagerAdapter {
    private List<Photo> photos;

    public photoViewPager(List<Photo> photos) {
        this.photos = photos;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.itemphoto, container,false);

        ImageView img = view.findViewById(R.id.imgphoto);
        Photo photo = photos.get(position);
        img.setImageResource(photo.getImg());

        container.addView(view);


        return view;
    }

    @Override
    public int getCount() {
        if (photos != null) {
            return photos.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
