package com.example.studyterminalapp.activity.student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.bean.Homework;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.vo.QidAndStatusVo;
import com.example.studyterminalapp.bean.vo.SimpleHomeworkVo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentHomeworkDetailActivity extends AppCompatActivity {
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;
    private SimpleHomeworkVo simpleHomework;
    private Homework homework;
    private TextView tvHomeworkName, tvOpenDate, tvDeadline, tvOpenDateStatus,
            tvFinishedCount, tvQuestionCount;
    private Button btnDoHomework;
    // 该章节下某个用户已经完成的题目的id列表
    private List<QidAndStatusVo> completeQidList;
    // 该章节所有题目id列表
    private List<Integer> qidList;
    private DateTimeFormatter df;
    private boolean isDue;
    private Thread dataThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_homework_detail);

        simpleHomework = (SimpleHomeworkVo) getIntent().getSerializableExtra("simpleHomework");

        initView();
        initListener();
        initData();
    }

    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        tvTitle.setText("作业详情");
        df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        qidList = new ArrayList<>();
        completeQidList = new ArrayList<>();

        tvHomeworkName = (TextView) findViewById(R.id.tv_homework_name);
        tvOpenDate = (TextView) findViewById(R.id.tv_open_date);
        tvDeadline = (TextView) findViewById(R.id.tv_deadline);
        tvOpenDateStatus = (TextView) findViewById(R.id.tv_open_date_status);
        tvFinishedCount = (TextView) findViewById(R.id.tv_finished_count);
        tvQuestionCount = (TextView) findViewById(R.id.tv_question_count);
        btnDoHomework = (Button) findViewById(R.id.btn_do_homework);
    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnDoHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isDue) {
                    ToastUtils.toast("作业不在开放时间内，无法作答");
                    return;
                }
            }
        });
    }

    private void initData() {
        tvHomeworkName.setText(simpleHomework.getHomeworkName());
        tvOpenDate.setText(df.format(simpleHomework.getOpenDate()));
        tvDeadline.setText(df.format(simpleHomework.getDeadline()));
        String openDateStatus;
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(simpleHomework.getOpenDate())) {
            openDateStatus = "未开放";
            isDue = true;
        } else if (now.isAfter(simpleHomework.getDeadline())) {
            openDateStatus = "已截止";
            isDue = true;
        } else {
            openDateStatus = "进行中";
            isDue = false;
        }
        tvOpenDateStatus.setText(openDateStatus);

        // 获取作业
        getHomework();
        new Thread(()->{
            while (homework == null) {
                if (homework != null) {
                    break;
                }
            }
            getAllQid();
            getCompleteQid();
        }).start();
//        dataThread = new Thread(() -> {
//            while (qidList.isEmpty() || completeQidList.isEmpty()) {
//                if (!qidList.isEmpty() && !completeQidList.isEmpty()) {
//                    break;
//                }
//            }
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    tvFinishedCount.setText(String.valueOf(completeQidList.size()));
//                }
//            });
//        });
//        dataThread.start();
    }

    private void getHomework() {
        String url = Constants.HOMEWORK + "/" + simpleHomework.getHid();
        try {
            RequestManager.getInstance().GetRequest(new HashMap<>(), url, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String c, String json) {
                    Type dataType = new TypeToken<Result<Homework>>(){}.getType();
                    Result<Homework> result = JsonParse.getInstance().getResult(json, dataType);
                    Integer code = result.getStatus();
                    switch (code) {
                        case 200:
                            if (result.getData() != null) {
                                homework = result.getData();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tvQuestionCount.setText(String.valueOf(homework.getTotalCount()));
                                    }
                                });
                            }
                            break;
                        default:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.toast("查询班级作业异常-1");
                                }
                            });
                            break;
                    }
                }

                @Override
                public void onError(String msg) {
                    Log.i("Student Homework Detail", msg);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void getAllQid() {
        try {
            HashMap<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("cid", homework.getChapterId());
            RequestManager.getInstance().GetRequest(paramsMap, Constants.HOMEWORK_QUESTION_LIST, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String c, String json) {
                    Type dataType = new TypeToken<Result<List<Integer>>>(){}.getType();
                    Result<List<Integer>> result = JsonParse.getInstance().getResult(json, dataType);
                    Integer code = result.getStatus();
                    switch (code) {
                        case 200:
                            if (result.getData() != null) {
                                qidList = result.getData();
                            }
                            break;
                        default:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.toast("查询班级作业异常-2");
                                }
                            });
                            break;
                    }
                }

                @Override
                public void onError(String msg) {
                    Log.i("Student Homework Detail", msg);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void getCompleteQid() {
        try {
            HashMap<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("uid", MyApp.getId());
            paramsMap.put("cid", homework.getChapterId());
            RequestManager.getInstance().GetRequest(paramsMap, Constants.STUDENT_FINISHED_LIST, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String c, String json) {
                    Type dataType = new TypeToken<Result<List<QidAndStatusVo>>>(){}.getType();
                    Result<List<QidAndStatusVo>> result = JsonParse.getInstance().getResult(json, dataType);
                    Integer code = result.getStatus();
                    switch (code) {
                        case 200:
                            if (result.getData() != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        completeQidList = result.getData();
                                        tvFinishedCount.setText(String.valueOf(completeQidList.size()));
                                    }
                                });
                            }
                            break;
                        default:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.toast("查询班级作业异常-3");
                                }
                            });
                            break;
                    }
                }

                @Override
                public void onError(String msg) {
                    Log.i("Student Homework Detail", msg);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}