package com.example.studyterminalapp.activity.teacher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.adapter.teacher.StudentRecyclerAdapter;
import com.example.studyterminalapp.bean.Class;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.StudentBean;
import com.example.studyterminalapp.bean.vo.PageInfo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddStudentActivity extends AppCompatActivity {
    private int cid;
    private MaterialEditText etStudentName, etStudentNumber, etSchool;
    private Button btnSearchStudent, btnAddStudent;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<StudentBean> result;
    private int page = 1;
    private final int PAGE_SIZE = 10;
    private StudentRecyclerAdapter studentRecyclerAdapter;
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        cid = getIntent().getIntExtra("cid", 1);

        initView();
        initListener();
    }

    private void initView() {
        result = new ArrayList<>();

        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);

        tvTitle.setText("班级管理");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        etStudentName = (MaterialEditText) findViewById(R.id.et_student_name);
        etStudentNumber = (MaterialEditText) findViewById(R.id.et_student_number);
        etSchool = (MaterialEditText) findViewById(R.id.et_school);
        btnSearchStudent = (Button) findViewById(R.id.btn_search_student);
        btnAddStudent = (Button) findViewById(R.id.btn_add_student);

        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        studentRecyclerAdapter = new StudentRecyclerAdapter(this, result);

        /*
        与ListView效果对应的可以通过LinearLayoutManager来设置
        与GridView效果对应的可以通过GridLayoutManager来设置
        与瀑布流对应的可以通过StaggeredGridLayoutManager来设置
        */
        //创建线性布局
        LinearLayoutManager manager = new LinearLayoutManager(this);
        //垂直方向
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        //给RecyclerView设置布局管理器
        recyclerView.setLayoutManager(manager);
        //创建适配器，并且设置
        recyclerView.setAdapter(studentRecyclerAdapter);
    }

    private void initListener() {
        // refresh刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                refreshlayout.setEnableLoadMore(true);
                searchStudent(PAGE_SIZE, page, etStudentName.getText().toString(),
                        etSchool.getText().toString(), etStudentNumber.getText().toString(), 0);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                page++;
                searchStudent(PAGE_SIZE, page, etStudentName.getText().toString(),
                        etSchool.getText().toString(), etStudentNumber.getText().toString(), 1);
            }
        });

        // 搜索键
        btnSearchStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                searchStudent(PAGE_SIZE, page, etStudentName.getText().toString(),
                        etSchool.getText().toString(), etStudentNumber.getText().toString(), 2);
            }
        });

        // 添加学生
        btnAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(AddStudentActivity.this)
                        .content("是否要添加学生？")
                        .positiveText("是")
                        .negativeText("否")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                HashMap<String, Object> paramsMap = new HashMap<>();
                                paramsMap.put("cid", cid);
                                ArrayList<Integer> userIds = new ArrayList<>(studentRecyclerAdapter.getCheckedMap().keySet());
                                paramsMap.put("userIds", userIds);
                                try {
                                    RequestManager.getInstance().PostRequest(paramsMap, Constants.USER_CLASS_BATCH_ADD,
                                            new RequestManager.ResultCallback() {
                                                @Override
                                                public void onResponse(String responseCode, String response) {
                                                    int code = Integer.parseInt(responseCode);
                                                    switch (code) {
                                                        case 200:
                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    Toast.makeText(AddStudentActivity.this,
                                                                            "添加成功", Toast.LENGTH_SHORT).show();
                                                                    studentRecyclerAdapter.getCheckedMap().clear();
                                                                }
                                                            });
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                }

                                                @Override
                                                public void onError(String msg) {
                                                    Log.i("ADD Student", msg);
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


    // requestCase => 0 refresh; 1 load; 2 search
    private void searchStudent(int pageSize, int pageNum, String nickname, String school, String studentNumber, int requestCase) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        if (etStudentName.isEmpty()) {
            return;
        }
        paramsMap.put("nickname", nickname);
        if (!etSchool.isEmpty()) {
            paramsMap.put("school", school);
        }
        if (!etStudentNumber.isEmpty()) {
            paramsMap.put("studentNumber", studentNumber);
        }
        paramsMap.put("pageNum", pageNum);
        paramsMap.put("pageSize", pageSize);
        try {
            RequestManager.getInstance().GetRequest(paramsMap, Constants.USER_SEARCH, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String responseCode, String json) {
                    Type dataType = new TypeToken<Result<PageInfo<StudentBean>>>(){}.getType();
                    Result<PageInfo<StudentBean>> response = JsonParse.getInstance().getPageInfoResult(json, dataType);
                    int code = response.getStatus();
                    switch (code) {
                        case 200:
                            //Log.d("TEST", "JSON: " + json);
                            PageInfo<StudentBean> data = response.getData();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (requestCase == 0 || requestCase == 2) {
                                        result.clear();
                                    }
                                    result.addAll(data.getList());
                                    studentRecyclerAdapter.notifyDataSetChanged();
                                    if (data.isIsLastPage()) {
                                        AddStudentActivity.this.refreshLayout.setEnableLoadMore(false);
                                    }
                                }
                            });
                            break;
                        default:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddStudentActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                                    result.clear();
                                    studentRecyclerAdapter.notifyDataSetChanged();
                                }
                            });
                            break;
                    }

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