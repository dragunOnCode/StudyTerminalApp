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

import com.bumptech.glide.Glide;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.teacher.TeacherClassDetailActivity;
import com.example.studyterminalapp.adapter.admin.ChapterListAdapter;
import com.example.studyterminalapp.bean.ChapterBean;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.Textbook;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.example.studyterminalapp.views.ChapterListView;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.layout.ExpandableLayout;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class TextbookDetailActivity extends AppCompatActivity {
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;
    private Textbook textbook;
    private ChapterListAdapter chapterListAdapter;
    private ChapterListView clvList;
    private Button btnEditTextbook, btnAddChapter, btnExpandChapter, btnUploadEbook;
    private int tid;
    private TextView tvTextbookName, tvTextbookAuthor, tvPressName, tvCourseName, tvGrade,
            tvPublishDate, tvTextbookDescription;
    private boolean isExpanded;
    private ExpandableLayout elChapter;
    private ImageView ivTextbookPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textbook_detail);

        textbook = (Textbook) getIntent().getSerializableExtra("textbook");
        tid = textbook.getTid();

        initView();
        initData();
    }

    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);

        tvTextbookName = (TextView) findViewById(R.id.tv_textbook_name);
        tvTextbookAuthor = (TextView) findViewById(R.id.tv_textbook_author);
        tvPressName = (TextView) findViewById(R.id.tv_press_name);
        tvCourseName = (TextView) findViewById(R.id.tv_course_name);
        tvGrade = (TextView) findViewById(R.id.tv_grade);
        tvPublishDate = (TextView) findViewById(R.id.tv_publish_date);
        tvTextbookDescription = (TextView) findViewById(R.id.tv_textbook_description);

        clvList = (ChapterListView) findViewById(R.id.clv_list);

        btnEditTextbook = (Button) findViewById(R.id.btn_edit_textbook);
        btnAddChapter = (Button) findViewById(R.id.btn_add_chapter);
        btnExpandChapter = (Button) findViewById(R.id.btn_expand_chapter);
        btnUploadEbook = (Button) findViewById(R.id.btn_upload_ebook);
        ivTextbookPic = (ImageView) findViewById(R.id.iv_textbook_pic);

        tvTitle.setText(textbook.getTextbookName());
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        chapterListAdapter = new ChapterListAdapter(this);
        clvList.setAdapter(chapterListAdapter);

        btnEditTextbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TextbookDetailActivity.this, EditTextbookActivity.class);
                intent.putExtra("textbook", textbook);
                TextbookDetailActivity.this.startActivity(intent);
            }
        });

        btnAddChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Add Chapter", "准备跳转到AddChapter");
                Intent intent = new Intent(TextbookDetailActivity.this, AddChapterActivity.class);
                intent.putExtra("textbookId", tid);
                TextbookDetailActivity.this.startActivity(intent);
            }
        });

        elChapter = (ExpandableLayout) findViewById(R.id.el_chapter);

        isExpanded = false;
        btnExpandChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isExpanded = !isExpanded;
                elChapter.setExpanded(isExpanded, true);
            }
        });

        btnUploadEbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TextbookDetailActivity.this, UploadEbookActivity.class);
                intent.putExtra("textbookId", tid);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        tvTextbookName.setText(textbook.getTextbookName());
        tvTextbookAuthor.setText(textbook.getTextbookAuthor());
        tvPressName.setText(textbook.getPressName());
        tvCourseName.setText(textbook.getCourseName());
        tvGrade.setText(textbook.getGrade());
        tvPublishDate.setText(textbook.getPublishDate());
        tvTextbookDescription.setText(textbook.getTextbookDescription());
        if (textbook.getCoverUrl() != null) {
            Glide.with(TextbookDetailActivity.this)
                    .load(textbook.getCoverUrl())
                    .error(R.mipmap.ic_launcher)
                    .into(ivTextbookPic);
        }

        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("tid", tid);
        try {
            RequestManager.getInstance().GetRequest(paramsMap, Constants.GET_TEXTBOOK_ALL_CHAPTER,
                    new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String code, String json) {

                    //Log.d("TEST", "JSON: " + json);
                    Type dataType = new TypeToken<Result<List<ChapterBean>>>(){}.getType();
                    Result<List<ChapterBean>> result = JsonParse.getInstance().getResult(json, dataType);
                    List<ChapterBean> data = result.getData();
                    Log.i("Chapter List", data.toString());
                    if (data == null || data.isEmpty()) {
                        return;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chapterListAdapter.setData(data);
                        }
                    });
                }

                @Override
                public void onError(String msg) {
                    Log.i("Home Page Error", msg);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}