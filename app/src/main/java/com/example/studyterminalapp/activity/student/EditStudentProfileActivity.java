package com.example.studyterminalapp.activity.student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.bean.StudentBean;

public class EditStudentProfileActivity extends AppCompatActivity {

    private EditText etStudentName, etEmail, etSchool, etStudentNumber;
    private Button btnCancelCommit, btnCommitProfile;
    private StudentBean student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_profile);

        student = (StudentBean) getIntent().getSerializableExtra("student");
        initView();
        initData();
    }

    private void initView() {
        etStudentName = findViewById(R.id.et_edit_student_name);
        etStudentNumber = findViewById(R.id.et_edit_student_number);
        etEmail = findViewById(R.id.et_edit_email);
        etSchool = findViewById(R.id.et_edit_school);

        btnCancelCommit = findViewById(R.id.btn_cancel_commit);
        btnCommitProfile = findViewById(R.id.btn_commit_profile);

        btnCommitProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudentBean newInfo = new StudentBean();
                newInfo.setStudentName(etStudentName.getText().toString());
                newInfo.setSchool(etSchool.getText().toString());
                newInfo.setEmail(etEmail.getText().toString());
                newInfo.setStudentNumber(etStudentNumber.getText().toString());
                //Toast.makeText(EditStudentProfileActivity.this, "点击了", Toast.LENGTH_SHORT).show();
                Log.i("NewInfo", newInfo.toString());
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
        etStudentName.setText(student.getStudentName());
        etStudentNumber.setText(student.getStudentNumber());
        etSchool.setText(student.getSchool());
        etEmail.setText(student.getEmail());
    }
}