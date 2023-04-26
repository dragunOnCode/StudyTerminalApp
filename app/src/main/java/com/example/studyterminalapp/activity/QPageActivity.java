package com.example.studyterminalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.teacher.TeacherHomeActivity;
import com.example.studyterminalapp.activity.teacher.TeacherHomeworkActivity;
import com.example.studyterminalapp.bean.Textbook;
import com.example.studyterminalapp.utils.ActivityCollector;

public class QPageActivity extends AppCompatActivity {
    private LinearLayout ll2, ll3;
    private TextView tv1, tv2, tv3;
    private Button btnPrev, btnNext, btnPrint, btnQuit;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qpage);

        ActivityCollector.addActivity(this);

        id = getIntent().getIntExtra("id", 1);

        initView();
    }

    private void initView() {
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        ll2 = (LinearLayout) findViewById(R.id.ll2);
        ll3 = (LinearLayout) findViewById(R.id.ll3);
        btnPrev = (Button) findViewById(R.id.btn_prev);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnPrint = (Button) findViewById(R.id.btn_print);
        btnQuit = (Button) findViewById(R.id.btn_quit);

        tv1.setText("第 " + id + " 题");
        if (id % 2 == 0) {
            tv2.setVisibility(View.GONE);
        } else {
            tv3.setVisibility(View.GONE);
            for (int i = 0; i < 2; i++) {
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                TextView tv = new TextView(this);
                tv.setLayoutParams(lp);
                tv.setText(". xxx");
                tv.setTextSize(16);
                tv.setVisibility(View.VISIBLE);
                ll2.addView(tv);
                System.out.println("添加了,是否可见" + tv.getVisibility());
            }
        }

        if (id == 1) {
            btnPrev.setVisibility(View.GONE);
        } else {
            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(QPageActivity.this, QPageActivity.class);
                    intent.putExtra("id", id - 1);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, 0);
                }
            });
        }

        if (id == 10) {
            btnNext.setVisibility(View.GONE);
        } else {
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(QPageActivity.this, QPageActivity.class);
                    intent.putExtra("id", id + 1);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_out_right, 0);
                }
            });
        }

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.printActivity();
            }
        });

        btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QPageActivity.this, PageActivity.class);
                startActivity(intent);
                ActivityCollector.finishOneActivity("com.example.studyterminalapp.activity.QPageActivity");
            }
        });
    }


    @Override
    public void onBackPressed() {
        return;
        // super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //从活动管理器删除当前Activity
        ActivityCollector.removeActivity(this);
    }


}