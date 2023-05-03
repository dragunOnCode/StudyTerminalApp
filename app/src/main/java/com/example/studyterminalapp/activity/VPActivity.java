package com.example.studyterminalapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.adapter.MyFragmentAdapter;
import com.example.studyterminalapp.bean.Answer;
import com.example.studyterminalapp.bean.Question;
import com.example.studyterminalapp.fragment.QuestionFragment;
import com.xuexiang.xui.widget.dialog.materialdialog.GravityEnum;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VPActivity extends AppCompatActivity {
    private ViewPager mViewPager; // 主页面来展示子页面 Viewpager
    private List<Fragment> mFragmentList; //存储fragment页面，用来作为构造adapter的参数
    private ArrayList<String> mTitleList; //存储fragment页面，用来作为构造adapter的参数
    private MyFragmentAdapter mStateVPAdapter;
    private List<Question> questionList;
    private HashMap<Integer, Answer> answerMap;
    private Context context;
    private Button btnDialog;
    private Thread thread;
    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vp);

        context = this;

        initeView();
        initData();
        setFListener();
    }

    private void startThread(Runnable run) {
        if (thread != null) {
            thread.interrupt();
        }
        thread = new Thread(run);
        thread.start();
    }

    private void setFListener() {
        mStateVPAdapter = new MyFragmentAdapter(getSupportFragmentManager(), mFragmentList, mTitleList);
        mViewPager.setAdapter(mStateVPAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        btnDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(()->{
                    while (progress <= 10) {
                        try {
                            Thread.sleep(1000);
                            progress += 1;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                new MaterialDialog.Builder(context)
                        .title("提交作业中...")
                        .content("正在提交作业，请勿离开...")
                        .contentGravity(GravityEnum.CENTER)
                        .progress(false, 10, true)
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
                                progress = 0;
                            }
                        });
            }
        });
    }


    private void initData() {
        // 问题
        questionList = new ArrayList<>();
        // 答案
        answerMap = new HashMap<>();

        for (int i = 0; i < 9; i++) {
            Question q = new Question();
            if (i % 3 == 0) {
                q.setType(0);
                q.setContent("To be, or not to be, that is the question:\n" +
                        "Whether 'tis nobler in the mind to suffer\n" +
                        "The slings and arrows of outrageous fortune,\n" +
                        "Or to take Arms against a Sea of troubles,\n" +
                        "And by opposing end them: to die, to sleep;\n" +
                        "No more; and by a sleep, to say we end\n" +
                        "The heart-ache, and the thousand natural shocks\n" +
                        "That Flesh is heir to? 'Tis a consummation\n" +
                        "Devoutly to be wished. To die, to sleep,\n" +
                        "To sleep, perchance to Dream; aye, there's the rub,\n" +
                        "For in that sleep of death, what dreams may come,\n" +
                        "When we have shuffled off this mortal coil,\n" +
                        "Must give us pause. There's the respect");
                q.setAnswer("B");
            } else if (i % 3 == 1) {
                q.setType(1);
                q.setContent("噫吁嚱，危乎高哉！\n" +
                        "蜀道之难，难于上青天！\n" +
                        "蚕丛及鱼凫，开国何茫然！\n" +
                        "尔来四万八千岁，不与秦塞通人烟。\n" +
                        "西当太白有鸟道，可以横绝峨眉巅。\n" +
                        "地崩山摧壮士死，然后天梯石栈相钩连。\n" +
                        "上有六龙回日之高标，下有冲波逆折之回川。\n" +
                        "黄鹤之飞尚不得过，猿猱欲度愁攀援。\n" +
                        "青泥何盘盘，百步九折萦岩峦。");
                q.setAnswer("唉呀呀，多么高峻多么伟岸！蜀道真太难攀简直难于上青天。\n" +
                        "传说中蚕丛和鱼凫建立了蜀国，开国的年代实在久远无法详谈。\n" +
                        "自从那时至今约有四万八千年，秦蜀被秦岭所阻从不沟通往返。\n" +
                        "西边太白山有飞鸟能过的小道。从那小路走可横渡峨嵋山顶端。\n" +
                        "山崩地裂蜀国五壮士被压死了，两地才有天梯栈道开始相通连。\n" +
                        "上有挡住太阳神六龙车的山巅，下有激浪排空纡回曲折的大川。\n" +
                        "善于高飞的黄鹤尚且无法飞过，即使猢狲要想翻过也愁于攀援。\n" +
                        "青泥岭多么曲折绕着山峦盘旋，百步之内萦绕岩峦转九个弯弯。");
            } else {
                q.setType(2);
                q.setContent("首先变换梗中的等价关系，即将blna-alnb=a-b变换成[1-ln(1/a)]/a=[1-ln(1/b)]/b.变换后很容易发现1/a和1/b是函数f(x)中函数值相等的点的横坐标。所以需要用f(x)来代换求解。\n" +
                        "当m=1/a，n=1/b，则f(m)=f(n)时，证明结论变为2 <m n <e，即2-m <n <e-m.Mn因为a b .那么，如果m <n，就有0 <m <1，n > 1。\n" +
                        "证明结论的前半部分，即2-m <n .证明这个结论，可以用f(x)的单调性，但需要用在同一个单调区间上。所以需要构造一个新的函数g(x)=f(x)-f(2-x)，0 <x <1。G(x)是(0<m<1)中的递增函数，0 <m <1，所以g (m) <g (1)=0，从而得到f (m) <f (2-m)。\n" +
                        "而且因为f(n)=f(m)，f (n) <f (2-m)。\n" +
                        "0 <m <1，so 2-m > 1和n > 1，f(x)是x > 1上的递减函数，so n > 2-m，即m n > 2。");
                q.setAnswer(String.valueOf(i));
            }
            questionList.add(q);
        }

        mFragmentList = new ArrayList<>();
        mTitleList = new ArrayList<>();
//        for (int i = 0; i < questionList.size(); i++) {
//            QuestionFragment f = QuestionFragment.newInstance("我的", R.drawable.ic_launcher_background,
//                    context, answerMap, questionList.get(i));
//            mFragmentList.add(f);
//            mTitleList.add("第" + i + "题");
//        }

//        //添加页面
//        mFragmentList.add(f1);
//        mFragmentList.add(f2);
//        mFragmentList.add(f3);
//
//        String t1 = "我";
//        String t2 = "是";
//        String t3 = "谁";
//        mTitleList.add(t1);
//        mTitleList.add(t2);
//        mTitleList.add(t3);
    }

    private void initeView() {
        mViewPager = findViewById(R.id.vp);
        btnDialog = (Button) findViewById(R.id.btn_dialog);
    }

    private void updateProgress(MaterialDialog dialogInterface) {
        Log.d("Update Progress", "被调用了");
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
                dialog.setProgress(progress);
            }
            runOnUiThread(() -> {
                thread = null;
                dialog.setContent("提交成功！");
            });
        });
    }
}