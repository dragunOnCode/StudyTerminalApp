package com.example.studyterminalapp.activity.student;

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


import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.adapter.student.StudentHomeAdapter;
import com.example.studyterminalapp.bean.HomeClassBean;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.SaTokenInfo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.example.studyterminalapp.views.ClassListView;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
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
    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        // uid = getIntent().getIntExtra("id", 6);
        uid = MyApp.getId();

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
                intent.putExtra("id", uid);
                StudentHomeActivity.this.startActivity(intent);
            }
        });
    }

    private void initData() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("pageSize", 1000);
        paramsMap.put("pageNum", 1);
        paramsMap.put("uid", uid);
        try {
            RequestManager.getInstance().GetRequest(paramsMap, Constants.USER_CLASS_LIST, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String code, String json) {

                    //Log.d("TEST", "JSON: " + json);
                    Type dataType = new TypeToken<Result<List<HomeClassBean>>>(){}.getType();
                    Result<List<HomeClassBean>> result = JsonParse.getInstance().getResult(json, dataType);
                    List<HomeClassBean> data = result.getData();
                    if (data == null || data.isEmpty()) {
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            studentHomeAdapter.setData(data);
                        }
                    });
                }

                @Override
                public void onError(String msg) {
                    Log.i("Home Page Error", msg);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
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
    }

}