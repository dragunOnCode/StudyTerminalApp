package com.example.studyterminalapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.studyterminalapp.R;
import com.example.studyterminalapp.adapter.StudentHomeAdapter;
import com.example.studyterminalapp.bean.HomeClassBean;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.views.ClassListView;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StudentHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rlTitleBar;
    private ImageView ivBack;
    private EditText etSearchContent;
    private MaterialSpinner msSearchType;
    private RoundButton btnSearchContent;
    private RelativeLayout rlBottomBar;
    private StudentHomeAdapter studentHomeAdapter;
    private ClassListView clvList;
    private Button btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        initView();
        initData();
    }

    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));

        ivBack = findViewById(R.id.iv_back);
        ivBack.setVisibility(View.INVISIBLE);

        msSearchType = findViewById(R.id.ms_search_type);
        etSearchContent = findViewById(R.id.et_search_content);
        btnSearchContent = findViewById(R.id.btn_search_content);
        btnProfile = findViewById(R.id.btn_profile);

        rlBottomBar = findViewById(R.id.bottom_bar);
        clvList = findViewById(R.id.clv_list);

        studentHomeAdapter = new StudentHomeAdapter(this);
        clvList.setAdapter(studentHomeAdapter);

        btnSearchContent.setOnClickListener(this);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.i("Home", this.getClass().toString() + "跳转到Profile");
                Intent intent = new Intent(StudentHomeActivity.this, StudentProfileActivity.class);
                intent.putExtra("student", 1);
                StudentHomeActivity.this.startActivity(intent);
            }
        });
    }

    private void initData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(Constants.WEB_SITE + Constants.REQUEST_HOME_CLASS_DATA).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json = response.body().string();
                Log.d("TEST", "JSON: " + json);
                final List<HomeClassBean> homeClassList = JsonParse.getInstance().getHomeList(json);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        studentHomeAdapter.setData(homeClassList);
                    }
                });
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search_content:
                String content = etSearchContent.getText().toString();
                String type = msSearchType.getText().toString();
                Toast.makeText(this, "搜索了 "+type+","+content, Toast.LENGTH_SHORT).show();
                break;
        }
//        new MaterialDialog.Builder(this)
//                .iconRes(R.drawable.iv_back)
//                .title("什么")
//                .content("你说什么")
//                .positiveText("好")
//                .show();
    }

}