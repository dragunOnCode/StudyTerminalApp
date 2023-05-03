package com.example.studyterminalapp.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> myFragmentList;
    private ArrayList<String> mTitleList;
    public MyFragmentAdapter(@NonNull FragmentManager fm, List<Fragment> myFragmentList,
                             ArrayList<String> mTitleList) {
        super(fm);
        this.myFragmentList = myFragmentList;
        this.mTitleList = mTitleList;
    }

    /**
     * 获取页面
     * @param position  页面的位置
     * @return 返回具体页面
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return myFragmentList == null ? null:myFragmentList.get(position);
    }

    /**
     * 获取adapter内存储的页面个数
     * @return
     */
    @Override
    public int getCount() {
        return myFragmentList == null ? 0 : myFragmentList.size();
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }
}
