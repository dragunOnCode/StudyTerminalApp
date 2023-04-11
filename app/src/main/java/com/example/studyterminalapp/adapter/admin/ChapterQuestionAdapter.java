package com.example.studyterminalapp.adapter.admin;

import static com.xuexiang.xutil.XUtil.runOnUiThread;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.admin.ChapterDetailActivity;
import com.example.studyterminalapp.bean.ChapterBean;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.vo.ChapterQuestionVo;
import com.example.studyterminalapp.bean.vo.QuestionVo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChapterQuestionAdapter extends BaseAdapter {
    private Context context;
    List<ChapterQuestionVo> data;

    public ChapterQuestionAdapter() {
    }

    public ChapterQuestionAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void setData(List<ChapterQuestionVo> data) {
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
            view = LayoutInflater.from(context).inflate(R.layout.chapter_question_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvQuestionContent = view.findViewById(R.id.tv_question_content);
            viewHolder.tvQuestionDifficulty = view.findViewById(R.id.tv_question_difficulty);
            viewHolder.tvQuestionType = view.findViewById(R.id.tv_question_type);
            viewHolder.tvCourseName = view.findViewById(R.id.tv_course_name);
            viewHolder.tvGrade = view.findViewById(R.id.tv_grade);
            viewHolder.btnDelete = view.findViewById(R.id.btn_delete);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ChapterQuestionVo question = (ChapterQuestionVo) getItem(i);
        if (question != null) {
            viewHolder.tvQuestionContent.setText(question.getQuestionContent());
            viewHolder.tvQuestionDifficulty.setText(question.getQuestionDifficulty());
            viewHolder.tvQuestionType.setText(question.getQuestionType());
            viewHolder.tvCourseName.setText(question.getCourseName());
            viewHolder.tvGrade.setText(question.getGrade());
            viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Log.d("Remove Student", studentListAdapter.getCheckedMap().keySet().toString());
                    new MaterialDialog.Builder(context)
                            .content("是否要删除该题目？")
                            .positiveText("是")
                            .negativeText("否")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    try {
                                        String url = Constants.CHAPTER_QUESTION + "/" + question.getId();
                                        RequestManager.getInstance().DeleteRequest(new HashMap<>(), url,
                                                new RequestManager.ResultCallback() {
                                                    @Override
                                                    public void onResponse(String responseCode, String response) {
                                                        Type dataType = new TypeToken<Result>(){}.getType();
                                                        Result result = JsonParse.getInstance().getResult(response, dataType);
                                                        switch (result.getStatus()) {
                                                            case 200:
                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                                break;
                                                            default:
                                                                runOnUiThread(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Toast.makeText(context,
                                                                                result.getMsg(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                                break;
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(String msg) {
                                                        Log.i("Delete Chapter Question", msg);
                                                    }
                                                });
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                }
                            })
                            .show();
                }
            });
        }
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Toast.makeText(context, chapterBean.getChapterName(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context, ChapterDetailActivity.class);
//                intent.putExtra("chapter", question);
//                context.startActivity(intent);
//            }
//        });
        return view;
    }

    class ViewHolder{
        public TextView tvQuestionContent, tvQuestionType, tvQuestionDifficulty, tvCourseName, tvGrade;
        public Button btnDelete;
    }
}
