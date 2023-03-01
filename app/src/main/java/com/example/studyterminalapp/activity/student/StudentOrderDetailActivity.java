package com.example.studyterminalapp.activity.student;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.bean.OrderBean;
import com.example.studyterminalapp.bean.StudentBean;
import com.xuexiang.xui.widget.textview.MarqueeTextView;
import com.xuexiang.xui.widget.textview.marqueen.DisplayEntity;

import java.util.List;

public class StudentOrderDetailActivity extends AppCompatActivity {

    private OrderBean order;
    private TextView tvOrderDetailReceiver, tvOrderDetailTelephone,
            tvOrderDetailAddress, tvOrderDetailSeq, tvOrderDetailStatus, tvOrderDetailDate,
            tvOrderDetailMode, tvOrderDetailPrice;
    private Button btnDeleteOrder, btnFeedbackOrder;
    private MarqueeTextView tvOrderDetailName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_order_detail);

        order = (OrderBean) getIntent().getSerializableExtra("order");
        Log.i("Order", order.toString());
        initView();
        initData();
    }

    private void initView() {
        tvOrderDetailName = findViewById(R.id.tv_order_detail_name);
        tvOrderDetailReceiver = findViewById(R.id.tv_order_detail_receiver);
        tvOrderDetailTelephone = findViewById(R.id.tv_order_detail_telephone);
        tvOrderDetailAddress = findViewById(R.id.tv_order_detail_address);
        tvOrderDetailSeq = findViewById(R.id.tv_order_detail_seq);
        tvOrderDetailStatus = findViewById(R.id.tv_order_detail_status);
        tvOrderDetailDate = findViewById(R.id.tv_order_detail_date);
        tvOrderDetailMode = findViewById(R.id.tv_order_detail_mode);
        tvOrderDetailPrice = findViewById(R.id.tv_order_detail_price);
        btnDeleteOrder = findViewById(R.id.btn_delete_order);
        btnFeedbackOrder = findViewById(R.id.btn_feedback_order);

        tvOrderDetailName.setOnMarqueeListener(new MarqueeTextView.OnMarqueeListener() {
            @Override
            public DisplayEntity onStartMarquee(DisplayEntity displayMsg, int index) {
                if (displayMsg.toString().equals("离离原上草，一岁一枯荣。")) {
                    return null;
                } else {
                    Log.i("Roll", "开始滚动：" + displayMsg.toString());
                    return displayMsg;
                }
            }
            @Override
            public List<DisplayEntity> onMarqueeFinished(List<DisplayEntity> displayDatas) {
                Log.i("Roll", "滚动完毕");
                return displayDatas;
            }
        });

        btnFeedbackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Feedback Order", order.getOid() + " 有问题");
            }
        });
        btnDeleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Delete Order", order.getOid() + " 删除");
            }
        });
    }

    private void initData() {
        if (order == null) {
            return;
        }

        tvOrderDetailName.addDisplayString(order.getOrderName());
        tvOrderDetailName.startRoll();

        tvOrderDetailReceiver.setText(order.getContact().getReceiver());
        tvOrderDetailTelephone.setText(order.getContact().getTelephone());
        tvOrderDetailAddress.setText(order.getContact().getAddress());
        tvOrderDetailSeq.setText(order.getOrderSequence());
        String status = "-";
        switch (order.getOrderStatus()) {
            case 0:
                status = "已支付";
                break;
            case 1:
                status = "已完成";
                break;
        }
        tvOrderDetailStatus.setText(status);
        tvOrderDetailDate.setText(order.getOrderDate());
        String mode = "-";
        switch (order.getPaymentMode()) {
            case 0:
                mode = "余额支付";
                break;
            case 1:
                mode = "微信支付";
                break;
            case 2:
                mode = "支付宝支付";
                break;
            default:
                break;
        }
        tvOrderDetailMode.setText(mode);
        tvOrderDetailPrice.setText("￥"+order.getOrderPrice());
    }
}