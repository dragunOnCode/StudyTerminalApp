package com.example.studyterminalapp.activity.student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.adapter.student.FeedbackQuestionListAdapter;
import com.example.studyterminalapp.adapter.student.StudentHomeAdapter;
import com.example.studyterminalapp.bean.vo.QuestionVo;
import com.example.studyterminalapp.bean.vo.UserAnswerAndQuestionVo;
import com.example.studyterminalapp.views.ClassListView;
import com.example.studyterminalapp.views.FeedbackQuestionListView;

import java.util.List;

public class FeedbackQuestionActivity extends AppCompatActivity {
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;
    private int uid;
    private FeedbackQuestionListView fqlvList;
    private FeedbackQuestionListAdapter adapter;
    private List<UserAnswerAndQuestionVo> answerAndQuestionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_question);

        uid = MyApp.getId();
        answerAndQuestionList = (List<UserAnswerAndQuestionVo>) getIntent().getSerializableExtra("answerAndQuestionList");

        initView();
        initData();
    }

    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);

        tvTitle.setText("答题中");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        fqlvList = findViewById(R.id.fqlv_list);

        adapter = new FeedbackQuestionListAdapter(this);
        fqlvList.setAdapter(adapter);
    }


    private void initData() {
        if (answerAndQuestionList == null || answerAndQuestionList.isEmpty()) {
            return;
        }
        adapter.setData(answerAndQuestionList);
    }
}