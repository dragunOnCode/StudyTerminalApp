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
import com.example.studyterminalapp.bean.HomeClassBean;

import java.util.ArrayList;
import java.util.List;

public class StudentHomeAdapter extends BaseAdapter {
    Context context;
    List<HomeClassBean> data;

    public StudentHomeAdapter() {
    }

    public StudentHomeAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void setData(List<HomeClassBean> data) {
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
            view = LayoutInflater.from(context).inflate(R.layout.home_class_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvClassName= view.findViewById(R.id.tv_class_name);
            viewHolder.tvSchool  = view.findViewById(R.id.tv_school);
            viewHolder.tvGrade  = view.findViewById(R.id.tv_grade);
            viewHolder.tvSubject = view.findViewById(R.id.tv_subject);
            viewHolder.tvStudentNum  = view.findViewById(R.id.tv_student_num);
            viewHolder.ivClassPic  = view.findViewById(R.id.iv_class_pic);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        HomeClassBean homeClassBean = (HomeClassBean) getItem(i);
        if (homeClassBean != null) {
            Log.i("HomeClassBean", homeClassBean.toString());
            viewHolder.tvClassName.setText(homeClassBean.getClassName());
            viewHolder.tvSubject.setText(homeClassBean.getCourseName());
            viewHolder.tvGrade.setText(homeClassBean.getGrade());
            viewHolder.tvSchool.setText(homeClassBean.getSchoolName());
            viewHolder.tvStudentNum.setText(homeClassBean.getOwner() + " 是任课老师");
            Glide.with(context)
                    .load(homeClassBean.getClassPic())
                    .error(R.mipmap.ic_launcher)
                    .into(viewHolder.ivClassPic);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, homeClassBean.getShopName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, ClassDetailActivity.class);
                intent.putExtra("class", homeClassBean);
                context.startActivity(intent);
            }
        });
        return view;
    }

    class ViewHolder{
        public TextView tvClassName, tvSchool, tvSubject, tvGrade, tvStudentNum;
        public ImageView ivClassPic;
    }
}
