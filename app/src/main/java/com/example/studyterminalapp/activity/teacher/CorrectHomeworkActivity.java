package com.example.studyterminalapp.activity.teacher;

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
import com.example.studyterminalapp.bean.Correct;
import com.example.studyterminalapp.bean.vo.UserAnswerAndQuestionVo;
import com.example.studyterminalapp.fragment.FinishedQuestionFragment;
import com.example.studyterminalapp.fragment.QuestionFragment;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.QuestionConstant;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.GravityEnum;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.functions.Consumer;
import rxhttp.wrapper.entity.Progress;
import rxhttp.wrapper.param.RxHttp;

public class CorrectHomeworkActivity extends AppCompatActivity {
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;
    private List<UserAnswerAndQuestionVo> answerAndQuestionList;
    private ViewPager mViewPager; // 主页面来展示子页面 Viewpager
    private List<Fragment> mFragmentList; //存储fragment页面，用来作为构造adapter的参数
    private ArrayList<String> mTitleList; //存储fragment页面，用来作为构造adapter的参数
    private Button btnCommitCorrect;
    private MyFragmentAdapter mStateVPAdapter;
    private HashMap<Integer, Correct> correctMap;
    private Context context;
    private int progress;
    private Thread thread;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correct_homework);

        answerAndQuestionList = (List<UserAnswerAndQuestionVo>) getIntent().getSerializableExtra("answerAndQuestionList");
        userId = getIntent().getIntExtra("userId", 0);
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
                        .content("是否要退出批改？")
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

        btnCommitCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postCorrectBatch();
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
                FinishedQuestionFragment fragment = (FinishedQuestionFragment) mStateVPAdapter.getItem(position);

                if (correctMap != null && correctMap.get(position) != null) {
                    fragment.setCorrectContent(correctMap.get(position));
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
        correctMap = new HashMap<>();

        mFragmentList = new ArrayList<>();
        mTitleList = new ArrayList<>();
        for (int i = 0; i < answerAndQuestionList.size(); i++) {
            FinishedQuestionFragment f = FinishedQuestionFragment.newInstance("我的", R.drawable.ic_launcher_background,
                    context, i, userId, correctMap, answerAndQuestionList.get(i));
            mFragmentList.add(f);
            mTitleList.add("第" + (i + 1) + "题");
        }
    }

    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);

        tvTitle.setText("批改作业");

        mViewPager = findViewById(R.id.viewpager);
        btnCommitCorrect = (Button) findViewById(R.id.btn_commit_correct);
    }


    private void postCorrectBatch() {
        new MaterialDialog.Builder(context)
                .content("是否要提交批改？")
                .positiveText("是")
                .negativeText("否")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        for (Map.Entry<Integer, Correct> entry : correctMap.entrySet()) {
                            new Thread(()->{
                                Correct correct = entry.getValue();
                                RxHttp.postJson(Constants.WEB_SITE + Constants.CORRECT)
                                        .add("uid", correct.getUid())
                                        .add("qid", correct.getQid())
                                        .add("score", correct.getScore())
                                        .add("comment", correct.getComment())
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
                            }).start();
                        }

                        new MaterialDialog.Builder(context)
                                .title("提交批改中...")
                                .content("正在提交批改，请勿离开...")
                                .contentGravity(GravityEnum.CENTER)
                                .progress(false, correctMap.size(), true)
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