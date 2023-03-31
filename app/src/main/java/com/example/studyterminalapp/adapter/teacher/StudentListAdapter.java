package com.example.studyterminalapp.adapter.teacher;

import static com.xuexiang.xutil.XUtil.runOnUiThread;

import android.content.Context;
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
import com.example.studyterminalapp.bean.StudentBean;
import com.xuexiang.xui.widget.button.SmoothCheckBox;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentListAdapter extends BaseAdapter {
    private Context context;
    List<StudentBean> data;
    HashMap<Integer, Integer> checkedMap;

    public StudentListAdapter() {
    }

    public StudentListAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
        checkedMap = new HashMap<>();
    }

    public void setData(List<StudentBean> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public HashMap<Integer, Integer> getCheckedMap() {
        return checkedMap;
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
        StudentListAdapter.ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.class_student_item, null);
            viewHolder = new StudentListAdapter.ViewHolder();
            viewHolder.tvStudentName = view.findViewById(R.id.tv_student_name);
            viewHolder.tvUsername = view.findViewById(R.id.tv_username);
            viewHolder.tvStudentNumber = view.findViewById(R.id.tv_student_number);
            viewHolder.tvGender = view.findViewById(R.id.tv_gender);
            viewHolder.scbCheck = view.findViewById(R.id.scb_check);

            view.setTag(viewHolder);
        } else {
            viewHolder = (StudentListAdapter.ViewHolder) view.getTag();
        }
        StudentBean student = (StudentBean) getItem(i);
        if (student != null) {
            viewHolder.tvStudentName.setText(student.getNickname());
            viewHolder.tvUsername.setText(student.getUsername());
            viewHolder.tvStudentNumber.setText(student.getStudentNumber());
            viewHolder.tvGender.setText(student.getGender());
            viewHolder.scbCheck.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                    if (isChecked) {
                        checkedMap.put(student.getUid(), i);
                    } else {
                        checkedMap.remove(student.getUid());
                    }
                }
            });
        }
        // 可以用来显示更详细的学生信息
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {}
//        });
        return view;
    }


    class ViewHolder{
        public TextView tvStudentName, tvUsername, tvStudentNumber, tvGender;
        public SmoothCheckBox scbCheck;
    }
}
