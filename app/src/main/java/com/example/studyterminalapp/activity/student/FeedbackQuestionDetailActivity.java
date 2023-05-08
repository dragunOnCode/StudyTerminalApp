package com.example.studyterminalapp.activity.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.adapter.student.QuestionChoicesAdapter;
import com.example.studyterminalapp.bean.Answer;
import com.example.studyterminalapp.bean.Correct;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.vo.UserAnswerAndQuestionVo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.QuestionConstant;
import com.example.studyterminalapp.utils.RequestManager;
import com.example.studyterminalapp.views.ChoicesListView;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.utils.ResUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.GravityEnum;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.edittext.ValidatorEditText;
import com.xuexiang.xui.widget.picker.widget.OptionsPickerView;
import com.xuexiang.xui.widget.picker.widget.builder.OptionsPickerBuilder;
import com.xuexiang.xui.widget.picker.widget.configure.PickerOptions;
import com.xuexiang.xui.widget.picker.widget.listener.OnOptionsSelectListener;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.rxjava3.functions.Consumer;
import rxhttp.wrapper.entity.Progress;
import rxhttp.wrapper.param.RxHttp;

public class FeedbackQuestionDetailActivity extends AppCompatActivity {
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;
    private ImageView ivQuestion, ivCalculation;
    private TextView tvQuestionType, tvQuestionContent, tvQuestionScore, tvBlank,
            tvChoices, tvQuestionSolution, tvComment, tvCorrectScore, tvIncorrectReason;
    private RadioGroup rgReason;
    private LinearLayout llAnswerInfo, llChoices, llBlank, llCalculation, llCorrect, llIncorrect, llComment;
    private Button btnCommitReason;
    private ChoicesListView lvChoices;
    private ArrayList<String> choicesList;
    private int idx;
    private UserAnswerAndQuestionVo answerAndQuestion;
    private QuestionChoicesAdapter questionChoicesAdapter;
    private int userId;
    private Context context;
    private String[] incorrectReasonOption;
    private int incorrectReasonIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_question_detail);
        
        userId = MyApp.getId();
        answerAndQuestion = (UserAnswerAndQuestionVo) getIntent().getSerializableExtra("answerAndQuestion");
        context = this;
        
        initView();
        initListener();
        initData();
    }

    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);

        tvTitle.setText("作答详情");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        choicesList = new ArrayList<>();

        ivQuestion = (ImageView) findViewById(R.id.iv_question);
        ivCalculation = (ImageView) findViewById(R.id.iv_calculation);
        tvQuestionType = (TextView) findViewById(R.id.tv_question_type);
        tvQuestionContent = (TextView) findViewById(R.id.tv_question_content);
        tvQuestionScore = (TextView) findViewById(R.id.tv_question_score);
        tvQuestionSolution = (TextView) findViewById(R.id.tv_question_solution);
        tvChoices = (TextView) findViewById(R.id.tv_choices);
        llAnswerInfo = (LinearLayout) findViewById(R.id.ll_answer_info);
        llChoices = (LinearLayout) findViewById(R.id.ll_choices);
        llBlank = (LinearLayout) findViewById(R.id.ll_blank);
        llCalculation = (LinearLayout) findViewById(R.id.ll_calculation);
        llCorrect = (LinearLayout) findViewById(R.id.ll_correct);
        llIncorrect = (LinearLayout) findViewById(R.id.ll_incorrect);
        llComment = (LinearLayout) findViewById(R.id.ll_comment);
        lvChoices = (ChoicesListView) findViewById(R.id.lv_choices);
        tvBlank = (TextView) findViewById(R.id.tv_blank);
        tvCorrectScore = findViewById(R.id.tv_correct_score);
        tvComment = findViewById(R.id.tv_comment);
        tvIncorrectReason = (TextView) findViewById(R.id.tv_incorrect_reason);
        tvIncorrectReason.getPaint().setFlags( Paint.UNDERLINE_TEXT_FLAG );
        tvIncorrectReason.getPaint().setAntiAlias(true);//抗锯齿
        btnCommitReason = (Button) findViewById(R.id.btn_commit_reason);
        //rgReason = (RadioGroup) findViewById(R.id.rg_reason);

        incorrectReasonOption = ResUtils.getStringArray(context, R.array.incorrect_reason);
    }

    private void initListener() {
        tvIncorrectReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OptionsPickerView pvOptions = new OptionsPickerBuilder(context, (v, options1, options2, options3) -> {
                    tvIncorrectReason.setText(incorrectReasonOption[options1]);
                    incorrectReasonIndex = options1;
                    return false;
                })
                        .setTitleText("错因选择")
                        .setDividerColor(Color.BLACK)
                        .setTextColorCenter(R.color.price_red) //设置选中项文字颜色
                        .setContentTextSize(20)
                        .isDialog(true)
                        .setSelectOptions(incorrectReasonIndex)
                        .build();
                pvOptions.setPicker(incorrectReasonOption);//一级选择器
                pvOptions.show();
            }
        });

        btnCommitReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String, Object> paramsMap = new HashMap<>();
//                int checkedRadioButtonId = rgReason.getCheckedRadioButtonId();
//                RadioButton button = (RadioButton) findViewById(checkedRadioButtonId);
                paramsMap.put("uid", userId);
                paramsMap.put("qid", answerAndQuestion.getQid());
                paramsMap.put("incorrectReason", tvIncorrectReason.getText());
                try {
                    RequestManager.getInstance().GetRequest(paramsMap, Constants.FEEDBACK_ANSWER, new RequestManager.ResultCallback() {
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
                                            ToastUtils.toast("更新反馈成功");
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
        if (answerAndQuestion == null) {
            return;
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String questionType = "";
        switch (answerAndQuestion.getQuestionType()) {
            case QuestionConstant.CHOICES:
                questionType = "选择题";
                llBlank.setVisibility(View.GONE);
                llCalculation.setVisibility(View.GONE);
                questionChoicesAdapter = new QuestionChoicesAdapter(context);
                lvChoices.setAdapter(questionChoicesAdapter);
                choicesList = (ArrayList<String>) answerAndQuestion.getChoiceSelectionList();
                questionChoicesAdapter.setData(choicesList);
                if (answerAndQuestion.getAnswer() != null) {
                    tvChoices.setText(answerAndQuestion.getAnswer());
                }
                break;
            case QuestionConstant.BLANK:
                questionType = "填空题";
                lvChoices.setVisibility(View.GONE);
                llChoices.setVisibility(View.GONE);
                llCalculation.setVisibility(View.GONE);
                if (answerAndQuestion.getAnswer() != null) {
                    tvBlank.setText(answerAndQuestion.getAnswer());
                }
                break;
            case QuestionConstant.CALCULATION:
                questionType = "计算题";
                lvChoices.setVisibility(View.GONE);
                llBlank.setVisibility(View.GONE);
                llChoices.setVisibility(View.GONE);
                if (answerAndQuestion.getAnswerUrl() != null) {
                    Glide.with(context)
                            .load(answerAndQuestion.getAnswerUrl())
                            .error(R.mipmap.ic_launcher)
                            .into(ivCalculation);
                }
                break;
            default:
                break;
        }
        tvQuestionType.setText(questionType);
        tvQuestionScore.setText(String.valueOf(answerAndQuestion.getQuestionScore()));
        tvQuestionContent.setText(answerAndQuestion.getQuestionContent());
        tvQuestionSolution.setText(answerAndQuestion.getQuestionSolution());
        tvCorrectScore.setText(answerAndQuestion.getUserScore()+"分");
        if (answerAndQuestion.getComment() == null) {
            llComment.setVisibility(View.GONE);
        }
        tvComment.setText(answerAndQuestion.getComment());
        tvIncorrectReason.setText(answerAndQuestion.getIncorrectReason());
        if (answerAndQuestion.getQuestionScore() == answerAndQuestion.getUserScore()) {
            llIncorrect.setVisibility(View.GONE);
        } else {
            tvCorrectScore.setTextColor(getResources().getColor(R.color.price_red));
        }
    }
}