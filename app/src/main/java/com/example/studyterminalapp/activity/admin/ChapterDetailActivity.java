package com.example.studyterminalapp.activity.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.adapter.admin.ChapterListAdapter;
import com.example.studyterminalapp.adapter.admin.ChapterQuestionAdapter;
import com.example.studyterminalapp.bean.ChapterBean;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.vo.ChapterQuestionVo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.example.studyterminalapp.views.QuestionListView;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.edittext.ValidatorEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.layout.ExpandableLayout;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class ChapterDetailActivity extends AppCompatActivity {
    private RelativeLayout rlTitleBar;
    private TextView tvTitle, tvChildChapter, tvQuestionList;
    private ImageView ivBack;
    private ChapterBean chapter;
    private MaterialEditText etChapterName;
    private ValidatorEditText etChapterNum, etChildChapter;
    private MaterialSpinner msIsChild;
    private Button btnCommitEdit, btnAddQuestion;
    private ChapterQuestionAdapter chapterQuestionAdapter;
    private ExpandableLayout elQuestion;
    private QuestionListView qlvList;
    private boolean isExpanded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_detail);

        chapter = (ChapterBean) getIntent().getSerializableExtra("chapter");

        initView();
        initListener();
        initData();
    }

    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("章节管理");
        ivBack = findViewById(R.id.iv_back);

        etChapterName = (MaterialEditText) findViewById(R.id.et_chapter_name);
        etChapterNum = (ValidatorEditText) findViewById(R.id.et_chapter_num);
        tvChildChapter = (TextView) findViewById(R.id.tv_child_chapter);
        etChildChapter = (ValidatorEditText) findViewById(R.id.et_child_chapter);
        msIsChild = (MaterialSpinner) findViewById(R.id.ms_is_child);
        btnCommitEdit = (Button) findViewById(R.id.btn_commit_edit);
        elQuestion = (ExpandableLayout) findViewById(R.id.el_question);
        qlvList = (QuestionListView) findViewById(R.id.qlv_list);
        tvQuestionList = (TextView) findViewById(R.id.tv_question_list);

        chapterQuestionAdapter = new ChapterQuestionAdapter(this);
        qlvList.setAdapter(chapterQuestionAdapter);

        btnAddQuestion = (Button) findViewById(R.id.btn_add_question);

        isExpanded = false;
        tvQuestionList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded = !isExpanded;
                elQuestion.setExpanded(isExpanded, true);
            }
        });
    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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

        btnAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChapterDetailActivity.this, AddChapterQuestionActivity.class);
                intent.putExtra("chapterId", chapter.getCid());
                startActivity(intent);
            }
        });

        btnCommitEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etChapterName.isEmpty() || etChapterNum.isEmpty() ||
                        (msIsChild.getText().toString().equals("是") && etChildChapter.isEmpty()) ||
                        !etChapterNum.validate()) {
                    return;
                }
                HashMap<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("textbookId", chapter.getTextbookId());
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
                    RequestManager.getInstance().PostRequest(paramsMap, Constants.UPDATE_CHAPTER,
                            new RequestManager.ResultCallback() {
                                @Override
                                public void onResponse(String responseCode, String response) {
                                    Type dataType = new TypeToken<Result>(){}.getType();
                                    Result result = JsonParse.getInstance().getResult(response, dataType);
                                    switch (result.getStatus()) {
                                        case 200:
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(ChapterDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            break;
                                        default:
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(ChapterDetailActivity.this, "更新失败", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            break;
                                    }
                                    finish();
                                }

                                @Override
                                public void onError(String msg) {
                                    Log.i("Update Chapter", msg);
                                }
                            });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initData() {
        etChapterName.setText(chapter.getChapterName());
        etChapterNum.setText(chapter.getChapterNum().toString());
        if (chapter.getChildChapter() != null) {
            etChildChapter.setText(chapter.getChildChapter().toString());
        }
        msIsChild.setSelectedIndex(chapter.getIsChild());

        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("cid", chapter.getCid());
        try {
            RequestManager.getInstance().GetRequest(paramsMap, Constants.CHAPTER_QUESTION_LIST,
                    new RequestManager.ResultCallback() {
                        @Override
                        public void onResponse(String code, String json) {

                            //Log.d("TEST", "JSON: " + json);
                            Type dataType = new TypeToken<Result<List<ChapterQuestionVo>>>(){}.getType();
                            Result<List<ChapterQuestionVo>> result = JsonParse.getInstance().getResult(json, dataType);
                            List<ChapterQuestionVo> data = result.getData();
                            Log.i("Chapter Question List", data.toString());
                            if (data == null || data.isEmpty()) {
                                return;
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chapterQuestionAdapter.setData(data);
                                }
                            });
                        }

                        @Override
                        public void onError(String msg) {
                            Log.i("Chapter Question List Error", msg);
                        }
                    });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}