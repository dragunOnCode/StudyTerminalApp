package com.example.studyterminalapp.activity.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.example.studyterminalapp.bean.vo.QuestionVo;
import com.example.studyterminalapp.bean.vo.SimpleHomeworkVo;
import com.example.studyterminalapp.bean.vo.UserAnswerAndQuestionVo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.QuestionConstant;
import com.example.studyterminalapp.utils.RequestManager;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.button.RippleView;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

// todo:直接请求所有availableQuestion(List<QuestionVo>)
public class StudentHomeworkDetail2Activity extends AppCompatActivity {
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;
    private SimpleHomeworkVo simpleHomework;
    private Homework homework;
    private TextView tvHomeworkName, tvOpenDate, tvDeadline, tvOpenDateStatus,
            tvFinishedCount, tvQuestionCount;
    private Button btnDoHomework;
    private RippleView btnQuestionFeedback;
    // 该章节下某用户需要完成的题目的id列表
    private List<QuestionVo> availableQuestions;
    private List<UserAnswerAndQuestionVo> answerAndQuestionList;
    private DateTimeFormatter df;
    private boolean isDue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_homework_detail2);

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


        tvHomeworkName = (TextView) findViewById(R.id.tv_homework_name);
        tvOpenDate = (TextView) findViewById(R.id.tv_open_date);
        tvDeadline = (TextView) findViewById(R.id.tv_deadline);
        tvOpenDateStatus = (TextView) findViewById(R.id.tv_open_date_status);
        tvFinishedCount = (TextView) findViewById(R.id.tv_finished_count);
        tvQuestionCount = (TextView) findViewById(R.id.tv_question_count);
        btnDoHomework = (Button) findViewById(R.id.btn_do_homework);
        btnDoHomework.setVisibility(View.INVISIBLE);
        btnQuestionFeedback = (RippleView) findViewById(R.id.btn_question_feedback);
        btnQuestionFeedback.setVisibility(View.INVISIBLE);
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
                Intent intent = new Intent(StudentHomeworkDetail2Activity.this, FinishQuestionActivity.class);
                intent.putExtra("questionList", (Serializable) availableQuestions);
                intent.putExtra("simpleHomework", (SimpleHomeworkVo) simpleHomework);
                startActivity(intent);
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
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            getAvailableQuestions();
        }).start();

        new Thread(()->{
            while (homework == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            getStudentAnswer();
        }).start();
    }

    private void getHomework() {
        try {
            HashMap<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("hid", simpleHomework.getHid());
            RequestManager.getInstance().GetRequest(paramsMap, Constants.HOMEWORK_SIMPLE_INFO, new RequestManager.ResultCallback() {
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

    private void getAvailableQuestions() {
        try {
            HashMap<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("uid", MyApp.getId());
            paramsMap.put("cid", homework.getChapterId());
            RequestManager.getInstance().GetRequest(paramsMap, Constants.STUDENT_AVAILABLE_QUESTION, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String c, String json) {
                    Type dataType = new TypeToken<Result<List<QuestionVo>>>(){}.getType();
                    Result<List<QuestionVo>> result = JsonParse.getInstance().getResult(json, dataType);
                    Integer code = result.getStatus();
                    switch (code) {
                        case 200:
                            if (result.getData() != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        availableQuestions = result.getData();
                                        int finished = homework.getTotalCount() - availableQuestions.size();
                                        tvFinishedCount.setText(String.valueOf(finished));
                                        if (!availableQuestions.isEmpty()) {
                                            btnDoHomework.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });
                            }
                            break;
                        case 210:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvFinishedCount.setText(String.valueOf(homework.getTotalCount()));
                                    btnDoHomework.setVisibility(View.GONE);
                                }
                            });
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

    private void getStudentAnswer() {
        try {
            HashMap<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("userId", MyApp.getId());
            paramsMap.put("chapterId", homework.getChapterId());
            RequestManager.getInstance().GetRequest(paramsMap, Constants.STUDENT_ANSWER, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String c, String json) {
                    Type dataType = new TypeToken<Result<List<UserAnswerAndQuestionVo>>>(){}.getType();
                    Result<List<UserAnswerAndQuestionVo>> result = JsonParse.getInstance().getResult(json, dataType);
                    Integer code = result.getStatus();
                    switch (code) {
                        case 200:
                            if (result.getData() != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //answerAndQuestionList = result.getData().stream().filter(i -> i.getCorrectStatus().equals(QuestionConstant.CORRECTED)).collect(Collectors.toList());
                                        answerAndQuestionList = result.getData();
                                        if (!answerAndQuestionList.isEmpty()
                                                && LocalDateTime.now().isAfter(simpleHomework.getDeadline())) {
                                            btnQuestionFeedback.setVisibility(View.VISIBLE);
                                            btnQuestionFeedback.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent intent = new Intent(StudentHomeworkDetail2Activity.this, FeedbackQuestionActivity.class);
                                                    intent.putExtra("answerAndQuestionList", (Serializable) answerAndQuestionList);
                                                    startActivity(intent);
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                            break;
                        default:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.toast("查询学生作答情况异常-1");
                                }
                            });
                            break;
                    }
                }

                @Override
                public void onError(String msg) {
                    Log.i("Intermediate", msg);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}