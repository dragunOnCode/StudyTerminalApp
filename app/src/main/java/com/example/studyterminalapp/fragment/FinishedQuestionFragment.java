package com.example.studyterminalapp.fragment;

import static com.xuexiang.xutil.XUtil.runOnUiThread;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.adapter.student.QuestionChoicesAdapter;
import com.example.studyterminalapp.bean.Answer;
import com.example.studyterminalapp.bean.Correct;
import com.example.studyterminalapp.bean.vo.QuestionVo;
import com.example.studyterminalapp.bean.vo.UserAnswerAndQuestionVo;
import com.example.studyterminalapp.utils.QuestionConstant;
import com.example.studyterminalapp.views.ChoicesListView;
import com.github.gzuliyujiang.filepicker.ExplorerConfig;
import com.github.gzuliyujiang.filepicker.FilePicker;
import com.github.gzuliyujiang.filepicker.annotation.ExplorerMode;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.edittext.ValidatorEditText;
import com.xuexiang.xutil.tip.ToastUtils;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FinishedQuestionFragment extends Fragment{
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "img";

    private ImageView ivQuestion, ivCalculation;
    private TextView tvQuestionType, tvQuestionContent, tvQuestionScore, tvBlank,
            tvChoices, tvQuestionSolution;
    private LinearLayout llAnswerInfo, llChoices, llBlank, llCalculation, llCorrect;
    private MultiLineEditText etComment;
    private ValidatorEditText etCorrectScore;
    private Button btnRecord;
    private ChoicesListView lvChoices;
    private ArrayList<String> choicesList;
    private int idx;
    private UserAnswerAndQuestionVo answerAndQuestion;
    private QuestionChoicesAdapter questionChoicesAdapter;
    private int userId;
    private Context context;
    private HashMap<Integer, Correct> correctMap;

    private String title;
    private int img;


    public static FinishedQuestionFragment newInstance(String title, int img, Context context, int idx, int userId,
                                                       HashMap<Integer, Correct> answerMap, UserAnswerAndQuestionVo vo) {
        FinishedQuestionFragment fragment = new FinishedQuestionFragment(context, idx, userId, answerMap, vo);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putInt(ARG_PARAM2,img);
        fragment.setArguments(args);
        return fragment;
    }

    public FinishedQuestionFragment() {
    }

    public FinishedQuestionFragment(Context context, int idx, int userId, HashMap<Integer, Correct> correctMap, UserAnswerAndQuestionVo answerAndQuestionVo) {
        this.context = context;
        this.correctMap = correctMap;
        this.answerAndQuestion = answerAndQuestionVo;
        this.idx = idx;
        this.userId = userId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            img = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finished_question, container, false);
        Bundle argument = getArguments();
        //ImageView iv = view.findViewById(R.id.iv);
        //iv.setImageResource(argument.getInt(ARG_PARAM2,R.drawable.ji1));
        initView(view);
        runOnUiThread(()->{
            initData();
        });
        initListener();
        return view;
    }

    private void initView(View view) {
        choicesList = new ArrayList<>();

        ivQuestion = (ImageView) view.findViewById(R.id.iv_question);
        ivCalculation = (ImageView) view.findViewById(R.id.iv_calculation);
        tvQuestionType = (TextView) view.findViewById(R.id.tv_question_type);
        tvQuestionContent = (TextView) view.findViewById(R.id.tv_question_content);
        tvQuestionScore = (TextView) view.findViewById(R.id.tv_question_score);
        tvQuestionSolution = (TextView) view.findViewById(R.id.tv_question_solution);
        tvChoices = (TextView) view.findViewById(R.id.tv_choices);
        llAnswerInfo = (LinearLayout) view.findViewById(R.id.ll_answer_info);
        llChoices = (LinearLayout) view.findViewById(R.id.ll_choices);
        llBlank = (LinearLayout) view.findViewById(R.id.ll_blank);
        llCalculation = (LinearLayout) view.findViewById(R.id.ll_calculation);
        llCorrect = (LinearLayout) view.findViewById(R.id.ll_correct);
        lvChoices = (ChoicesListView) view.findViewById(R.id.lv_choices);
        tvBlank = (TextView) view.findViewById(R.id.tv_blank);
        etCorrectScore = (ValidatorEditText) view.findViewById(R.id.et_correct_score);
        etComment = (MultiLineEditText) view.findViewById(R.id.et_comment);
        btnRecord = (Button) view.findViewById(R.id.btn_record);
    }

    private void initListener() {
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                correctMap.put(idx, getCorrectContent());
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
        etCorrectScore.setText(String.valueOf(answerAndQuestion.getUserScore()));
        etComment.setContentText(answerAndQuestion.getComment());
    }

    private Correct getCorrectContent() {
        if (answerAndQuestion == null) {
            return null;
        }
        if (etCorrectScore.isEmpty() || !etCorrectScore.validate() ||
                Integer.parseInt(etCorrectScore.getText().toString()) > answerAndQuestion.getQuestionScore()) {
            ToastUtils.toast("请输入正确的分值");
        }
        Correct correct = new Correct();
        correct.setQid(answerAndQuestion.getQid());
        correct.setUid(userId);
        correct.setScore(Integer.parseInt(etCorrectScore.getText().toString()));
        if (etComment.isNotEmpty()) {
            correct.setComment(etComment.getContentText());
        }
        return correct;
    }

    public void setCorrectContent(Correct correct) {
        if (correct == null) {
            return;
        }
        etCorrectScore.setText(correct.getScore().toString());
        etComment.setContentText(correct.getComment());
    }

}