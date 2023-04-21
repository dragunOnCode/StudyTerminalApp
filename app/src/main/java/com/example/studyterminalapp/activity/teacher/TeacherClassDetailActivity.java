package com.example.studyterminalapp.activity.teacher;

import static com.xuexiang.xutil.XUtil.runOnUiThread;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.admin.EditTextbookActivity;
import com.example.studyterminalapp.adapter.teacher.ResourceListAdapter;
import com.example.studyterminalapp.adapter.teacher.StudentListAdapter;
import com.example.studyterminalapp.bean.Class;
import com.example.studyterminalapp.bean.HomeClassBean;
import com.example.studyterminalapp.bean.Resource;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.StudentBean;
import com.example.studyterminalapp.bean.Textbook;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.example.studyterminalapp.views.ChapterListView;
import com.example.studyterminalapp.views.ResourceListView;
import com.example.studyterminalapp.views.StudentListView;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.layout.ExpandableLayout;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TeacherClassDetailActivity extends AppCompatActivity {
    private Class teacherClass;
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;
    private ExpandableLayout elResource, elStudent;
    private TextView tvResource, tvClassStudent;
    private MaterialEditText etClassName, etSchool;
    private MaterialSpinner msCourseName, msGrade;
    private MultiLineEditText etCourseDescription;
    private Button btnCommitEdit, btnAddStudent, btnRemoveStudent, btnChangeTextbook, btnAddResource;
    private ResourceListView rlvList;
    private boolean isResourceExpanded, isStudentExpanded;
    private ResourceListAdapter resourceListAdapter;
    private StudentListAdapter studentListAdapter;
    private StudentListView slvList;
    // textbook
    private ImageView ivTextbookPic;
    private TextView tvTextbookName, tvTextbookAuthor, tvPressName, tvCourseName, tvGrade;
    private RelativeLayout bookItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_class_detail);

        teacherClass = (Class) getIntent().getSerializableExtra("class");

        initView();
        initListener();
        initData();
    }

    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        elResource = (ExpandableLayout) findViewById(R.id.el_resource);
        tvResource = (TextView) findViewById(R.id.tv_resource);

        tvTitle.setText("班级管理");

        isResourceExpanded = false;
        etClassName = (MaterialEditText) findViewById(R.id.et_class_name);
        etSchool = (MaterialEditText) findViewById(R.id.et_school);
        msCourseName = (MaterialSpinner) findViewById(R.id.ms_course_name);
        msGrade = (MaterialSpinner) findViewById(R.id.ms_grade);
        etCourseDescription = (MultiLineEditText) findViewById(R.id.et_course_description);
        btnCommitEdit = (Button) findViewById(R.id.btn_commit_edit);
        rlvList = (ResourceListView) findViewById(R.id.rlv_list);
        slvList = (StudentListView) findViewById(R.id.slv_list);

        elStudent = (ExpandableLayout) findViewById(R.id.el_student);
        tvClassStudent = (TextView) findViewById(R.id.tv_class_student);
        isStudentExpanded = false;

        resourceListAdapter = new ResourceListAdapter(this, 1);
        rlvList.setAdapter(resourceListAdapter);

        studentListAdapter = new StudentListAdapter(this);
        slvList.setAdapter(studentListAdapter);

        btnAddStudent = (Button) findViewById(R.id.btn_add_student);
        btnRemoveStudent = (Button) findViewById(R.id.btn_remove_student);
        btnChangeTextbook = (Button) findViewById(R.id.btn_change_textbook);

        ivTextbookPic = (ImageView) findViewById(R.id.iv_textbook_pic);
        tvTextbookName = (TextView) findViewById(R.id.tv_textbook_name);
        tvTextbookAuthor = (TextView) findViewById(R.id.tv_textbook_author);
        tvPressName = (TextView) findViewById(R.id.tv_press_name);
        tvCourseName = (TextView) findViewById(R.id.tv_course_name);
        tvGrade = (TextView) findViewById(R.id.tv_grade);
        bookItem = (RelativeLayout) findViewById(R.id.book_item);

        btnAddResource = (Button) findViewById(R.id.btn_add_resource);
    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        elResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(TeacherClassDetailActivity.this, "点击", Toast.LENGTH_SHORT).show();
            }
        });

        tvResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isResourceExpanded = !isResourceExpanded;
                elResource.setExpanded(isResourceExpanded, true);
            }
        });

        tvClassStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStudentExpanded = !isStudentExpanded;
                elStudent.setExpanded(isStudentExpanded, true);
            }
        });

        btnCommitEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etClassName.isEmpty() || etSchool.isEmpty()) {
                    return;
                }
                HashMap<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("cid", teacherClass.getCid());
                paramsMap.put("className", etClassName.getText().toString());
                paramsMap.put("schoolName", etSchool.getText().toString());
                paramsMap.put("courseName", msCourseName.getText().toString());
                paramsMap.put("grade", msGrade.getText().toString());
                if (!etCourseDescription.isEmpty()) {
                    paramsMap.put("courseDescription", etCourseDescription.getContentText());
                }
                try {
                    RequestManager.getInstance().PutRequest(paramsMap, Constants.CLASS, new RequestManager.ResultCallback() {
                        @Override
                        public void onResponse(String responseCode, String response) {
                            int code = Integer.parseInt(responseCode);
                            switch (code) {
                                case 200:
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(TeacherClassDetailActivity.this,
                                                    "更新班级信息成功", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                default:
                                    break;
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            Log.i("Update Class", msg);
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        btnChangeTextbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherClassDetailActivity.this, ChangeTextbookActivity.class);
                intent.putExtra("cid", teacherClass.getCid());
                TeacherClassDetailActivity.this.startActivity(intent);
            }
        });

        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo添加学生
                Intent intent = new Intent(TeacherClassDetailActivity.this, AddStudentActivity.class);
                intent.putExtra("cid", teacherClass.getCid());
                TeacherClassDetailActivity.this.startActivity(intent);
            }
        });

        btnAddResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo:添加资源
                Intent intent = new Intent(TeacherClassDetailActivity.this, AddResourceActivity.class);
                intent.putExtra("classId", teacherClass.getCid());
                TeacherClassDetailActivity.this.startActivity(intent);
            }
        });

        btnRemoveStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.d("Remove Student", studentListAdapter.getCheckedMap().keySet().toString());
                new MaterialDialog.Builder(TeacherClassDetailActivity.this)
                        .content("是否要删除学生？")
                        .positiveText("是")
                        .negativeText("否")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                HashMap<String, Object> paramsMap = new HashMap<>();
                                paramsMap.put("cid", teacherClass.getCid());
                                ArrayList<Integer> userIds = new ArrayList<>(studentListAdapter.getCheckedMap().keySet());
                                paramsMap.put("userIds", userIds);
                                try {
                                    RequestManager.getInstance().PostRequest(paramsMap, Constants.USER_CLASS_BATCH_DELETE,
                                            new RequestManager.ResultCallback() {
                                        @Override
                                        public void onResponse(String responseCode, String response) {
                                            int code = Integer.parseInt(responseCode);
                                            switch (code) {
                                                case 200:
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(TeacherClassDetailActivity.this,
                                                                    "删除成功", Toast.LENGTH_SHORT).show();
                                                            studentListAdapter.getCheckedMap().clear();
                                                        }
                                                    });
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }

                                        @Override
                                        public void onError(String msg) {
                                            Log.i("Delete Student", msg);
                                        }
                                    });
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void initData() {
        etClassName.setText(teacherClass.getClassName());
        etSchool.setText(teacherClass.getSchoolName());
        msCourseName.setText(teacherClass.getCourseName());
        msGrade.setText(teacherClass.getGrade());
        etCourseDescription.setContentText(teacherClass.getCourseDescription());

        // 请求resource
        HashMap<String, Object> resourceParams = new HashMap<>();
        resourceParams.put("cid", teacherClass.getCid());
        try {
            RequestManager.getInstance().GetRequest(resourceParams, Constants.CLASS_RESOURCE, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String code, String json) {

                    //Log.d("TEST", "JSON: " + json);
                    Type dataType = new TypeToken<Result<List<Resource>>>(){}.getType();
                    Result<List<Resource>> result = JsonParse.getInstance().getResult(json, dataType);
                    List<Resource> data = result.getData();
                    if (data == null || data.isEmpty()) {
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resourceListAdapter.setData(data);
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

        // 请求学生
        HashMap<String, Object> studentParams = new HashMap<>();
        studentParams.put("cid", teacherClass.getCid());
        try {
            RequestManager.getInstance().GetRequest(studentParams, Constants.CLASS_ALL_STUDENT, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String code, String json) {

                    //Log.d("TEST", "JSON: " + json);
                    Type dataType = new TypeToken<Result<List<StudentBean>>>(){}.getType();
                    Result<List<StudentBean>> result = JsonParse.getInstance().getResult(json, dataType);
                    List<StudentBean> data = result.getData();
                    if (data == null || data.isEmpty()) {
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            studentListAdapter.setData(data);
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

        // 请求教辅
        if (teacherClass.getTextbookId() == null) {
            bookItem.setVisibility(View.INVISIBLE);
        } else {
            String url = Constants.TEXTBOOK + "/" + teacherClass.getTextbookId().toString();
            try {
                RequestManager.getInstance().GetRequest(new HashMap<>(), url, new RequestManager.ResultCallback() {
                    @Override
                    public void onResponse(String code, String json) {
                        //Log.d("TEST", "JSON: " + json);
                        Type dataType = new TypeToken<Result<Textbook>>(){}.getType();
                        Result<Textbook> result = JsonParse.getInstance().getResult(json, dataType);
                        Textbook textbook = result.getData();
                        if (textbook == null) {
                            return;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvTextbookName.setText(textbook.getTextbookName());
                                tvTextbookAuthor.setText(textbook.getTextbookAuthor());
                                tvPressName.setText(textbook.getPressName());
                                tvCourseName.setText(textbook.getCourseName());
                                tvGrade.setText(textbook.getGrade());
                                Glide.with(TeacherClassDetailActivity.this)
                                        .load(textbook.getTextbookPic())
                                        .error(R.mipmap.ic_launcher)
                                        .into(ivTextbookPic);
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

    }
}