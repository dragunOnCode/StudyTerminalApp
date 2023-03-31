package com.example.studyterminalapp.adapter.teacher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.bean.Class;

import java.util.List;


public class ClassRecyclerAdapter extends RecyclerView.Adapter<ClassRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<Class> list;
    private View inflater;
    private ClassRecyclerAdapter.OnItemClickListener mOnItemClickListener;

    //构造方法，传入数据,即把展示的数据源传进来，并且复制给一个全局变量，以后的操作都在该数据源上进行
    public ClassRecyclerAdapter(Context context, List<Class> list) {
        this.context = context;
        this.list = list;
    }

    //由于ClassRecyclerAdapter继承自RecyclerView.Adapter,则必须重写onCreateViewHolder()，onBindViewHolder()，getItemCount()
    //onCreateViewHolder()方法用于创建ViewHolder实例，我们在这个方法将item_dome.xml布局加载进来
    //然后创建一个ViewHolder实例，并把加载出来的布局传入到构造函数，最后将实例返回
    @Override
    public ClassRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        inflater = LayoutInflater.from(context).inflate(R.layout.teacher_class_item, parent, false);
        ClassRecyclerAdapter.MyViewHolder myViewHolder = new ClassRecyclerAdapter.MyViewHolder(inflater);
        return myViewHolder;
    }

    //onBindViewHolder()方法用于对RecyclerView子项数据进行赋值，会在每个子项被滚动到屏幕内的时候执行
    //这里我们通过position参数的得到当前项的实例，然后将数据设置到ViewHolder的TextView即可
    @Override
    public void onBindViewHolder(ClassRecyclerAdapter.MyViewHolder holder, int position) {
        //将数据和控件绑定
        Class teacherClass = list.get(position);
        holder.tvClassName.setText(teacherClass.getClassName());
        holder.tvSchool.setText(teacherClass.getSchoolName());
        holder.tvSubject.setText(teacherClass.getCourseName());
        holder.tvStudentNum.setText(teacherClass.getStudentCount() + " 人已加入");
        holder.tvGrade.setText(teacherClass.getGrade());
        int pos = position;
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
        TextView tvClassName, tvSchool, tvSubject, tvStudentNum, tvGrade;

        public MyViewHolder(View itemView) {//这个view参数就是recyclerview子项的最外层布局
            super(itemView);
            //可以通过findViewById方法获取布局中的TextView
            tvClassName = (TextView) itemView.findViewById(R.id.tv_class_name);
            tvSchool = itemView.findViewById(R.id.tv_school);
            tvSubject = itemView.findViewById(R.id.tv_subject);
            tvStudentNum = itemView.findViewById(R.id.tv_student_num);
            tvGrade = itemView.findViewById(R.id.tv_grade);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, tvClassName.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(ClassRecyclerAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
