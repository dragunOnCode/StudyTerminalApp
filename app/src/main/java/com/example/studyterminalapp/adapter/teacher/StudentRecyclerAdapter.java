package com.example.studyterminalapp.adapter.teacher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.bean.StudentBean;
import com.xuexiang.xui.widget.button.SmoothCheckBox;

import java.util.HashMap;
import java.util.List;

public class StudentRecyclerAdapter extends RecyclerView.Adapter<StudentRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<StudentBean> list;
    private HashMap<Integer, Integer> checkedMap;
    private View inflater;
    private OnItemClickListener mOnItemClickListener;

    //构造方法，传入数据,即把展示的数据源传进来，并且复制给一个全局变量，以后的操作都在该数据源上进行
    public StudentRecyclerAdapter(Context context, List<StudentBean> list) {
        this.context = context;
        this.list = list;
        checkedMap = new HashMap<>();
    }

    public HashMap<Integer, Integer> getCheckedMap() {
        return checkedMap;
    }

    //由于ClassRecyclerAdapter继承自RecyclerView.Adapter,则必须重写onCreateViewHolder()，onBindViewHolder()，getItemCount()
    //onCreateViewHolder()方法用于创建ViewHolder实例，我们在这个方法将item_dome.xml布局加载进来
    //然后创建一个ViewHolder实例，并把加载出来的布局传入到构造函数，最后将实例返回
    @Override
    public StudentRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        inflater = LayoutInflater.from(context).inflate(R.layout.class_student_item, parent, false);
        StudentRecyclerAdapter.MyViewHolder myViewHolder = new StudentRecyclerAdapter.MyViewHolder(inflater);
        return myViewHolder;
    }

    //onBindViewHolder()方法用于对RecyclerView子项数据进行赋值，会在每个子项被滚动到屏幕内的时候执行
    //这里我们通过position参数的得到当前项的实例，然后将数据设置到ViewHolder的TextView即可
    @Override
    public void onBindViewHolder(StudentRecyclerAdapter.MyViewHolder holder, int position) {
        //将数据和控件绑定
        StudentBean student = list.get(position);
        holder.tvStudentName.setText(student.getNickname());
        holder.tvUsername.setText(student.getUsername());
        holder.tvStudentNumber.setText(student.getStudentNumber());
        holder.tvGender.setText(student.getGender());
        int pos = position;
        holder.scbCheck.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                if (isChecked) {
                    checkedMap.put(student.getUid(), pos);
                } else {
                    checkedMap.remove(student.getUid());
                }
            }
        });
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, pos);
                }
            });
        }
    }

    //getItemCount()告诉RecyclerView一共有多少个子项，直接返回数据源的长度。
    @Override
    public int getItemCount() {
        //返回Item总条数
        return list.size();
    }

    public Object getItem(int i) {
        return list == null ? null : list.get(i);
    }

    //内部类，绑定控件
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName;
        TextView tvUsername;
        TextView tvStudentNumber;
        TextView tvGender;
        SmoothCheckBox scbCheck;

        public MyViewHolder(View itemView) {//这个view参数就是recyclerview子项的最外层布局
            super(itemView);
            //可以通过findViewById方法获取布局中的TextView
            tvStudentName = (TextView) itemView.findViewById(R.id.tv_student_name);
            tvUsername = itemView.findViewById(R.id.tv_username);
            tvStudentNumber = itemView.findViewById(R.id.tv_student_number);
            tvGender = itemView.findViewById(R.id.tv_gender);
            scbCheck = itemView.findViewById(R.id.scb_check);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(context, tvStudentName.getText(), Toast.LENGTH_SHORT).show();
//                }
//            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
