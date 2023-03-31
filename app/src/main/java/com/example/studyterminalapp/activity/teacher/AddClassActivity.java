package com.example.studyterminalapp.activity.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.admin.AddTextbookActivity;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class AddClassActivity extends AppCompatActivity {
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;
    private int tid;
    private MaterialEditText etClassName, etSchool;
    private MaterialSpinner msCourseName, msGrade;
    private Button btnCommitAdd;
    private MultiLineEditText etCourseDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        tid = MyApp.getId();

        initView();
        initListener();
    }

    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);

        tvTitle.setText("新增班级");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etClassName = (MaterialEditText) findViewById(R.id.et_class_name);
        etSchool = (MaterialEditText) findViewById(R.id.et_school);
        etCourseDescription = (MultiLineEditText) findViewById(R.id.et_course_description);
        msCourseName = (MaterialSpinner) findViewById(R.id.ms_course_name);
        msGrade = (MaterialSpinner) findViewById(R.id.ms_grade);
        btnCommitAdd = (Button) findViewById(R.id.btn_commit_add);
    }

    private void initListener() {
        btnCommitAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etClassName.isEmpty() || etSchool.isEmpty() || etCourseDescription.isEmpty()) {
                    Toast.makeText(AddClassActivity.this, "请输入完整的班级信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                HashMap<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("className", etClassName.getText().toString());
                paramsMap.put("courseName", msCourseName.getText().toString());
                paramsMap.put("grade", msGrade.getText().toString());
                paramsMap.put("schoolName", etSchool.getText().toString());
                paramsMap.put("courseDescription", etCourseDescription.getContentText().toString());
                try {
                    RequestManager.getInstance().PostRequest(paramsMap, Constants.CLASS,
                            new RequestManager.ResultCallback() {
                                @Override
                                public void onResponse(String responseCode, String response) {
                                    Type dataType = new TypeToken<Result>(){}.getType();
                                    Result result = JsonParse.getInstance().getResult(response, dataType);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(AddClassActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    finish();
                                }

                                @Override
                                public void onError(String msg) {
                                    Log.i("Update Student Profile", msg);
                                }
                            });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}