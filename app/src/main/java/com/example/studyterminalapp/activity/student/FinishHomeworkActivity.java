package com.example.studyterminalapp.activity.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.PageActivity;
import com.example.studyterminalapp.activity.QPageActivity;
import com.example.studyterminalapp.activity.admin.UploadEbookActivity;
import com.example.studyterminalapp.activity.teacher.TeacherClassDetailActivity;
import com.example.studyterminalapp.adapter.student.QuestionChoicesAdapter;
import com.example.studyterminalapp.bean.Answer;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.vo.QuestionVo;
import com.example.studyterminalapp.utils.ActivityCollector;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.QuestionConstant;
import com.example.studyterminalapp.utils.RequestManager;
import com.example.studyterminalapp.utils.UserManage;
import com.example.studyterminalapp.views.ChoicesListView;
import com.github.gzuliyujiang.filepicker.ExplorerConfig;
import com.github.gzuliyujiang.filepicker.FilePicker;
import com.github.gzuliyujiang.filepicker.annotation.ExplorerMode;
import com.github.gzuliyujiang.filepicker.contract.OnFilePickedListener;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import io.reactivex.rxjava3.functions.Consumer;
import rxhttp.wrapper.entity.Progress;
import rxhttp.wrapper.param.RxHttp;

public class FinishHomeworkActivity extends AppCompatActivity implements OnFilePickedListener {
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack, ivCalculation;
    private TextView tvQuestionType, tvQuestionContent;
    private LinearLayout llAnswerInfo, llChoices, llBlank, llCalculation;
    private MultiLineEditText etBlank;
    private Button btnUploadCalculation, btnPrevPage, btnNextPage, btnCommitAnswer;
    private ChoicesListView lvChoices;
    private ArrayList<String> choicesList;
    private ArrayList<Integer> availableQuestions;
    private int idx;
    private QuestionVo question;
    private QuestionChoicesAdapter questionChoicesAdapter;
    private int selectPosition;
    private Uri fileUri;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_homework);

        idx = getIntent().getIntExtra("idx", 0);
        availableQuestions = getIntent().getIntegerArrayListExtra("availableQuestions");
        context = this;
        ActivityCollector.addActivity(this);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);

        tvTitle.setText("答题中");

        choicesList = new ArrayList<>();
        selectPosition = -1;


        ivCalculation = (ImageView) findViewById(R.id.iv_calculation);
        tvQuestionType = (TextView) findViewById(R.id.tv_question_type);
        tvQuestionContent = (TextView) findViewById(R.id.tv_question_content);
        llAnswerInfo = (LinearLayout) findViewById(R.id.ll_answer_info);
        llChoices = (LinearLayout) findViewById(R.id.ll_choices);
        llBlank = (LinearLayout) findViewById(R.id.ll_blank);
        llCalculation = (LinearLayout) findViewById(R.id.ll_calculation);
        lvChoices = (ChoicesListView) findViewById(R.id.lv_choices);
        etBlank = (MultiLineEditText) findViewById(R.id.et_blank);
        btnUploadCalculation = (Button) findViewById(R.id.btn_upload_calculation);
        btnPrevPage = (Button) findViewById(R.id.btn_prev_page);
        btnNextPage = (Button) findViewById(R.id.btn_next_page);
        btnCommitAnswer = (Button) findViewById(R.id.btn_commit_answer);

        //questionChoicesAdapter = new QuestionChoicesAdapter(context);
        //lvChoices.setAdapter(questionChoicesAdapter);
    }

    private void initListener() {
        // 上一题
        if (idx == 0) {
            btnPrevPage.setVisibility(View.GONE);
        } else {
            btnPrevPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (question != null) {
                        MyApp.addAnswer(question.getQid(), getAnswerContent());
                    }
                    Intent intent = new Intent(context, FinishHomeworkActivity.class);
                    intent.putExtra("idx", idx - 1);
                    intent.putIntegerArrayListExtra("availableQuestions", availableQuestions);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_in_left, 0);
//                    Log.i("Answer prev", "-------------------------");
//                    for (Map.Entry<Integer, Answer> entry : MyApp.getAnswers().entrySet()) {
//                        Log.i("Answer " + entry.getKey(), entry.getValue().toString());
//                    }
//                    Log.i("Answer prev", "-------------------------");
                }
            });
        }

        // 下一题
        if (idx == availableQuestions.size() - 1) {
            btnNextPage.setVisibility(View.GONE);
        } else {
            btnNextPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (question != null) {
                        MyApp.addAnswer(question.getQid(), getAnswerContent());
                    }
                    Intent intent = new Intent(context, FinishHomeworkActivity.class);
                    intent.putExtra("idx", idx + 1);
                    intent.putIntegerArrayListExtra("availableQuestions", availableQuestions);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.slide_out_right, 0);
//                    Log.i("Answer next", "++++++++++++++++++++++++++");
//                    for (Map.Entry<Integer, Answer> entry : MyApp.getAnswers().entrySet()) {
//                        Log.i("Answer " + entry.getKey(), entry.getValue().toString());
//                    }
//                    Log.i("Answer next", "++++++++++++++++++++++++++");
                }
            });
        }

        // 退出答题
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(context)
                        .content("是否要退出作答？")
                        .positiveText("是")
                        .negativeText("否")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                MyApp.clearAnswers();
                                // Intent intent = new Intent(.this, PageActivity.class);
                                // startActivity(intent);
                                ActivityCollector.finishOneActivity("com.example.studyterminalapp.activity.student.FinishHomeworkActivity");
                            }
                        })
                        .show();
            }
        });

        // 提交所有题目
        if (idx != availableQuestions.size() - 1) {
            btnCommitAnswer.setVisibility(View.GONE);
        } else {
            btnCommitAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // todo: 提交题目
                    if (question != null) {
                        MyApp.addAnswer(question.getQid(), getAnswerContent());
                    }
                    Log.d("Answer Commit", "============================");
                    for (Map.Entry<Integer, Answer> entry : MyApp.getAnswers().entrySet()) {
                        Log.i("Answer " + entry.getKey(), entry.getValue().toString());
                    }
                    Log.d("Answer Commit", "============================");
                    postAnswerBatch();
                }
            });
        }
    }

    private void initData() {
        if (availableQuestions == null || availableQuestions.isEmpty()) {
            ToastUtils.toast("没有有效题目");
        }
        getQuestion(availableQuestions.get(idx));
    }


    private void getQuestion(int qid) {
        if (qid <= 0) {
            return;
        }
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("qid", qid);
        try {
            RequestManager.getInstance().GetRequest(paramsMap, Constants.QUESTION, new RequestManager.ResultCallback() {
                @Override
                public void onResponse(String responseCode, String json) {
                    Type dataType = new TypeToken<Result<QuestionVo>>(){}.getType();
                    Result<QuestionVo> response = JsonParse.getInstance().getPageInfoResult(json, dataType);
                    int code = response.getStatus();
                    switch (code) {
                        case 200:
                            //Log.d("TEST", "JSON: " + json);
                            if (response.getData() != null) {
                                question = response.getData();
                                Log.d("Question Detail", question.toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String questionType = "";
                                        switch (question.getQuestionType()) {
                                            case QuestionConstant.CHOICES:
                                                questionType = "选择题";
                                                llBlank.setVisibility(View.GONE);
                                                llCalculation.setVisibility(View.GONE);
                                                questionChoicesAdapter = new QuestionChoicesAdapter(context);
                                                lvChoices.setAdapter(questionChoicesAdapter);
                                                choicesList = (ArrayList<String>) question.getChoiceSelectionList();
                                                questionChoicesAdapter.setData(choicesList);
                                                // 选中选项
                                                lvChoices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        //获取选中的参数
                                                        selectPosition = position;
                                                        questionChoicesAdapter.setSelectPosition(selectPosition);
                                                        questionChoicesAdapter.notifyDataSetChanged();
                                                        //ToastUtils.toast("选中" + selectPosition + choicesList.get(position));
                                                    }
                                                });
                                                break;
                                            case QuestionConstant.BLANK:
                                                questionType = "填空题";
                                                llChoices.setVisibility(View.GONE);
                                                llCalculation.setVisibility(View.GONE);
                                                break;
                                            case QuestionConstant.CALCULATION:
                                                questionType = "计算题";
                                                llBlank.setVisibility(View.GONE);
                                                llChoices.setVisibility(View.GONE);
                                                // 上传图片
                                                btnUploadCalculation.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        ExplorerConfig config = new ExplorerConfig(context);
                                                        config.setRootDir(getExternalFilesDir(null));
                                                        config.setLoadAsync(false);
                                                        config.setExplorerMode(ExplorerMode.FILE);
                                                        config.setOnFilePickedListener(FinishHomeworkActivity.this);
                                                        FilePicker picker = new FilePicker(FinishHomeworkActivity.this);
                                                        picker.setExplorerConfig(config);
                                                        picker.show();
                                                    }
                                                });
                                                break;
                                            default:
                                                break;
                                        }
                                        tvQuestionType.setText(questionType);
                                        tvQuestionContent.setText(question.getQuestionContent());
                                    }
                                });
                            }
                            break;
                        default:
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtils.toast(response.getMsg());
                                }
                            });
                            break;
                    }

                }

                @Override
                public void onError(String msg) {
                    Log.i("Question Error", msg);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private Answer getAnswerContent() {
        if (question == null) {
            return null;
        }
        Answer answer = new Answer();
        answer.setQid(question.getQid());
        switch (question.getQuestionType()) {
            case QuestionConstant.CHOICES:
                String choice = selectPosition == -1 ? "" : ((char)('A' + selectPosition)) + "";
                answer.setAnswer(choice);
                break;
            case QuestionConstant.BLANK:
                answer.setAnswer(etBlank.getContentText());
                break;
            case QuestionConstant.CALCULATION:
                answer.setFileUri(fileUri);
                break;
        }
        return answer;
    }

    private void postAnswerBatch() {
        new MaterialDialog.Builder(context)
                .content("是否要提交作答？")
                .positiveText("是")
                .negativeText("否")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        CountDownLatch latch = new CountDownLatch(MyApp.getAnswers().size());
                        for (Map.Entry<Integer, Answer> entry : MyApp.getAnswers().entrySet()) {
                            new Thread(()->{
                                Answer answer = entry.getValue();
                                if (answer.getFileUri() != null) {  // 计算题（需要上传文件）
                                    RxHttp.postForm(Constants.WEB_SITE + Constants.USER_QUESTION)
                                            .add("qid", answer.getQid())
                                            .add("answer", answer.getAnswer())
                                            .addPart(context, "file", fileUri)
                                            .toObservableString()
                                            .onMainProgress(new Consumer<Progress>() {
                                                @Override
                                                public void accept(Progress progress) throws Throwable {}
                                            }).subscribe(new Consumer<String>() {
                                                @Override
                                                public void accept(String s) throws Throwable {
                                                    latch.countDown();
                                                    Log.i("Upload Progress", "上传成功");
                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Throwable {
                                                    latch.countDown();
                                                    Log.e("Upload Progress", "上传异常");
                                                }
                                            });
                                } else {    // 选择、填空题
                                    RxHttp.postForm(Constants.WEB_SITE + Constants.INSERT_USER_QUESTION)
                                            .add("uid", MyApp.getId())
                                            .add("qid", answer.getQid())
                                            .add("answer", answer.getAnswer())
                                            .toObservableString()
                                            .subscribe(new Consumer<String>() {
                                                @Override
                                                public void accept(String s) throws Throwable {
                                                    latch.countDown();
                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Throwable {
                                                    latch.countDown();
                                                    Log.e("Request Error", "请求异常");
                                                }
                                            });
                                }
                            }).start();
                        }
                        // 等待所有请求完毕
                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        MyApp.clearAnswers();
                        ActivityCollector.finishOneActivity("com.example.studyterminalapp.activity.student.FinishHomeworkActivity");
                    }
                })
                .show();

    }

    @Override
    public void onFilePicked(@NonNull File file) {
        //Toast.makeText(getApplicationContext(), file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        fileUri = getUriForFile(this, file);
        Glide.with(context)
                .load(file)
                .error(R.mipmap.ic_launcher)
                .into(ivCalculation);
    }

    // 获取真实Uri
    private static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.choosecrop.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}