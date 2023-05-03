package com.example.studyterminalapp.activity.teacher;

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
import com.example.studyterminalapp.bean.vo.QuestionVo;
import com.example.studyterminalapp.bean.vo.StudentHomeworkStatusVo;
import com.example.studyterminalapp.bean.vo.UserAnswerAndQuestionVo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.QuestionConstant;
import com.example.studyterminalapp.utils.RequestManager;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class IntermediateCorrectActivity extends AppCompatActivity {

    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;
    private StudentHomeworkStatusVo homeworkStatus;
    private Homework homework;
    private TextView tvHomeworkName, tvNickname, tvStudentNumber, tvOpenDate, tvDeadline, tvOpenDateStatus,
            tvFinishedCount, tvQuestionCount, tvCorrectCount;
    private Button btnDoCorrect;
    private List<UserAnswerAndQuestionVo> answerAndQuestionList;
    private DateTimeFormatter df;
    private boolean isDue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermediate_correct);


        homeworkStatus = (StudentHomeworkStatusVo) getIntent().getSerializableExtra("homeworkStatus");
        homework = (Homework) getIntent().getSerializableExtra("homework");

        initView();
        initListener();
        initData();
    }


    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);
        tvTitle.setText("批改详情");
        df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


        tvHomeworkName = (TextView) findViewById(R.id.tv_homework_name);
        tvNickname = (TextView) findViewById(R.id.tv_nickname);
        tvStudentNumber = (TextView) findViewById(R.id.tv_student_number);
        tvOpenDate = (TextView) findViewById(R.id.tv_open_date);
        tvDeadline = (TextView) findViewById(R.id.tv_deadline);
        tvOpenDateStatus = (TextView) findViewById(R.id.tv_open_date_status);
        tvFinishedCount = (TextView) findViewById(R.id.tv_finished_count);
        tvQuestionCount = (TextView) findViewById(R.id.tv_question_count);
        tvCorrectCount = (TextView) findViewById(R.id.tv_correct_count);
        btnDoCorrect = (Button) findViewById(R.id.btn_do_correct);
        btnDoCorrect.setVisibility(View.INVISIBLE);
    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnDoCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntermediateCorrectActivity.this, CorrectHomeworkActivity.class);
                intent.putExtra("answerAndQuestionList", (Serializable) answerAndQuestionList);
                intent.putExtra("userId", homeworkStatus.getUid());
                startActivity(intent);
            }
        });
    }

    private void initData() {
        tvNickname.setText(homeworkStatus.getNickname());
        tvStudentNumber.setText(homeworkStatus.getStudentNumber());
        tvHomeworkName.setText(homework.getHomeworkName());
        tvQuestionCount.setText(String.valueOf(homework.getTotalCount()));
        tvOpenDate.setText(df.format(homework.getOpenDate()));
        tvDeadline.setText(df.format(homework.getDeadline()));
        String openDateStatus;
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(homework.getOpenDate())) {
            openDateStatus = "未开放";
            isDue = true;
        } else if (now.isAfter(homework.getDeadline())) {
            openDateStatus = "已截止";
            isDue = true;
        } else {
            openDateStatus = "进行中";
            isDue = false;
        }
        tvOpenDateStatus.setText(openDateStatus);

        // 获取作业
        getStudentAnswer();
    }


    private void getStudentAnswer() {
        try {
            HashMap<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("userId", homeworkStatus.getUid());
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
                                        answerAndQuestionList = result.getData();
                                        List<UserAnswerAndQuestionVo> answered = answerAndQuestionList.stream().filter(i -> !i.getFinishedStatus().equals(QuestionConstant.NO_ANSWER)).collect(Collectors.toList());
                                        int finished = answered.size();
                                        tvFinishedCount.setText(String.valueOf(finished));
                                        long corrected = answerAndQuestionList.stream().filter(i -> i.getCorrectStatus().equals(QuestionConstant.CORRECTED)).count();
                                        tvCorrectCount.setText(String.valueOf(corrected));
                                        if (!answerAndQuestionList.isEmpty()) {
                                            btnDoCorrect.setVisibility(View.VISIBLE);
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