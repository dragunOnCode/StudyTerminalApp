package com.example.studyterminalapp.activity.teacher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.adapter.teacher.ClassRecyclerAdapter;
import com.example.studyterminalapp.bean.Class;
import com.example.studyterminalapp.bean.Result;
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
import com.xuexiang.xui.widget.button.RippleView;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeacherHomeworkActivity extends AppCompatActivity {
    private int tid;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ClassRecyclerAdapter adapter;//声明适配器
    private Context context;
    private List<Class> result;
    private int page = 1;
    private final int PAGE_SIZE = 10;
    private MaterialSpinner msSearchType, msGrade;
    private RoundButton btnSearchContent;
    private TextView tvClassTotal;
    private String searchGrade;
    private String searchType;
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_homework);

        context = this;
        tid = MyApp.getId();

        initView();
        initListener();
        initData();
    }

    private void initData() {
        refreshLayout.autoRefresh();
    }

    private void initView() {
        result = new ArrayList<>();

        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);

        tvTitle.setText("作业管理");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        msSearchType = (MaterialSpinner) findViewById(R.id.ms_search_type);
        msGrade = (MaterialSpinner) findViewById(R.id.ms_grade);
        btnSearchContent = (RoundButton) findViewById(R.id.btn_search_content);
        tvClassTotal = (TextView) findViewById(R.id.tv_class_total);

        searchType = msSearchType.getText().toString();
        searchGrade = msGrade.getText().toString();

        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new ClassRecyclerAdapter(context, result);
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
        // refresh刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                refreshlayout.setEnableLoadMore(true);
                getTeacherClass(PAGE_SIZE, page, searchType, searchGrade, 0);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                page++;
                getTeacherClass(PAGE_SIZE, page, searchType, searchGrade, 1);
            }
        });

        // adapter的item跳转
        adapter.setOnItemClickListener(new ClassRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Toast.makeText(context, "这是条目" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ClassHomeworkActivity.class);
                intent.putExtra("class", (Class) adapter.getItem(position));
                startActivity(intent);
            }
        });

        // 搜索键
        btnSearchContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchGrade = msGrade.getText().toString();
                searchType = msSearchType.getText().toString();
                page = 1;
                getTeacherClass(PAGE_SIZE, page, searchType, searchGrade, 2);
            }
        });

    }

    // requestCase => 0 refresh; 1 load; 2 search
    private void getTeacherClass(int pageSize, int pageNum, String courseName, String grade, int requestCase) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("oid", tid);
        paramsMap.put("grade", grade);
        paramsMap.put("courseName", courseName);
        paramsMap.put("pageNum", pageNum);
        paramsMap.put("pageSize", pageSize);
        try {
            RequestManager.getInstance().GetRequest(paramsMap, Constants.CLASS_SEARCH, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String responseCode, String json) {
                    Type dataType = new TypeToken<Result<PageInfo<Class>>>(){}.getType();
                    Result<PageInfo<Class>> response = JsonParse.getInstance().getPageInfoResult(json, dataType);
                    int code = response.getStatus();
                    switch (code) {
                        case 200:
                            Log.d("TEST", "JSON: " + json);
                            PageInfo<Class> data = response.getData();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (requestCase == 0 || requestCase == 2) {
                                        result.clear();
                                    }
                                    result.addAll(data.getList());
                                    tvClassTotal.setText("总共 " + data.getTotal() + " 条结果");
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
                                    tvClassTotal.setText("总共 0 条结果");
                                    adapter.notifyDataSetChanged();
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