package com.example.studyterminalapp.activity.admin;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.adapter.admin.QuestionRecyclerAdapter;
import com.example.studyterminalapp.adapter.admin.TextbookRecyclerAdapter;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.Textbook;
import com.example.studyterminalapp.bean.vo.PageInfo;
import com.example.studyterminalapp.bean.vo.QuestionVo;
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
import com.xuexiang.xui.widget.imageview.preview.PreviewBuilder;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;
import com.xuexiang.xutil.common.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminQuestionActivity extends AppCompatActivity {
    private int aid;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private QuestionRecyclerAdapter adapter;//声明适配器
    private Context context;
    private List<QuestionVo> result;
    private int page = 1;
    private final int PAGE_SIZE = 10;
    private RippleView btnAddQuestion;
    private MaterialSpinner msSearchType;
    private EditText etSearchContent;
    private RoundButton btnSearchContent;
    private TextView tvQuestionTotal;
    private String searchContent;
    private String searchType;
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_question);

        context = this;
        aid = MyApp.getId();

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

        tvTitle.setText("题库管理");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnAddQuestion = (RippleView) findViewById(R.id.btn_add_question);
        msSearchType = (MaterialSpinner) findViewById(R.id.ms_search_type);
        etSearchContent = (EditText) findViewById(R.id.et_search_content);
        btnSearchContent = (RoundButton) findViewById(R.id.btn_search_content);
        tvQuestionTotal = (TextView) findViewById(R.id.tv_question_total);

        searchType = msSearchType.getText().toString();
        searchContent = etSearchContent.getText().toString();

        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new QuestionRecyclerAdapter(context, result);
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
                getQuestion(PAGE_SIZE, page, searchType, searchContent, 0);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                page++;
                getQuestion(PAGE_SIZE, page, searchType, searchContent, 1);
            }
        });

        // adapter的item跳转
        adapter.setOnItemClickListener(new TextbookRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(context, "这是条目" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminQuestionActivity.this, QuestionDetailActivity.class);
                intent.putExtra("question", (QuestionVo) adapter.getItem(position));
                AdminQuestionActivity.this.startActivity(intent);
            }
        });

        // 搜索键
        btnSearchContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchContent = etSearchContent.getText().toString();
                searchType = msSearchType.getText().toString();
                page = 1;
                getQuestion(PAGE_SIZE, page, searchType, searchContent, 2);
            }
        });

        // 新增题目
        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "新增", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AdminQuestionActivity.this, AddQuestionActivity.class);
                intent.putExtra("id", aid);
                context.startActivity(intent);
            }
        });

    }

    // requestCase => 0 refresh; 1 load; 2 search
    private void getQuestion(int pageSize, int pageNum, String courseName, String content, int requestCase) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        if (!StringUtils.isEmpty(content)) {
            paramsMap.put("content", content);
        }
        paramsMap.put("courseName", courseName);
        paramsMap.put("pageNum", pageNum);
        paramsMap.put("pageSize", pageSize);
        try {
            RequestManager.getInstance().GetRequest(paramsMap, Constants.QUESTION_SEARCH, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String responseCode, String json) {
                    Type dataType = new TypeToken<Result<PageInfo<QuestionVo>>>(){}.getType();
                    Result<PageInfo<QuestionVo>> response = JsonParse.getInstance().getPageInfoResult(json, dataType);
                    int code = response.getStatus();
                    switch (code) {
                        case 200:
                            //Log.d("TEST", "JSON: " + json);
                            PageInfo<QuestionVo> data = response.getData();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (requestCase == 0 || requestCase == 2) {
                                        result.clear();
                                    }
                                    result.addAll(data.getList());
                                    tvQuestionTotal.setText("总共 " + data.getTotal() + " 条结果");
                                    adapter.notifyDataSetChanged();
                                    if (data.isIsLastPage()) {
                                        AdminQuestionActivity.this.refreshLayout.setEnableLoadMore(false);
                                    }
                                }
                            });
                            break;
                        default:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, "查询结果为空", Toast.LENGTH_SHORT).show();
                                    result.clear();
                                    tvQuestionTotal.setText("总共 0 条结果");
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