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
import com.example.studyterminalapp.activity.admin.AdminTextbookActivity;
import com.example.studyterminalapp.activity.admin.ChapterDetailActivity;
import com.example.studyterminalapp.activity.admin.TextbookDetailActivity;
import com.example.studyterminalapp.activity.teacher.TeacherClassDetailActivity;
import com.example.studyterminalapp.bean.ChapterBean;
import com.example.studyterminalapp.bean.Result;
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

public class ChapterListAdapter extends BaseAdapter {
    private Context context;
    List<ChapterBean> data;

    public ChapterListAdapter() {
    }

    public ChapterListAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void setData(List<ChapterBean> data) {
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
        ChapterListAdapter.ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.chapter_item, null);
            viewHolder = new ChapterListAdapter.ViewHolder();
            viewHolder.tvChapterName = view.findViewById(R.id.tv_chapter_name);
            viewHolder.tvChapterNum = view.findViewById(R.id.tv_chapter_num);
            viewHolder.btnDeleteChapter = view.findViewById(R.id.btn_delete_chapter);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ChapterListAdapter.ViewHolder) view.getTag();
        }
        ChapterBean chapterBean = (ChapterBean) getItem(i);
        if (chapterBean != null) {
            viewHolder.tvChapterName.setText(chapterBean.getChapterName());
            viewHolder.tvChapterNum.setText(String.valueOf(chapterBean.getChapterNum()));
            if (chapterBean.getIsChild() != null && chapterBean.getIsChild() == 1) {
                viewHolder.tvChapterNum.setText(chapterBean.getChapterNum() + "." + chapterBean.getChildChapter());
            }
            viewHolder.btnDeleteChapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Log.d("Remove Student", studentListAdapter.getCheckedMap().keySet().toString());
                    new MaterialDialog.Builder(context)
                            .content("是否要删除该章节及其下所有章节？")
                            .positiveText("是")
                            .negativeText("否")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    HashMap<String, Object> paramsMap = new HashMap<>();
                                    paramsMap.put("cid", chapterBean.getCid());
                                    paramsMap.put("chapterNum", chapterBean.getChapterNum());
                                    paramsMap.put("textbookId", chapterBean.getTextbookId());
                                    paramsMap.put("isChild", chapterBean.getIsChild());
                                    paramsMap.put("childChapter", chapterBean.getChildChapter());
                                    try {
                                        RequestManager.getInstance().PostRequest(paramsMap, Constants.DELETE_CHAPTER,
                                                new RequestManager.ResultCallback() {
                                                    @Override
                                                    public void onResponse(String responseCode, String response) {
                                                        Type dataType = new TypeToken<Result>(){}.getType();
                                                        Result result = JsonParse.getInstance().getResult(response, dataType);
                                                        runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(context,
                                                                        result.getMsg(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onError(String msg) {
                                                        Log.i("Delete Chapter", msg);
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
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(context, chapterBean.getChapterName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ChapterDetailActivity.class);
                intent.putExtra("chapter", chapterBean);
                context.startActivity(intent);
            }
        });
        return view;
    }

    class ViewHolder{
        public TextView tvChapterName, tvChapterNum;
        public Button btnDeleteChapter;
    }
}
