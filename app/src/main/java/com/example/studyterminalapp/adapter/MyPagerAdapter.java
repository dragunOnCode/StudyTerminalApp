package com.example.studyterminalapp.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class MyPagerAdapter extends PagerAdapter {
    private ArrayList<View> mViewlist;
    private ArrayList<String> mtitlelist;

    public MyPagerAdapter() {
    }

    public MyPagerAdapter(ArrayList<View> viewlist, ArrayList<String> mtitlelist) {
        mViewlist = viewlist;
        this.mtitlelist = mtitlelist;
    }

    @Override
    public int getCount() {
        return mViewlist.size();//返回view数组大小
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(mViewlist.get(position));
        return mViewlist.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(mViewlist.get(position));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mtitlelist.get(position);
    }
}

