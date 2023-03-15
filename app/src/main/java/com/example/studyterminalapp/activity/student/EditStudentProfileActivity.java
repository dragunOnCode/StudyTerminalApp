package com.example.studyterminalapp.activity.student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.bean.StudentBean;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.RequestManager;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class EditStudentProfileActivity extends AppCompatActivity {

    private EditText etNickname, etEmail, etSchool, etStudentNumber;
    private TextView tvUsername;
    private Button btnCancelCommit, btnCommitProfile;
    private StudentBean student;
    private int uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_profile);

        student = (StudentBean) getIntent().getSerializableExtra("student");
        // uid = getIntent().getIntExtra("id", 6);
        uid = MyApp.getId();
        initView();
        initData();
    }

    private void initView() {
        etNickname = findViewById(R.id.et_edit_student_name);
        etStudentNumber = findViewById(R.id.et_edit_student_number);
        etEmail = findViewById(R.id.et_edit_email);
        etSchool = findViewById(R.id.et_edit_school);

        tvUsername = (TextView) findViewById(R.id.tv_username);

        btnCancelCommit = findViewById(R.id.btn_cancel_commit);
        btnCommitProfile = findViewById(R.id.btn_commit_profile);

        btnCommitProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("uid", uid);
                paramsMap.put("nickname", etNickname.getText().toString());
                paramsMap.put("email", etEmail.getText().toString());
                paramsMap.put("school", etSchool.getText().toString());
                paramsMap.put("studentNumber", etStudentNumber.getText().toString());
                try {
                    RequestManager.getInstance().PostRequest(paramsMap, Constants.UPDATE_STUDENT_PROFILE, new RequestManager.ResultCallback() {
                        @Override
                        public void onResponse(String responseCode, String response) {
                            int code = Integer.parseInt(responseCode);
                            switch (code) {
                                case 200:
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(EditStudentProfileActivity.this, "更新用户信息成功", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                default:
                                    break;
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            Log.i("Update Student Profile", msg);
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        btnCancelCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        if (student == null) {
            return;
        }
        tvUsername.setText(student.getUsername());
        etNickname.setText(student.getNickname());
        etStudentNumber.setText(student.getStudentNumber());
        etSchool.setText(student.getSchool());
        etEmail.setText(student.getEmail());
    }
}