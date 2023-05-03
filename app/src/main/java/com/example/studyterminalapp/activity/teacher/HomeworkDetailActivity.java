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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.adapter.teacher.FinishedRecyclerAdapter;
import com.example.studyterminalapp.adapter.teacher.HomeworkRecyclerAdapter;
import com.example.studyterminalapp.bean.ChapterBean;
import com.example.studyterminalapp.bean.Class;
import com.example.studyterminalapp.bean.Homework;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.vo.PageInfo;
import com.example.studyterminalapp.bean.vo.StudentHomeworkStatusVo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.google.gson.reflect.TypeToken;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.widget.picker.widget.TimePickerView;
import com.xuexiang.xui.widget.picker.widget.builder.TimePickerBuilder;
import com.xuexiang.xui.widget.picker.widget.configure.TimePickerType;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectChangeListener;
import com.xuexiang.xui.widget.picker.widget.listener.OnTimeSelectListener;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;
import com.xuexiang.xutil.data.DateUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class HomeworkDetailActivity extends AppCompatActivity {
    private Homework homework;
    private Class teacherClass;
    private Context context;
    private TextView tvOpenDate, tvDeadline;
    private EditText etHomeworkName;
    private Button btnUpdateHomework, btnOpenDate, btnDeadline;
    private MaterialSpinner msChapter;
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;
    private TimePickerView openDatePicker;
    private TimePickerView deadlinePicker;
    private List<ChapterBean> availableChapter;
    private DateTimeFormatter df;
    private RefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private FinishedRecyclerAdapter adapter;//声明适配器
    private List<StudentHomeworkStatusVo> homeworkStatusList;
    private int page = 1;
    private final int PAGE_SIZE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_detail);

        context = this;
        homework = (Homework) getIntent().getSerializableExtra("homework");
        teacherClass = (Class) getIntent().getSerializableExtra("class");

        initView();
        initListener();
        initData();
    }

    private void initView() {
        homeworkStatusList = new ArrayList<>();
        df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        availableChapter = new ArrayList<>();

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

        tvOpenDate = (TextView) findViewById(R.id.tv_open_date);
        tvDeadline = (TextView) findViewById(R.id.tv_deadline);
        etHomeworkName = (EditText) findViewById(R.id.et_homework_name);
        btnUpdateHomework = (Button) findViewById(R.id.btn_update_homework);
        btnOpenDate = (Button) findViewById(R.id.btn_open_date);
        btnDeadline = (Button) findViewById(R.id.btn_deadline);
        msChapter = (MaterialSpinner) findViewById(R.id.ms_chapter);


        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new FinishedRecyclerAdapter(context, homeworkStatusList);
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
                getFinishedList(PAGE_SIZE, page, 0);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
                page++;
                getFinishedList(PAGE_SIZE, page, 1);
            }
        });

        // adapter的item跳转
        adapter.setOnItemClickListener(new FinishedRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(context, "这是条目" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, IntermediateCorrectActivity.class);
                intent.putExtra("homeworkStatus", (StudentHomeworkStatusVo)homeworkStatusList.get(position));
                intent.putExtra("homework", (Homework) homework);
                startActivity(intent);
            }
        });


        btnOpenDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(DateUtils.string2Date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        DateUtils.yyyyMMddHHmmss.get()));
                openDatePicker = new TimePickerBuilder(context, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelected(Date date, View v) {
                        //ToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
                        tvOpenDate.setText(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
                    }
                })
                        .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                            @Override
                            public void onTimeSelectChanged(Date date) {
                                Log.i("pvTime", "onTimeSelectChanged");
                            }
                        })
                        .setType(TimePickerType.ALL)
                        .setTitleText("开始时间选择")
                        .isDialog(true)
                        .setOutSideCancelable(false)
                        .setDate(calendar)
                        .build();
                openDatePicker.show();
            }
        });

        btnDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(DateUtils.string2Date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        DateUtils.yyyyMMddHHmmss.get()));
                deadlinePicker = new TimePickerBuilder(context, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelected(Date date, View v) {
                        //ToastUtils.toast(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
                        tvDeadline.setText(DateUtils.date2String(date, DateUtils.yyyyMMddHHmmss.get()));
                    }
                })
                        .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                            @Override
                            public void onTimeSelectChanged(Date date) {
                                Log.i("pvTime", "onTimeSelectChanged");
                            }
                        })
                        .setType(TimePickerType.ALL)
                        .setTitleText("截止时间选择")
                        .isDialog(true)
                        .setOutSideCancelable(false)
                        .setDate(calendar)
                        .build();
                deadlinePicker.show();
            }
        });

        msChapter.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                ToastUtils.toast(availableChapter.get(position).toString());
            }
        });

        btnUpdateHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etHomeworkName.getText().toString().isEmpty() || tvOpenDate.getText().toString().isEmpty()
                        || tvDeadline.getText().toString().isEmpty()) {
                    ToastUtils.toast("请输入本次作业的完整信息");
                    return;
                }
                HashMap<String, Object> paramsMap = new HashMap<>();
                if (!etHomeworkName.getText().toString().isEmpty()) {
                    paramsMap.put("homeworkName", etHomeworkName.getText().toString());
                }
                paramsMap.put("classId", homework.getClassId());
                paramsMap.put("chapterId", availableChapter.get(msChapter.getSelectedIndex()).getCid());
                String openDate = tvOpenDate.getText().toString().replace(" ", "T");
                paramsMap.put("openDate", openDate);
                String deadline = tvDeadline.getText().toString().replace(" ", "T");
                paramsMap.put("deadline", deadline);
                try {
                    RequestManager.getInstance().PutRequest(paramsMap, Constants.HOMEWORK, new RequestManager.ResultCallback() {
                        @Override
                        public void onResponse(String responseCode, String json) {
                            Type dataType = new TypeToken<Result>(){}.getType();
                            Result response = JsonParse.getInstance().getResult(json, dataType);
                            int code = response.getStatus();
                            switch (code) {
                                case 200:
                                    //Log.d("TEST", "JSON: " + json);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ToastUtils.toast("更新成功");
                                        }
                                    });
                                    break;
                                default:
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();
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
        });
    }

    private void initData() {
        //msChapter.setItems()
        getCurrentChapter();
        getAvailableHomework();

        msChapter.setSelectedIndex(0);
        etHomeworkName.setText(homework.getHomeworkName());
        tvOpenDate.setText(df.format(homework.getOpenDate()));
        tvDeadline.setText(df.format(homework.getDeadline()));

        refreshLayout.autoRefresh();
    }

    private void getCurrentChapter() {
        String url = Constants.CHAPTER + "/" + homework.getChapterId();
        try {
            RequestManager.getInstance().GetRequest(new HashMap<>(), url, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String responseCode, String json) {
                    Type dataType = new TypeToken<Result<ChapterBean>>(){}.getType();
                    Result<ChapterBean> response = JsonParse.getInstance().getResult(json, dataType);
                    int code = response.getStatus();
                    switch (code) {
                        case 200:
                            availableChapter.add(response.getData());
                            break;
                        default:
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

    private void getAvailableHomework() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("cid", homework.getClassId());
        paramsMap.put("tid", teacherClass.getTextbookId());
        try {
            RequestManager.getInstance().GetRequest(paramsMap, Constants.AVAILABLE_HOMEWORK, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String responseCode, String json) {
                    Type dataType = new TypeToken<Result<List<ChapterBean>>>(){}.getType();
                    Result<List<ChapterBean>> response = JsonParse.getInstance().getResult(json, dataType);
                    int code = response.getStatus();
                    switch (code) {
                        case 200:
                            Log.d("TEST", "JSON: " + json);
                            availableChapter.addAll(response.getData());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    List<String> list = availableChapter.stream().map(ChapterBean::getChapterName).collect(Collectors.toList());
                                    msChapter.setItems(list);
                                }
                            });
                            break;
                        default:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, response.getMsg(), Toast.LENGTH_SHORT).show();
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

    private void getFinishedList(int pageSize, int pageNum, int requestCase) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("pageSize", pageSize);
        paramsMap.put("pageNum", pageNum);
        paramsMap.put("classId", homework.getClassId());
        paramsMap.put("chapterId", homework.getChapterId());
        try {
            RequestManager.getInstance().GetRequest(paramsMap, Constants.FINISHED_LIST, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String responseCode, String json) {
                    Type dataType = new TypeToken<Result<PageInfo<StudentHomeworkStatusVo>>>(){}.getType();
                    Result<PageInfo<StudentHomeworkStatusVo>> response = JsonParse.getInstance().getPageInfoResult(json, dataType);
                    int code = response.getStatus();
                    switch (code) {
                        case 200:
                            PageInfo<StudentHomeworkStatusVo> data = response.getData();
                            Log.d("Homework", "JSON: " + data.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (requestCase == 0 || requestCase == 2) {
                                        homeworkStatusList.clear();
                                    }
                                    homeworkStatusList.addAll(data.getList());
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
                                    homeworkStatusList.clear();
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
}