package com.example.studyterminalapp.activity.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.QuestionConstant;
import com.example.studyterminalapp.utils.RequestManager;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.button.RippleView;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AddQuestionActivity extends AppCompatActivity {
    private RelativeLayout rlTitleBar;
    private TextView tvTitle, tvChoicesSelection;
    private ImageView ivBack;
    private MaterialSpinner msCourseName, msGrade, msQuestionType, msQuestionDifficulty;
    private MultiLineEditText etQuestionContent, etChoicesSelection, etQuestionSolution, etQuestionAnalysis;
    private RippleView btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        initView();
        initListener();
    }

    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);

        tvChoicesSelection = (TextView) findViewById(R.id.tv_choices_selection);
        msCourseName = (MaterialSpinner) findViewById(R.id.ms_course_name);
        msGrade = (MaterialSpinner) findViewById(R.id.ms_grade);
        msQuestionType = (MaterialSpinner) findViewById(R.id.ms_question_type);
        msQuestionDifficulty = (MaterialSpinner) findViewById(R.id.ms_question_difficulty);
        etQuestionContent = (MultiLineEditText) findViewById(R.id.et_question_content);
        etChoicesSelection = (MultiLineEditText) findViewById(R.id.et_choices_selection);
        etQuestionSolution = (MultiLineEditText) findViewById(R.id.et_question_solution);
        etQuestionAnalysis = (MultiLineEditText) findViewById(R.id.et_question_analysis);
        btnConfirm = (RippleView) findViewById(R.id.btn_confirm);

        tvTitle.setText("新增题目");
    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        msQuestionType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                String i = view.getSelectedItem().toString();
                if (QuestionConstant.CHOICES.equals(i)) {
                    tvChoicesSelection.setVisibility(View.VISIBLE);
                    etChoicesSelection.setVisibility(View.VISIBLE);
                } else {
                    tvChoicesSelection.setVisibility(View.INVISIBLE);
                    etChoicesSelection.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etQuestionContent.isEmpty() || etQuestionSolution.isEmpty()) {
                    return;
                }
                HashMap<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("questionContent", etQuestionContent.getContentText().toString());
                paramsMap.put("questionSolution", etQuestionSolution.getContentText().toString());
                paramsMap.put("courseName", msCourseName.getText().toString());
                paramsMap.put("grade", msGrade.getText().toString());
                paramsMap.put("questionDifficulty", msQuestionDifficulty.getText().toString());
                paramsMap.put("questionType", msQuestionType.getText().toString());
                if (!etQuestionAnalysis.isEmpty()) {
                    paramsMap.put("questionAnalysis", etQuestionAnalysis.getContentText().toString());
                }
                if (QuestionConstant.CHOICES.equals(msQuestionType.getSelectedItem().toString())) {
                    String[] c = etChoicesSelection.getContentText().toString().split("\n");
                    List<String> choices= Stream.of(c).collect(Collectors.toList());
                    paramsMap.put("choiceSelectionList", choices);
                }
                try {
                    RequestManager.getInstance().PostRequest(paramsMap, Constants.QUESTION,
                            new RequestManager.ResultCallback() {
                                @Override
                                public void onResponse(String responseCode, String response) {
                                    Type dataType = new TypeToken<Result>(){}.getType();
                                    Result result = JsonParse.getInstance().getResult(response, dataType);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(AddQuestionActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    finish();
                                }

                                @Override
                                public void onError(String msg) {
                                    Log.i("Update Student Profile", msg);
                                }
                            });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}