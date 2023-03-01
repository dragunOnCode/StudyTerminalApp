package com.example.studyterminalapp.adapter.student;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.student.StudentOrderDetailActivity;
import com.example.studyterminalapp.bean.OrderBean;

import java.util.ArrayList;
import java.util.List;

public class StudentOrderAdapter extends BaseAdapter {
    Context context;
    List<OrderBean> data;

    public StudentOrderAdapter() {
    }

    public StudentOrderAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void setData(List<OrderBean> data) {
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.order_item, null);
            vh = new ViewHolder();
            vh.tvOrderName = convertView.findViewById(R.id.tv_order_name);
            vh.tvOrderStatus = convertView.findViewById(R.id.tv_order_status);
            vh.tvOrderDate = convertView.findViewById(R.id.tv_order_date);
            vh.tvOrderPrice = convertView.findViewById(R.id.tv_order_price);
//            vh.btnFeedbackOrder = convertView.findViewById(R.id.btn_feedback_order);
//            vh.btnDeleteOrder = convertView.findViewById(R.id.btn_delete_order);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        // 获取i对应的订单列表
        OrderBean orderBean = (OrderBean) getItem(i);
        if (orderBean != null) {
            vh.tvOrderName.setText(orderBean.getOrderName());
            String status = "-";
            switch (orderBean.getOrderStatus()) {
                case 0:
                    status = "已支付";
                    break;
                case 1:
                    status = "已完成";
                    break;
            }
            vh.tvOrderStatus.setText(status);
            vh.tvOrderDate.setText(orderBean.getOrderDate());
            vh.tvOrderPrice.setText("￥ " + orderBean.getOrderPrice());


        }
        // 设置点击事件
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderBean == null)return;
                Intent intent = new Intent(context, StudentOrderDetailActivity.class);
                //把订单的详细信息传递到订单详情界面
                intent.putExtra("order", orderBean);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    class ViewHolder{
        public TextView tvOrderName, tvOrderStatus, tvOrderDate, tvOrderPrice;
        //public Button btnFeedbackOrder, btnDeleteOrder;
    }
}
