package com.example.studyterminalapp.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.bean.ChapterBean;

import java.util.ArrayList;
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
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, chapterBean.getChapterName(), Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(context, ClassDetailActivity.class);
//                intent.putExtra("chapter", chapterBean);
//                context.startActivity(intent);
            }
        });
        return view;
    }

    class ViewHolder{
        public TextView tvChapterName, tvChapterNum;
    }
}
