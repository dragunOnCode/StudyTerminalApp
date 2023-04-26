package com.example.studyterminalapp.adapter.student;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.studyterminalapp.R;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class QuestionChoicesAdapter extends BaseAdapter {
    Context context;
    private int selectPosition = -1;//用于记录用户选择的变量
    List<String> choicesList;

    public QuestionChoicesAdapter() {
    }

    public QuestionChoicesAdapter(Context context){
        this.context = context;
        this.choicesList = new ArrayList<>();
    }

    public void setData(List<String> data) {
        this.choicesList.clear();
        this.choicesList.addAll(data);
        notifyDataSetChanged();
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    @Override
    public int getCount() {
        return choicesList == null ? 0 : choicesList.size();
    }

    @Override
    public Object getItem(int i) {
        return choicesList == null ? null : choicesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.choice_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvSeq = (TextView)convertView.findViewById(R.id.tv_seq);
            viewHolder.tvChoiceContent = (TextView)convertView.findViewById(R.id.tv_choice_content);
            viewHolder.rbSelect = (RadioButton)convertView.findViewById(R.id.rb_select);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }
        viewHolder.tvSeq.setText(((char) ('A' + position)) + "");
        viewHolder.tvChoiceContent.setText(choicesList.get(position));
        if(selectPosition == position){
            viewHolder.rbSelect.setChecked(true);
        }
        else{
            viewHolder.rbSelect.setChecked(false);
        }
        return convertView;
    }

    class ViewHolder{
        TextView tvSeq, tvChoiceContent;
        RadioButton rbSelect;
    }
}
