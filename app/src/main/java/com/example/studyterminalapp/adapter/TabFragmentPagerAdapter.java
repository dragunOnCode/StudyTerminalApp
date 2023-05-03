package com.example.studyterminalapp.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.studyterminalapp.PageFragment;

import java.util.ArrayList;
import java.util.List;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
    private FragmentManager mfragmentManager;
    private List<PageFragment> mlist;
    private ArrayList<String> mtitlelist;

    //这是一段构造器，我没写的时候，第一次代码是报错的，在我做了下面这个构造器之后，没有报错！！！
    public TabFragmentPagerAdapter(FragmentManager fm, List<PageFragment> list, ArrayList<String> mtitlelist) {
        super(fm);
        this.mlist = list;
        this.mtitlelist = mtitlelist;
    }
    //显示第几个页面
    @Override
    public Fragment getItem(int position) {
        return mlist.get(position);
    }
    //一共有几个页面，注意，使用Fragment特有的构造器时，和ViewPager的原生构造器的方法不同
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mtitlelist.get(position);
    }
}
