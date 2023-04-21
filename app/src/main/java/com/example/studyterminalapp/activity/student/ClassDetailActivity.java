package com.example.studyterminalapp.activity.student;

import static com.example.studyterminalapp.utils.ResourceConstant.RESOURCE_NOT_FOUND;
import static com.xuexiang.xutil.XUtil.runOnUiThread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.teacher.TeacherClassDetailActivity;
import com.example.studyterminalapp.adapter.student.HomeworkListAdapter;
import com.example.studyterminalapp.adapter.teacher.ResourceListAdapter;
import com.example.studyterminalapp.adapter.teacher.StudentListAdapter;
import com.example.studyterminalapp.bean.Class;
import com.example.studyterminalapp.bean.HomeClassBean;
import com.example.studyterminalapp.bean.Resource;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.Textbook;
import com.example.studyterminalapp.bean.vo.PageInfo;
import com.example.studyterminalapp.bean.vo.SimpleHomeworkVo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.RequestManager;
import com.example.studyterminalapp.views.HomeworkListView;
import com.example.studyterminalapp.views.ResourceListView;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.GravityEnum;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.layout.ExpandableLayout;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import rxhttp.wrapper.param.RxHttp;

public class ClassDetailActivity extends AppCompatActivity {
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;
    private TextView tvClassName, tvSchool, tvCourseName, tvGrade, tvCourseDescription, tvResource,
                        tvHomework;
    private ExpandableLayout elResource, elHomework;
    private ResourceListView rlvList;
    private HomeworkListView hlvList;
    private HomeClassBean classBean;
    private Class classInfo;
    private boolean isResourceExpanded, isHomeworkExpanded;
    private ResourceListAdapter resourceListAdapter;
    private HomeworkListAdapter homeworkListAdapter;
    private RelativeLayout bookItem;
    int currentProgress;
    long currentSize;
    long totalSize;
    private Thread thread;
    private Context context;
    private Textbook textbook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);

        classBean = (HomeClassBean) getIntent().getSerializableExtra("class");
        context = this;

        initView();
        initListener();
        initData();
    }

    private void initView() {
        isHomeworkExpanded = false;
        isResourceExpanded = false;

        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);

        tvClassName = (TextView) findViewById(R.id.tv_class_name);
        tvSchool = (TextView) findViewById(R.id.tv_school);
        tvCourseName = (TextView) findViewById(R.id.tv_course_name);
        tvGrade = (TextView) findViewById(R.id.tv_grade);
        tvCourseDescription = (TextView) findViewById(R.id.tv_course_description);
        tvResource = (TextView) findViewById(R.id.tv_resource);
        tvHomework = (TextView) findViewById(R.id.tv_homework);
        elResource = (ExpandableLayout) findViewById(R.id.el_resource);
        elHomework = (ExpandableLayout) findViewById(R.id.el_homework);
        rlvList = (ResourceListView) findViewById(R.id.rlv_list);
        hlvList = (HomeworkListView) findViewById(R.id.hlv_list);

        resourceListAdapter = new ResourceListAdapter(this, 0);
        rlvList.setAdapter(resourceListAdapter);

        homeworkListAdapter = new HomeworkListAdapter(this);
        hlvList.setAdapter(homeworkListAdapter);

        bookItem = (RelativeLayout) findViewById(R.id.book_item);
    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvResource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isResourceExpanded = !isResourceExpanded;
                elResource.setExpanded(isResourceExpanded, true);
            }
        });

        tvHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isHomeworkExpanded = !isHomeworkExpanded;
                elHomework.setExpanded(isHomeworkExpanded, true);
            }
        });

        bookItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (textbook == null || textbook.getContentUrl() == null) {
                    ToastUtils.toast("找不到教辅");
                    return;
                }
                new MaterialDialog.Builder(context)
                        .content("是否要下载该教辅？")
                        .positiveText("是")
                        .negativeText("否")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String appPath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                                String[] split = textbook.getContentUrl().split("\\.");
                                String extension = split[split.length - 1];
                                RxHttp.get(textbook.getContentUrl())
                                        .toDownloadObservable(appPath + "/" + textbook.getTextbookName() + "." + extension)
                                        .onMainProgress(progress -> {
                                            //下载进度回调,0-100，仅在进度有更新时才会回调，最多回调101次，最后一次回调文件存储路径
                                            currentProgress = progress.getProgress(); //当前进度 0-100
                                            currentSize = progress.getCurrentSize(); //当前已下载的字节大小
                                            totalSize = progress.getTotalSize();     //要下载的总字节大小
                                            Log.e("Download Progress", currentProgress + " | " + currentSize + " : " + totalSize);
                                        })
                                        .subscribe(s -> {                  //s为String类型，这里为文件存储路径
                                            ToastUtils.toast("下载完成");
                                        }, throwable -> {
                                            ToastUtils.toast("下载失败");
                                        });

                                new MaterialDialog.Builder(ClassDetailActivity.this)
                                        .title(R.string.tip_download)
                                        .content(R.string.content_downloading)
                                        .contentGravity(GravityEnum.CENTER)
                                        .progress(false, 100, true)
                                        .showListener(
                                                dialogInterface -> {
                                                    updateProgress((MaterialDialog) dialogInterface);
                                                })
                                        .negativeText("关闭")
                                        .show();
                            }
                        })
                        .show();
            }
        });
    }

    private void initData() {
        tvTitle.setText("班级详情");

        getClassInfo();
        getClassResource();
        getHomework();
    }

    private void startThread(Runnable run) {
        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(run);
        thread.start();
    }

    private void updateProgress(MaterialDialog dialogInterface) {
        final MaterialDialog dialog = dialogInterface;
        startThread(() -> {
            while (dialog.getCurrentProgress() != dialog.getMaxProgress()
                    && !Thread.currentThread().isInterrupted()) {
                if (dialog.isCancelled()) {
                    break;
                }
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    break;
                }
                dialog.setProgress(currentProgress);
            }
            runOnUiThread(() -> {
                thread = null;
                dialog.setContent("下载完成");
            });
        });
    }

    private void getClassInfo() {
        String url = Constants.CLASS + "/" + classBean.getCid();
        try {
            RequestManager.getInstance().GetRequest(new HashMap<>(), url, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String c, String json) {

                    //Log.d("TEST", "JSON: " + json);
                    Type dataType = new TypeToken<Result<Class>>(){}.getType();
                    Result<Class> result = JsonParse.getInstance().getResult(json, dataType);
                    Integer code = result.getStatus();
                    switch (code) {
                        case 200:
                            classInfo = result.getData();
                            if (classInfo == null) {
                                return;
                            }
                            getTextbook();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tvClassName.setText(classInfo.getClassName());
                                    tvSchool.setText(classInfo.getSchoolName());
                                    tvCourseName.setText(classInfo.getCourseName());
                                    tvGrade.setText(classInfo.getGrade());
                                    tvCourseDescription.setText(classInfo.getCourseDescription());
                                }
                            });
                            break;
                        default:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.toast("查询异常");
                                }
                            });
                            break;
                    }
                }

                @Override
                public void onError(String msg) {
                    Log.i("Student Class Detail", msg);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void getTextbook() {
        if (classInfo == null || classInfo.getTextbookId() == null) {
            bookItem.setVisibility(View.INVISIBLE);
            return;
        } else {
            String url = Constants.TEXTBOOK + "/" + classInfo.getTextbookId().toString();
            try {
                RequestManager.getInstance().GetRequest(new HashMap<>(), url, new RequestManager.ResultCallback() {
                    @Override
                    public void onResponse(String code, String json) {
                        //Log.d("TEST", "JSON: " + json);
                        Type dataType = new TypeToken<Result<Textbook>>(){}.getType();
                        Result<Textbook> result = JsonParse.getInstance().getResult(json, dataType);
                        textbook = result.getData();
                        if (textbook == null) {
                            return;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView tvTextbookName = bookItem.findViewById(R.id.tv_textbook_name);
                                TextView tvTextbookAuthor = bookItem.findViewById(R.id.tv_textbook_author);
                                TextView tvPressName = bookItem.findViewById(R.id.tv_press_name);
                                TextView tvCourseName = bookItem.findViewById(R.id.tv_course_name);
                                TextView tvGrade = bookItem.findViewById(R.id.tv_grade);
                                ImageView ivTextbookPic = bookItem.findViewById(R.id.iv_textbook_pic);

                                tvTextbookName.setText(textbook.getTextbookName());
                                tvTextbookAuthor.setText(textbook.getTextbookAuthor());
                                tvPressName.setText(textbook.getPressName());
                                tvCourseName.setText(textbook.getCourseName());
                                tvGrade.setText(textbook.getGrade());
                                Glide.with(ClassDetailActivity.this)
                                        .load(textbook.getTextbookPic())
                                        .error(R.mipmap.ic_launcher)
                                        .into(ivTextbookPic);
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

    private void getClassResource() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("cid", classBean.getCid());
        try {
            RequestManager.getInstance().GetRequest(paramsMap, Constants.RESOURCE_CLASS_ALL, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String c, String json) {

                    //Log.d("TEST", "JSON: " + json);
                    Type dataType = new TypeToken<Result<List<Resource>>>(){}.getType();
                    Result<List<Resource>> result = JsonParse.getInstance().getResult(json, dataType);
                    Integer code = result.getStatus();
                    switch (code) {
                        case 200:
                            if (result.getData() != null && !result.getData().isEmpty()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        resourceListAdapter.setData(result.getData());
                                    }
                                });
                            }
                            break;
                        default:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.toast("查询资源异常");
                                }
                            });
                            break;
                    }
                }

                @Override
                public void onError(String msg) {
                    Log.i("Student Class Resource", msg);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void getHomework() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("cid", classBean.getCid());
        try {
            RequestManager.getInstance().GetRequest(paramsMap, Constants.HOMEWORK_CLASS_SIMPLE, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String c, String json) {
                    Type dataType = new TypeToken<Result<List<SimpleHomeworkVo>>>(){}.getType();
                    Result<List<SimpleHomeworkVo>> result = JsonParse.getInstance().getResult(json, dataType);
                    Integer code = result.getStatus();
                    switch (code) {
                        case 200:
                            if (result.getData() != null && !result.getData().isEmpty()) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        homeworkListAdapter.setData(result.getData());
                                    }
                                });
                            }
                            break;
                        default:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.toast("查询班级作业异常");
                                }
                            });
                            break;
                    }
                }

                @Override
                public void onError(String msg) {
                    Log.i("Student Class Homework", msg);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}