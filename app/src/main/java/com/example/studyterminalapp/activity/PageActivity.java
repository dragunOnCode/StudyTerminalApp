package com.example.studyterminalapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.adapter.MyPagerAdapter;
import com.example.studyterminalapp.utils.ActivityCollector;
import com.example.studyterminalapp.utils.UserManage;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;

public class PageActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private View loginView, registerView;
    private ArrayList<View> mViews;  //存放视图的数组
    private ArrayList<String> mtitle;//存放标题的数组
    private MyPagerAdapter mAdapter;//适配器
    private Button btnStart, btnPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);

        initView();
    }


    private void initView() {
        btnStart = (Button) findViewById(R.id.btn_start);
        btnPrint = (Button) findViewById(R.id.btn_print);
        mViewPager = findViewById(R.id.viewpager);

        LayoutInflater inflater = getLayoutInflater();

        mViews = new ArrayList<>();
        mtitle = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            View view = inflater.inflate(R.layout.question_page, null);

            mViews.add(view);
            mtitle.add("第 " +i + " 题");
        }

        mAdapter = new MyPagerAdapter(mViews, mtitle);
        mViewPager.setAdapter(mAdapter);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PageActivity.this, QPageActivity.class);
                intent.putExtra("id", 1);
                startActivity(intent);
            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.printActivity();
            }
        });
    }


    @Override
    public void onBackPressed() {
        return;
        // super.onBackPressed();
    }
}