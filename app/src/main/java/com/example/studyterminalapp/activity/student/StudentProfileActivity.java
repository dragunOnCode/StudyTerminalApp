package com.example.studyterminalapp.activity.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.LoginActivity;
import com.example.studyterminalapp.activity.teacher.TeacherHomeActivity;
import com.example.studyterminalapp.bean.HomeClassBean;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.SaTokenInfo;
import com.example.studyterminalapp.bean.StudentBean;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.example.studyterminalapp.utils.UserManage;
import com.google.gson.reflect.TypeToken;

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

public class StudentProfileActivity extends AppCompatActivity {

    private StudentBean student;
    private RelativeLayout rlTitleBar;
    private ImageView ivBack;
    private Button btnHomePage, btnEditProfile, btnMyOrder, btnEditAddress, btnLogout;
    private TextView tvTitle, tvStudentName, tvUsername, tvEmail, tvStudentNumber, tvSchool;
    private ImageView ivProfilePic;
    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        // uid = getIntent().getIntExtra("id", 6);
        uid = MyApp.getId();

        initView();
        initData();
    }

    private void initView() {
        student = null;
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));

        ivBack = findViewById(R.id.iv_back);
        ivBack.setVisibility(View.INVISIBLE);

        tvTitle = findViewById(R.id.tv_title);
        tvStudentName = findViewById(R.id.tv_student_name);
        tvUsername = findViewById(R.id.tv_username);
        tvEmail = findViewById(R.id.tv_email);
        tvStudentNumber = findViewById(R.id.tv_student_number);
        tvSchool = findViewById(R.id.tv_school);
        ivProfilePic = findViewById(R.id.iv_profile_pic);

        btnHomePage = findViewById(R.id.btn_home_page);
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        btnMyOrder = findViewById(R.id.btn_my_order);
        btnEditAddress = findViewById(R.id.btn_edit_address);
        btnLogout = (Button) findViewById(R.id.btn_logout);

        tvTitle.setText("我的");

        btnHomePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Profile", this.getClass().toString() + "跳转到HomePage");
                Intent intent = new Intent(StudentProfileActivity.this, StudentHomeActivity.class);
                intent.putExtra("student", 1);
                StudentProfileActivity.this.startActivity(intent);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Profile", this.getClass().toString() + "跳转到EditProfile");
                Intent intent = new Intent(StudentProfileActivity.this, EditStudentProfileActivity.class);
                intent.putExtra("student", student);
                intent.putExtra("id", uid);
                StudentProfileActivity.this.startActivity(intent);
            }
        });

        btnMyOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Profile", this.getClass().toString() + "MyOrder");
                Intent intent = new Intent(StudentProfileActivity.this, StudentOrderActivity.class);
                // 可以用putExtra传递用户id
                // intent.putExtra("student", student);
                StudentProfileActivity.this.startActivity(intent);
            }
        });

        btnEditAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentProfileActivity.this, StudentContactActivity.class);
                //intent.putExtra("student", student);
                StudentProfileActivity.this.startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManage.getInstance().clearUserInfo(StudentProfileActivity.this);
                Intent intent = new Intent(StudentProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("uid", uid);
        try {
            RequestManager.getInstance().GetRequest(paramsMap, Constants.USER_PROFILE, new RequestManager.ResultCallback() {
                        @Override
                        public void onResponse(String code, String json) {
                            //Log.d("TEST", "JSON: " + json);
                            Type dataType = new TypeToken<Result<StudentBean>>(){}.getType();
                            Result<StudentBean> result = JsonParse.getInstance().getResult(json, dataType);
                            student = result.getData();
                            Log.i("Profile", student.toString());
                            if (student != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvStudentName.setText(student.getNickname());
                                        tvUsername.setText(student.getUsername());
                                        tvEmail.setText(student.getEmail());
                                        tvStudentNumber.setText(student.getStudentNumber());
                                        tvSchool.setText(student.getSchool());
                                        Glide.with(StudentProfileActivity.this).load(student.getProfileImg())
                                                .error(R.mipmap.ic_launcher)
                                                .into(ivProfilePic);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            Log.e("Student Profile Error", msg);
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}