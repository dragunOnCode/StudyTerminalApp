package com.example.studyterminalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.studyterminalapp.R;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout rlTitleBar;
    private ImageView ivBack;
    private EditText etSearchContent;
    private MaterialSpinner msSearchType;
    private RoundButton btnSearchContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //findViewById(R.id.).setOnClickListener(this);
        initView();
    }

    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));

        ivBack = findViewById(R.id.iv_back);
        ivBack.setVisibility(View.INVISIBLE);

        msSearchType = findViewById(R.id.ms_search_type);
        etSearchContent = findViewById(R.id.et_search_content);
        btnSearchContent = findViewById(R.id.btn_search_content);

        btnSearchContent.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_search_content:
                String content = etSearchContent.getText().toString();
                String type = msSearchType.getText().toString();
                Toast.makeText(this, "搜索了 "+type+","+content, Toast.LENGTH_SHORT).show();
                break;
        }
//        new MaterialDialog.Builder(this)
//                .iconRes(R.drawable.iv_back)
//                .title("什么")
//                .content("你说什么")
//                .positiveText("好")
//                .show();
    }
}