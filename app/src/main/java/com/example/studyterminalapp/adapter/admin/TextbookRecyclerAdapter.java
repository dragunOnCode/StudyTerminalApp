package com.example.studyterminalapp.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.bean.Textbook;

import java.util.List;

public class TextbookRecyclerAdapter extends RecyclerView.Adapter<TextbookRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<Textbook> list;
    private View inflater;
    private OnItemClickListener mOnItemClickListener;

    //构造方法，传入数据,即把展示的数据源传进来，并且复制给一个全局变量，以后的操作都在该数据源上进行
    public TextbookRecyclerAdapter(Context context, List<Textbook> list){
        this.context = context;
        this.list = list;
    }
    //由于TextbookRecyclerAdapter继承自RecyclerView.Adapter,则必须重写onCreateViewHolder()，onBindViewHolder()，getItemCount()
    //onCreateViewHolder()方法用于创建ViewHolder实例，我们在这个方法将item_dome.xml布局加载进来
    //然后创建一个ViewHolder实例，并把加载出来的布局传入到构造函数，最后将实例返回
    @Override
    public TextbookRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建ViewHolder，返回每一项的布局
        inflater = LayoutInflater.from(context).inflate(R.layout.textbook_item,parent,false);
        TextbookRecyclerAdapter.MyViewHolder myViewHolder = new TextbookRecyclerAdapter.MyViewHolder(inflater);
        return myViewHolder;
    }

    //onBindViewHolder()方法用于对RecyclerView子项数据进行赋值，会在每个子项被滚动到屏幕内的时候执行
    //这里我们通过position参数的得到当前项的实例，然后将数据设置到ViewHolder的TextView即可
    @Override
    public void onBindViewHolder(TextbookRecyclerAdapter.MyViewHolder holder, int position) {
        //将数据和控件绑定
        Textbook textbook = list.get(position);
        holder.tvTextbookName.setText(textbook.getTextbookName());
        holder.tvTextbookAuthor.setText(textbook.getTextbookAuthor());
        holder.tvPressName.setText(textbook.getPressName());
        holder.tvCourseName.setText(textbook.getCourseName());
        holder.tvGrade.setText(textbook.getGrade());
        Glide.with(context)
                .load(textbook.getTextbookPic())
                .error(R.mipmap.ic_launcher)
                .into(holder.ivTextbookPic);
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
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvTextbookName, tvTextbookAuthor, tvCourseName, tvPressName, tvGrade;
        ImageView ivTextbookPic;
        public MyViewHolder(View itemView) {//这个view参数就是recyclerview子项的最外层布局
            super(itemView);
            //可以通过findViewById方法获取布局中的TextView
            tvTextbookName = (TextView) itemView.findViewById(R.id.tv_textbook_name);
            tvTextbookAuthor = itemView.findViewById(R.id.tv_textbook_author);
            tvPressName = itemView.findViewById(R.id.tv_press_name);
            tvCourseName = itemView.findViewById(R.id.tv_course_name);
            tvGrade = itemView.findViewById(R.id.tv_grade);
            ivTextbookPic = itemView.findViewById(R.id.iv_textbook_pic);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, String.valueOf(R.id.tv_textbook_name), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
