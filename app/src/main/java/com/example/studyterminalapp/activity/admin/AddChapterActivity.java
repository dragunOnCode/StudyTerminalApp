package com.example.studyterminalapp.activity.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.edittext.ValidatorEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class AddChapterActivity extends AppCompatActivity {
    private TextView tvChildChapter;
    private MaterialEditText etChapterName;
    private ValidatorEditText etChapterNum, etChildChapter;
    private MaterialSpinner msIsChild;
    private Button btnCancel, btnCommitAdd;
    private int textbookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chapter);

        Log.i("Add Chapter", "已经跳转");
        textbookId = getIntent().getIntExtra("textbookId", 0);

        initView();
        initListener();
    }

    private void initView() {
        tvChildChapter = (TextView) findViewById(R.id.tv_child_chapter);
        etChapterName = (MaterialEditText) findViewById(R.id.et_chapter_name);
        etChapterNum = findViewById(R.id.et_chapter_num);
        etChildChapter = findViewById(R.id.et_child_chapter);
        msIsChild = (MaterialSpinner) findViewById(R.id.ms_is_child);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnCommitAdd = (Button) findViewById(R.id.btn_commit_add);

    }

    private void initListener() {
        msIsChild.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                String i = view.getSelectedItem().toString();
                if (i.equals("是")) {
                    tvChildChapter.setVisibility(View.VISIBLE);
                    etChildChapter.setVisibility(View.VISIBLE);
                } else {
                    tvChildChapter.setVisibility(View.INVISIBLE);
                    etChildChapter.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCommitAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etChapterName.isEmpty() || etChapterNum.isEmpty() ||
                        (msIsChild.getText().toString().equals("是") && etChildChapter.isEmpty()) ||
                        !etChapterNum.validate()) {
                    return;
                }
                HashMap<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("textbookId", textbookId);
                paramsMap.put("chapterName", etChapterName.getText().toString());
                paramsMap.put("chapterNum", Integer.parseInt(etChapterNum.getText().toString()));
                int isChild = 0;
                if (msIsChild.getText().toString().equals("是")) {
                    isChild = 1;
                    if (!etChildChapter.validate()) {
                        return;
                    }
                    paramsMap.put("childChapter", Integer.parseInt(etChildChapter.getText().toString()));
                }
                paramsMap.put("isChild", isChild);
                try {
                    RequestManager.getInstance().PostRequest(paramsMap, Constants.ADD_CHAPTER,
                            new RequestManager.ResultCallback() {
                                @Override
                                public void onResponse(String responseCode, String response) {
                                    Type dataType = new TypeToken<Result>(){}.getType();
                                    Result result = JsonParse.getInstance().getResult(response, dataType);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(AddChapterActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    finish();
                                }

                                @Override
                                public void onError(String msg) {
                                    Log.i("Add Chapter", msg);
                                }
                            });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}