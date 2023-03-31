package com.example.studyterminalapp.activity.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.LoginActivity;
import com.example.studyterminalapp.activity.teacher.AddStudentActivity;
import com.example.studyterminalapp.activity.teacher.TeacherClassDetailActivity;
import com.example.studyterminalapp.activity.teacher.TeacherHomeActivity;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.vo.ProfileAdminVo;
import com.example.studyterminalapp.bean.vo.ProfileTeacherVo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.example.studyterminalapp.utils.UserManage;
import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class AdminHomeActivity extends AppCompatActivity {
    private int aid;
    private TextView tvUsername, tvEmail;
    private LinearLayout llQuizManage, llTextbookManage;
    private RelativeLayout rlTitleBar;
    private ImageView ivBack;
    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        //aid = getIntent().getIntExtra("id", 1);
        aid = MyApp.getId();
        initView();
        initListener();
        initData();
    }

    private void initView() {
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvUsername = (TextView) findViewById(R.id.tv_username);

        llQuizManage = (LinearLayout) findViewById(R.id.ll_quiz_manage);
        llTextbookManage = (LinearLayout) findViewById(R.id.ll_textbook_manage);

        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivBack.setVisibility(View.INVISIBLE);
        rlTitleBar = (RelativeLayout) findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));

        btnLogout = (Button) findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManage.getInstance().clearUserInfo(AdminHomeActivity.this);
                Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initListener() {
        llQuizManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminQuestionActivity.class);
                intent.putExtra("aid", aid);
                AdminHomeActivity.this.startActivity(intent);
            }
        });

        llTextbookManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminTextbookActivity.class);
                intent.putExtra("aid", aid);
                AdminHomeActivity.this.startActivity(intent);
            }
        });
    }

    private void initData() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("aid", aid);
        RequestManager request = RequestManager.getInstance();
        try {
            request.GetRequest(paramsMap, Constants.ADMIN_PROFILE, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String responseCode, String response) {
                    int code = Integer.parseInt(responseCode);
                    switch (code) {
                        case 200:
                            Log.i("Login Response", response);
                            Type dataType = new TypeToken<Result<ProfileAdminVo>>(){}.getType();
                            Result<ProfileAdminVo> result = JsonParse.getInstance().getResult(response, dataType);

                            ProfileAdminVo data = result.getData();
                            if (data != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvUsername.setText(data.getUsername());
                                        tvEmail.setText(data.getEmail());
                                    }
                                });
                            }
                            break;
                        default:
                            Log.e("Login Response Not 200", response);
                            break;
                    }
                }

                @Override
                public void onError(String msg) {

                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}