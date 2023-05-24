package com.example.studyterminalapp.activity.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.teacher.ClassHomeworkActivity;
import com.example.studyterminalapp.adapter.student.StudentHomeAdapter;
import com.example.studyterminalapp.adapter.student.StudentHomeRecyclerAdapter;
import com.example.studyterminalapp.adapter.teacher.ClassRecyclerAdapter;
import com.example.studyterminalapp.bean.Class;
import com.example.studyterminalapp.bean.HomeClassBean;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.SaTokenInfo;
import com.example.studyterminalapp.bean.vo.PageInfo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.example.studyterminalapp.views.ClassListView;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class StudentHomeActivity extends AppCompatActivity {

    private RelativeLayout rlTitleBar;
    private ImageView ivBack;
    private EditText etSearchContent;
    private MaterialSpinner msSearchType;
    private RoundButton btnSearchContent;
    private RelativeLayout rlBottomBar;
    private StudentHomeAdapter studentHomeAdapter;
    private ClassListView clvList;
    private Button btnProfile;
    private int uid;

    private Context context;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<HomeClassBean> result;
    private int page = 1;
    private final int PAGE_SIZE = 10;
    private StudentHomeRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_home);

        // uid = getIntent().getIntExtra("id", 6);
        uid = MyApp.getId();
        context = this;

        initView();
        initListener();
        initData();
    }


    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));

        ivBack = findViewById(R.id.iv_back);
        ivBack.setVisibility(View.INVISIBLE);

        msSearchType = findViewById(R.id.ms_search_type);
        etSearchContent = findViewById(R.id.et_search_content);
        btnSearchContent = findViewById(R.id.btn_search_content);
        btnProfile = findViewById(R.id.btn_profile);

        rlBottomBar = findViewById(R.id.bottom_bar);
//        clvList = findViewById(R.id.clv_list);
//
//        studentHomeAdapter = new StudentHomeAdapter(this);
//        clvList.setAdapter(studentHomeAdapter);

        //btnSearchContent.setOnClickListener(this);
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Log.i("Home", this.getClass().toString() + "跳转到Profile");
                Intent intent = new Intent(StudentHomeActivity.this, StudentProfileActivity.class);
                intent.putExtra("id", uid);
                StudentHomeActivity.this.startActivity(intent);
            }
        });

        result = new ArrayList<>();
        adapter = new StudentHomeRecyclerAdapter(context, result);
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
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
        recyclerView.setAdapter(adapter);
    }

    private void initListener() {
        // refresh刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                refreshlayout.setEnableLoadMore(true);
                getHomeClass(PAGE_SIZE, page, 0);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                page++;
                getHomeClass(PAGE_SIZE, page, 1);
            }
        });

        // adapter的item跳转
        adapter.setOnItemClickListener(new StudentHomeRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // Toast.makeText(context, "这是条目" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ClassDetailActivity.class);
                intent.putExtra("class", (HomeClassBean) adapter.getItem(position));
                startActivity(intent);
            }
        });

        // 搜索键
        btnSearchContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page = 1;
                getHomeClass(PAGE_SIZE, page, 2);
            }
        });

    }

    private void initData1(int pageSize, int pageNum, int requestCase) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("pageSize", 1000);
        paramsMap.put("pageNum", 1);
        paramsMap.put("uid", uid);
        try {
            RequestManager.getInstance().GetRequest(paramsMap, Constants.USER_CLASS_LIST, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String c, String json) {

                    //Log.d("TEST", "JSON: " + json);
                    Type dataType = new TypeToken<Result<PageInfo<HomeClassBean>>>(){}.getType();
                    Result<PageInfo<HomeClassBean>> result = JsonParse.getInstance().getPageInfoResult(json, dataType);
                    Integer code = result.getStatus();
                    switch (code) {
                        case 200:
                            PageInfo<HomeClassBean> pageInfo = result.getData();
                            List<HomeClassBean> data = pageInfo.getList();
                            if (data == null || data.isEmpty()) {
                                return;
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    studentHomeAdapter.setData(data);
                                }
                            });
                            break;
                        default:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(StudentHomeActivity.this, "查询异常", Toast.LENGTH_SHORT).show();
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

    private void initData() {
        refreshLayout.autoRefresh();
    }

    // requestCase => 0 refresh; 1 load; 2 search
    private void getHomeClass(int pageSize, int pageNum, int requestCase) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("pageSize", pageSize);
        paramsMap.put("pageNum", pageNum);
        paramsMap.put("uid", uid);
        try {
            RequestManager.getInstance().GetRequest(paramsMap, Constants.USER_CLASS_LIST, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String responseCode, String json) {
                    Type dataType = new TypeToken<Result<PageInfo<HomeClassBean>>>(){}.getType();
                    Result<PageInfo<HomeClassBean>> response = JsonParse.getInstance().getPageInfoResult(json, dataType);
                    int code = response.getStatus();
                    switch (code) {
                        case 200:
                            Log.d("TEST", "JSON: " + json);
                            PageInfo<HomeClassBean> data = response.getData();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (requestCase == 0 || requestCase == 2) {
                                        result.clear();
                                    }
                                    result.addAll(data.getList());
                                    String content = etSearchContent.getText().toString();
                                    if (!content.isEmpty()) {
                                        List<HomeClassBean> r = result.stream().filter(c -> c.getClassName().contains(content)).collect(Collectors.toList());
                                        result.clear();
                                        result = r;
                                    }
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
                    Log.i("Home Page Error", msg);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


//    @Override
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.btn_search_content:
//                String content = etSearchContent.getText().toString();
//                String type = msSearchType.getText().toString();
//                //Toast.makeText(this, "搜索了 "+type+","+content, Toast.LENGTH_SHORT).show();
//                getHomeClass(PAGE_SIZE, page, 0);
//                break;
//        }
//    }

}