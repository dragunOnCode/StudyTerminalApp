package com.example.studyterminalapp.adapter.student;

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
import com.example.studyterminalapp.activity.student.ClassDetailActivity;
import com.example.studyterminalapp.activity.student.StudentHomeworkDetailActivity;
import com.example.studyterminalapp.adapter.teacher.ResourceListAdapter;
import com.example.studyterminalapp.bean.Homework;
import com.example.studyterminalapp.bean.Resource;
import com.example.studyterminalapp.bean.vo.SimpleHomeworkVo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.RequestManager;
import com.xuexiang.xui.widget.button.SmoothCheckBox;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeworkListAdapter extends BaseAdapter {
    private Context context;
    List<SimpleHomeworkVo> data;
    private DateTimeFormatter df;

    public HomeworkListAdapter() {
    }

    public HomeworkListAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
        df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    public void setData(List<SimpleHomeworkVo> data) {
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
            view = LayoutInflater.from(context).inflate(R.layout.student_homework_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvHomeworkName = view.findViewById(R.id.tv_homework_name);
            viewHolder.tvOpenDate = view.findViewById(R.id.tv_open_date);
            viewHolder.tvDeadline = view.findViewById(R.id.tv_deadline);
            viewHolder.tvOpenDateStatus = view.findViewById(R.id.tv_open_date_status);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        SimpleHomeworkVo homework = (SimpleHomeworkVo) getItem(i);
        if (homework != null) {
            viewHolder.tvHomeworkName.setText(homework.getHomeworkName());
            String openDate = df.format(homework.getOpenDate());
            viewHolder.tvOpenDate.setText(openDate.substring(0, 10));
            String deadline = df.format(homework.getDeadline());
            viewHolder.tvDeadline.setText(deadline.substring(0, 10));
            LocalDateTime now = LocalDateTime.now();
            String openDateStatus;
            if (now.isBefore(homework.getOpenDate())) {
                openDateStatus = "未开放";
            } else if (now.isAfter(homework.getDeadline())) {
                openDateStatus = "已截止";
            } else {
                openDateStatus = "进行中";
            }
            viewHolder.tvOpenDateStatus.setText(openDateStatus);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ToastUtils.toast(homework.getHomeworkName()+"被点击了");
                Intent intent = new Intent(context, StudentHomeworkDetailActivity.class);
                intent.putExtra("simpleHomework", homework);
                context.startActivity(intent);

            }
        });
        return view;
    }

    class ViewHolder{
        public TextView tvHomeworkName, tvOpenDate, tvDeadline, tvOpenDateStatus;
    }
}
