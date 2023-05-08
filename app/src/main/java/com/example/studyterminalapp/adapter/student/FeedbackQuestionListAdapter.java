package com.example.studyterminalapp.adapter.student;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.student.ClassDetailActivity;
import com.example.studyterminalapp.activity.student.FeedbackQuestionDetailActivity;
import com.example.studyterminalapp.bean.HomeClassBean;
import com.example.studyterminalapp.bean.vo.UserAnswerAndQuestionVo;

import java.util.ArrayList;
import java.util.List;

public class FeedbackQuestionListAdapter extends BaseAdapter {
    Context context;
    List<UserAnswerAndQuestionVo> data;

    public FeedbackQuestionListAdapter() {
    }

    public FeedbackQuestionListAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void setData(List<UserAnswerAndQuestionVo> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int i) {
        return data == null ? null : data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.feedback_question_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvQuestionContent= view.findViewById(R.id.tv_question_content);
            viewHolder.tvQuestionType  = view.findViewById(R.id.tv_question_type);
            viewHolder.tvQuestionDifficulty  = view.findViewById(R.id.tv_question_difficulty);
            viewHolder.tvStudentScore = view.findViewById(R.id.tv_student_score);
            viewHolder.tvQuestionScore  = view.findViewById(R.id.tv_question_score);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        UserAnswerAndQuestionVo answerAndQuestion = (UserAnswerAndQuestionVo) getItem(i);
        if (answerAndQuestion != null) {
            viewHolder.tvQuestionContent.setText(answerAndQuestion.getQuestionContent());
            viewHolder.tvQuestionType.setText(answerAndQuestion.getQuestionType());
            viewHolder.tvQuestionDifficulty.setText(answerAndQuestion.getQuestionDifficulty());
            viewHolder.tvStudentScore.setText(String.valueOf(answerAndQuestion.getUserScore()));
            viewHolder.tvQuestionScore.setText(String.valueOf(answerAndQuestion.getQuestionScore()));
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo 跳转要换
                Intent intent = new Intent(context, FeedbackQuestionDetailActivity.class);
                intent.putExtra("answerAndQuestion", answerAndQuestion);
                context.startActivity(intent);
            }
        });
        return view;
    }

    class ViewHolder{
        public TextView tvQuestionContent, tvQuestionType, tvQuestionDifficulty, tvStudentScore, tvQuestionScore;
    }
}
