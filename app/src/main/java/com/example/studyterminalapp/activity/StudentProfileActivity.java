package com.example.studyterminalapp.activity;

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
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.bean.StudentBean;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StudentProfileActivity extends AppCompatActivity {

    private StudentBean student;
    private RelativeLayout rlTitleBar;
    private ImageView ivBack;
    private Button btnHomePage, btnEditProfile;
    private TextView tvTitle, tvStudentName, tvUsername, tvEmail, tvStudentNumber, tvSchool;
    private ImageView ivProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

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
                StudentProfileActivity.this.startActivity(intent);
            }
        });
    }

    private void initData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(Constants.WEB_SITE + Constants.REQUEST_STUDENT_DATA).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String json = response.body().string();
                //Log.d("TEST", "JSON: " + json);
                student = JsonParse.getInstance().getStudent(json);
                if (student != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvStudentName.setText(student.getStudentName());
                            tvUsername.setText(student.getUsername());
                            tvEmail.setText(student.getEmail());
                            tvStudentNumber.setText(student.getStudentNumber());
                            tvSchool.setText(student.getSchool());
                            Glide.with(StudentProfileActivity.this).load(student.getProfilePicUrl())
                                    .error(R.mipmap.ic_launcher)
                                    .into(ivProfilePic);
                        }
                    });
                }
            }
        });
    }
}