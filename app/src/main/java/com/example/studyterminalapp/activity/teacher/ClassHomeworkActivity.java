package com.example.studyterminalapp.activity.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
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
import com.example.studyterminalapp.activity.admin.AdminTextbookActivity;
import com.example.studyterminalapp.activity.admin.TextbookDetailActivity;
import com.example.studyterminalapp.adapter.teacher.HomeworkRecyclerAdapter;
import com.example.studyterminalapp.bean.Class;
import com.example.studyterminalapp.bean.Homework;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.Textbook;
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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ClassHomeworkActivity extends AppCompatActivity {
    private Class teacherClass;
    private int tid;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private HomeworkRecyclerAdapter adapter;//声明适配器
    private Context context;
    private List<Homework> result;
    private int page = 1;
    private final int PAGE_SIZE = 10;
    private Button btnAddHomework;
    private TextView tvClassName, tvCourseName, tvSchool, tvGrade, tvHomework;
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_homework);

        tid = MyApp.getId();
        context = this;
        teacherClass = (Class) getIntent().getSerializableExtra("class");

        initView();
        initListener();
        initData();
    }

    private void initView() {
        result = new ArrayList<>();

        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);

        tvTitle.setText("班级作业管理");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAddHomework = (Button) findViewById(R.id.btn_add_homework);
        tvClassName = (TextView) findViewById(R.id.tv_class_name);
        tvCourseName = (TextView) findViewById(R.id.tv_course_name);
        tvSchool = (TextView) findViewById(R.id.tv_school);
        tvGrade = (TextView) findViewById(R.id.tv_grade);
        tvHomework = (TextView) findViewById(R.id.tv_homework);

        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new HomeworkRecyclerAdapter(context, result);
        /*
        与ListView效果对应的可以通过LinearLayoutManager来设置
        与GridView效果对应的可以通过GridLayoutManager来设置
        与瀑布流对应的可以通过StaggeredGridLayoutManager来设置
        */
        //创建线性布局
        LinearLayoutManager manager = new LinearLayoutManager(context);
        //垂直方向
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        //给RecyclerView设置布局管理器
        recyclerView.setLayoutManager(manager);
        //创建适配器，并且设置
        recyclerView.setAdapter(adapter);
    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // refresh刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                refreshlayout.setEnableLoadMore(true);
                getHomework(PAGE_SIZE, page, 0);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                page++;
                getHomework(PAGE_SIZE, page, 1);
            }
        });

        // adapter的item跳转
        adapter.setOnItemClickListener(new HomeworkRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(context, "这是条目" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, HomeworkDetailActivity.class);
                intent.putExtra("homework", (Homework) adapter.getItem(position));
                intent.putExtra("class", (Class) teacherClass);
                context.startActivity(intent);
            }
        });


        // 发布作业
        btnAddHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "发布作业", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, AddHomeworkActivity.class);
                intent.putExtra("class", teacherClass);
                context.startActivity(intent);
            }
        });


    }

    // requestCase => 0 refresh; 1 load; 2 search
    private void getHomework(int pageSize, int pageNum, int requestCase) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("pageSize", pageSize);
        paramsMap.put("pageNum", pageNum);
        paramsMap.put("cid", teacherClass.getCid());
        try {
            RequestManager.getInstance().GetRequest(paramsMap, Constants.HOMEWORK_LIST, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String responseCode, String json) {
                    Type dataType = new TypeToken<Result<PageInfo<Homework>>>(){}.getType();
                    Result<PageInfo<Homework>> response = JsonParse.getInstance().getPageInfoResult(json, dataType);
                    int code = response.getStatus();
                    switch (code) {
                        case 200:
                            PageInfo<Homework> data = response.getData();
                            Log.d("Homework", "JSON: " + data.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (requestCase == 0 || requestCase == 2) {
                                        result.clear();
                                    }
                                    result.addAll(data.getList());
                                    adapter.notifyDataSetChanged();
                                    if (data.isIsLastPage()) {
                                        refreshLayout.setEnableLoadMore(false);
                                    }
                                }
                            });
                            break;
                        default:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();
                                    result.clear();
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            break;
                    }

                }

                @Override
                public void onError(String msg) {
                    Log.i("Class Homework Error", msg);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        tvClassName.setText(teacherClass.getClassName());
        tvSchool.setText(teacherClass.getSchoolName());
        tvCourseName.setText(teacherClass.getCourseName());
        tvGrade.setText(teacherClass.getGrade());

        refreshLayout.autoRefresh();
    }
}