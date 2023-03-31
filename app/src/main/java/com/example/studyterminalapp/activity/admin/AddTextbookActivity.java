package com.example.studyterminalapp.activity.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.button.RippleView;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.edittext.ValidatorEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class AddTextbookActivity extends AppCompatActivity {
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;
    private MaterialEditText etTextbookName, etTextbookAuthor, etPressName;
    private MaterialSpinner msCourseName, msGrade;
    private ValidatorEditText etTextbookPrice;
    private MultiLineEditText etTextbookDescription;
    private RippleView btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_textbook);

        initView();
        initListener();
    }

    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);

        etTextbookName = (MaterialEditText) findViewById(R.id.et_textbook_name);
        etTextbookAuthor = (MaterialEditText) findViewById(R.id.et_textbook_author);
        etPressName = (MaterialEditText) findViewById(R.id.et_press_name);
        msCourseName = (MaterialSpinner) findViewById(R.id.ms_course_name);
        msGrade = (MaterialSpinner) findViewById(R.id.ms_grade);
        etTextbookPrice = (ValidatorEditText) findViewById(R.id.et_textbook_price);
        etTextbookDescription = (MultiLineEditText) findViewById(R.id.et_textbook_description);
        btnConfirm = (RippleView) findViewById(R.id.btn_confirm);

        tvTitle.setText("新增教辅");
    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etTextbookName.isEmpty() || etTextbookAuthor.isEmpty() || etPressName.isEmpty() ||
                        etTextbookPrice.isEmpty() || !etTextbookPrice.validate()) {
                    return;
                }
                HashMap<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("textbookName", etTextbookName.getText().toString());
                paramsMap.put("courseName", msCourseName.getText().toString());
                paramsMap.put("grade", msGrade.getText().toString());
                paramsMap.put("textbookAuthor", etTextbookAuthor.getText().toString());
                paramsMap.put("pressName", etPressName.getText().toString());
                paramsMap.put("textbookPrice", etTextbookPrice.getText().toString());
                if (!etTextbookDescription.isEmpty()) {
                    paramsMap.put("textbookDescription", etTextbookDescription.getContentText());
                }
                try {
                    RequestManager.getInstance().PostRequest(paramsMap, Constants.TEXTBOOK,
                            new RequestManager.ResultCallback() {
                        @Override
                        public void onResponse(String responseCode, String response) {
                            Type dataType = new TypeToken<Result>(){}.getType();
                            Result result = JsonParse.getInstance().getResult(response, dataType);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddTextbookActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            finish();
                        }

                        @Override
                        public void onError(String msg) {
                            Log.i("Update Student Profile", msg);
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}