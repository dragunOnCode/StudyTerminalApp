package com.example.studyterminalapp.adapter.teacher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.bean.Homework;
import com.example.studyterminalapp.bean.vo.QidAndStatusVo;
import com.example.studyterminalapp.bean.vo.StudentHomeworkStatusVo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class FinishedRecyclerAdapter extends RecyclerView.Adapter<FinishedRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<StudentHomeworkStatusVo> list;
    private View inflater;
    private OnItemClickListener mOnItemClickListener;
    private DateTimeFormatter df;

    //构造方法，传入数据,即把展示的数据源传进来，并且复制给一个全局变量，以后的操作都在该数据源上进行
    public FinishedRecyclerAdapter(Context context, List<StudentHomeworkStatusVo> list) {
        this.context = context;
        this.list = list;
        df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    }

    //由于ClassRecyclerAdapter继承自RecyclerView.Adapter,则必须重写onCreateViewHolder()，onBindViewHolder()，getItemCount()
    //onCreateViewHolder()方法用于创建ViewHolder实例，我们在这个方法将item_dome.xml布局加载进来
    //然后创建一个ViewHolder实例，并把加载出来的布局传入到构造函数，最后将实例返回
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        inflater = LayoutInflater.from(context).inflate(R.layout.finished_homework_item, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(inflater);
        return myViewHolder;
    }

    //onBindViewHolder()方法用于对RecyclerView子项数据进行赋值，会在每个子项被滚动到屏幕内的时候执行
    //这里我们通过position参数的得到当前项的实例，然后将数据设置到ViewHolder的TextView即可
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //将数据和控件绑定
        StudentHomeworkStatusVo homeworkStatusVo = list.get(position);
        holder.tvNickname.setText(homeworkStatusVo.getNickname());
        holder.tvStudentNumber.setText(homeworkStatusVo.getStudentNumber());
        holder.tvQuestionFinishNumber.setText(homeworkStatusVo.getQuestionFinishNumber().toString());
        holder.tvQuestionTotalNumber.setText(homeworkStatusVo.getQuestionTotalNumber().toString());

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
        TextView tvNickname, tvStudentNumber, tvQuestionFinishNumber, tvQuestionTotalNumber;

        public MyViewHolder(View itemView) {//这个view参数就是recyclerview子项的最外层布局
            super(itemView);
            //可以通过findViewById方法获取布局中的TextView
            tvNickname = (TextView) itemView.findViewById(R.id.tv_nickname);
            tvStudentNumber = itemView.findViewById(R.id.tv_student_number);
            tvQuestionFinishNumber = itemView.findViewById(R.id.tv_question_finish_number);
            tvQuestionTotalNumber = itemView.findViewById(R.id.tv_question_total_number);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, tvNickname.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
