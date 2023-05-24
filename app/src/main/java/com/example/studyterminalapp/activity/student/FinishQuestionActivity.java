package com.example.studyterminalapp.activity.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.adapter.MyFragmentAdapter;
import com.example.studyterminalapp.bean.Answer;
import com.example.studyterminalapp.bean.Question;
import com.example.studyterminalapp.bean.Resource;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.vo.QuestionVo;
import com.example.studyterminalapp.bean.vo.SimpleHomeworkVo;
import com.example.studyterminalapp.fragment.QuestionFragment;
import com.example.studyterminalapp.utils.ActivityCollector;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.QuestionConstant;
import com.example.studyterminalapp.utils.RequestManager;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.GravityEnum;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xutil.tip.ToastUtils;

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

public class FinishQuestionActivity extends AppCompatActivity {
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;
    private List<QuestionVo> questionList;
    private ViewPager mViewPager; // 主页面来展示子页面 Viewpager
    private List<Fragment> mFragmentList; //存储fragment页面，用来作为构造adapter的参数
    private ArrayList<String> mTitleList; //存储fragment页面，用来作为构造adapter的参数
    private Button btnCommitAnswer;
    private MyFragmentAdapter mStateVPAdapter;
    private HashMap<Integer, Answer> answerMap;
    private Context context;
    private Thread thread;
    private int progress;
    private SimpleHomeworkVo simpleHomework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finished_question);

        // todo:
        // 传递：
        // putExtras(key, (Serializable)list);
        // 接收：
        // ArrayList list =  (List<Object>) getIntent().getSerialzableExtra(list);
        questionList = (List<QuestionVo>) getIntent().getSerializableExtra("questionList");
        simpleHomework = (SimpleHomeworkVo) getIntent().getSerializableExtra("simpleHomework");
        context = this;

        //Log.i("QList", questionList.toString());

        initView();
        initData();
        setFragmentListener();
    }

    private void startThread(Runnable run) {
        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(run);
        thread.start();
    }

    private void setFragmentListener() {
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
                                finish();
                            }
                        })
                        .show();
            }
        });

        btnCommitAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postAnswerBatch();
            }
        });

        mStateVPAdapter = new MyFragmentAdapter(getSupportFragmentManager(), mFragmentList, mTitleList);
        mViewPager.setAdapter(mStateVPAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                QuestionFragment fragment = (QuestionFragment) mStateVPAdapter.getItem(position);
//                if (position == questionList.size() - 1) {
//                    Button btnCommitAnswer = fragment.getBtnCommitAnswer();
//                    if (btnCommitAnswer != null) {
//                        btnCommitAnswer.setVisibility(View.VISIBLE);
//                    }
//                }

//                Log.i("Answer", "++++++++++++++++++++++++++");
//                for (Map.Entry<Integer, Answer> e : answerMap.entrySet()) {
//                    Log.i("Answer " + e.getKey(), e.getValue().toString());
//                }
//                Log.i("Answer", "++++++++++++++++++++++++++");

                if (answerMap != null && answerMap.get(position) != null) {
                    fragment.setAnswerContent(answerMap.get(position));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    private void initData() {
        progress = 0;
        // 答案
        answerMap = new HashMap<>();

        mFragmentList = new ArrayList<>();
        mTitleList = new ArrayList<>();
        for (int i = 0; i < questionList.size(); i++) {
            QuestionFragment f = QuestionFragment.newInstance("我的", R.drawable.ic_launcher_background,
                    context, i, answerMap, questionList.get(i));
            mFragmentList.add(f);
            mTitleList.add("第" + (i + 1) + "题");
        }
    }

    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);

        tvTitle.setText("答题中");

        mViewPager = findViewById(R.id.viewpager);
        btnCommitAnswer = (Button) findViewById(R.id.btn_commit_answer);
    }


    private void postAnswerBatch() {
        new MaterialDialog.Builder(context)
                .content("是否要提交作答？")
                .positiveText("是")
                .negativeText("否")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        for (int i = 0; i < questionList.size(); i++) {

                            if (!answerMap.containsKey(i)) {
                                Answer answer = new Answer();
                                answer.setQid(questionList.get(i).getQid());
                                answer.setType(questionList.get(i).getQuestionType());
                                answer.setAnswer("");
                                answerMap.put(i, answer);
                            }
                        }
                        /*
                        Log.d("Answer ", "+++++++++++++++++++++++++++++++");
                        for (Map.Entry<Integer, Answer> entry : answerMap.entrySet()) {
                            Log.d("Answer: "+entry.getKey(), entry.getValue().toString());
                        }
                        Log.d("Answer ", "+++++++++++++++++++++++++++++++");
                        */
                        for (Map.Entry<Integer, Answer> entry : answerMap.entrySet()) {
                            new Thread(()->{
                                Answer answer = entry.getValue();
                                if (QuestionConstant.CALCULATION.equals(answer.getType())) {  // 计算题（需要上传文件）
                                    RxHttp.postForm(Constants.WEB_SITE + Constants.USER_QUESTION)
                                            .add("qid", answer.getQid())
                                            .add("answer", answer.getAnswer())
                                            .addPart(context, "file", answer.getFileUri())
                                            .toObservableString()
                                            .onMainProgress(new Consumer<Progress>() {
                                                @Override
                                                public void accept(Progress progress) throws Throwable {}
                                            }).subscribe(new Consumer<String>() {
                                                @Override
                                                public void accept(String s) throws Throwable {
                                                    progress += 1;
                                                    Log.i("Upload Question Progress", "上传成功");
                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Throwable {
                                                    Log.e("Upload Question Progress", "上传异常");
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
                                                    progress += 1;
                                                }
                                            }, new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Throwable {
                                                    Log.e("Post Question Progress", "请求异常");
                                                }
                                            });
                                }
                            }).start();
                        }

                        // 更新教师端状态
                        HashMap<String, Object> paramsMap = new HashMap<>();
                        paramsMap.put("hid", simpleHomework.getHid());
                        try {
                            RequestManager.getInstance().GetRequest(paramsMap, Constants.UPDATE_FINISHED,
                                    new RequestManager.ResultCallback() {
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
                                                        Log.i("Update Finished Progress", "更新成功");
                                                    }
                                                });
                                            }
                                            break;
                                        default:
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Log.e("Update Finished Progress", "更新异常");
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

                        new MaterialDialog.Builder(context)
                                .title("提交作业中...")
                                .content("正在提交作业，请勿离开...")
                                .contentGravity(GravityEnum.CENTER)
                                .progress(false, answerMap.size(), true)
                                .cancelListener(
                                        d -> {
                                            if (thread != null) {
                                                thread.interrupt();
                                            }
                                        })
                                .showListener(
                                        dialogInterface -> {
                                            updateProgress((MaterialDialog) dialogInterface);
                                        })
                                .negativeText("关闭")
                                .show()
                                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialogInterface) {
                                        finish();
                                    }
                                });
                    }
                })
                .show();

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
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
                dialog.setProgress(progress);
                //dialog.incrementProgress(1);
            }
            runOnUiThread(() -> {
                thread = null;
                dialog.setContent("提交成功！");
            });
        });
    }
}