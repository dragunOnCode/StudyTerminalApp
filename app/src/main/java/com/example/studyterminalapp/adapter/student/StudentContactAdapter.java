package com.example.studyterminalapp.adapter.student;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.bean.ContactBean;

import java.util.ArrayList;
import java.util.List;

public class StudentContactAdapter extends BaseAdapter {
    Context context;
    List<ContactBean> data;

    public StudentContactAdapter() {
    }

    public StudentContactAdapter(Context context) {
        this.context = context;
        data = new ArrayList<>();
    }

    public void setData(List<ContactBean> data) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_item, null);
            vh = new ViewHolder();
            vh.tvContactReceiver = convertView.findViewById(R.id.tv_contact_receiver);
            vh.tvContactTelephone = convertView.findViewById(R.id.tv_contact_telephone);
            vh.tvContactAddress = convertView.findViewById(R.id.tv_contact_address);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        // 获取i对应的订单列表
        ContactBean contactBean = (ContactBean) getItem(i);
        if (contactBean != null) {
            vh.tvContactReceiver.setText(contactBean.getReceiver());
            vh.tvContactTelephone.setText(contactBean.getTelephone());
            vh.tvContactAddress.setText(contactBean.getAddress());
        }
        // 设置点击事件
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (contactBean == null)return;
//                Intent intent = new Intent(context, StudentOrderDetailActivity.class);
//                //把订单的详细信息传递到订单详情界面
//                intent.putExtra("order", contactBean);
//                context.startActivity(intent);
//            }
//        });

        return convertView;
    }

    class ViewHolder{
        public TextView tvContactReceiver, tvContactTelephone, tvContactAddress;
        //public Button btnFeedbackOrder, btnDeleteOrder;
    }
}
