package com.example.studyterminalapp.activity.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.adapter.student.StudentOrderAdapter;
import com.example.studyterminalapp.bean.OrderBean;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class StudentOrderActivity extends AppCompatActivity {

    private ListView lvOrder;
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;
    private StudentOrderAdapter studentOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_order);

        initView();
        initAdapter();
        initData();
    }

    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        lvOrder = findViewById(R.id.lv_order);
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);

        tvTitle.setText("我的订单");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initAdapter() {
        studentOrderAdapter = new StudentOrderAdapter(this);
        lvOrder.setAdapter(studentOrderAdapter);
    }

    private void initData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(Constants.WEB_SITE + Constants.REQUEST_STUDENT_ORDER_DATA).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json = response.body().string();
                Log.d("TEST", "JSON: " + json);
                final List<OrderBean> orderList = JsonParse.getInstance().getOrderList(json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        studentOrderAdapter.setData(orderList);
                    }
                });
            }
        });
    }
}