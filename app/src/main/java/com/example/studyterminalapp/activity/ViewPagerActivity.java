package com.example.studyterminalapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.studyterminalapp.PageFragment;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.adapter.TabFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity{

    private ViewPager myViewPager;
    private List<PageFragment> list;
    private TabFragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        initView();

        // 设置菜单栏的点击事件
        myViewPager.setOnPageChangeListener(new MyPagerChangeListener());

        //把Fragment添加到List集合里面
        list = new ArrayList<PageFragment>();
        list.add(new PageFragment());
        list.add(new PageFragment());
        list.add(new PageFragment());
        ArrayList<String> titles = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            titles.add("第" + i + "题");
        }
        adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list, titles);
        myViewPager.setAdapter(adapter);
        myViewPager.setCurrentItem(0);  //初始化显示第一个页面
    }

    private void initView() {
        myViewPager = (ViewPager) findViewById(R.id.viewpage);
    }
//    //第一次设置点击监听事件，为菜单栏设置监听事件，监听的对象是页面的滑动
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tv_item_one:
//                myViewPager.setCurrentItem(0);
//                tv_item_one.setBackgroundColor(Color.RED);
//                tv_item_two.setBackgroundColor(Color.WHITE);
//                tv_item_three.setBackgroundColor(Color.WHITE);
//                break;
//            case R.id.tv_item_two:
//                myViewPager.setCurrentItem(1);
//                tv_item_one.setBackgroundColor(Color.WHITE);
//                tv_item_two.setBackgroundColor(Color.RED);
//                tv_item_three.setBackgroundColor(Color.WHITE);
//                break;
//            case R.id.tv_item_three:
//                myViewPager.setCurrentItem(2);
//                tv_item_one.setBackgroundColor(Color.WHITE);
//                tv_item_two.setBackgroundColor(Color.WHITE);
//                tv_item_three.setBackgroundColor(Color.RED);
//                break;
//        }
//    }
    //第二次设置点击监听事件，为ViewPager设置监听事件，用于实现菜单栏的样式变化
    private class MyPagerChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    Log.i("Change", "Page 0");
                    break;
                case 1:
                    Log.i("Change", "Page 1");
                    break;
                case 2:
                    Log.i("Change", "Page 2");
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}