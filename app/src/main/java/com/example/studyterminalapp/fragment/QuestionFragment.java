package com.example.studyterminalapp.fragment;

import static com.example.studyterminalapp.utils.FileSizeUtil.getUriForFile;
import static com.xuexiang.xutil.XUtil.runOnUiThread;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.FileExplorerFragment;
import com.example.studyterminalapp.activity.admin.UploadEbookActivity;
import com.example.studyterminalapp.activity.student.FinishHomeworkActivity;
import com.example.studyterminalapp.adapter.student.QuestionChoicesAdapter;
import com.example.studyterminalapp.bean.Answer;
import com.example.studyterminalapp.bean.Question;
import com.example.studyterminalapp.bean.vo.QuestionVo;
import com.example.studyterminalapp.utils.QuestionConstant;
import com.example.studyterminalapp.views.ChoicesListView;
import com.github.gzuliyujiang.filepicker.ExplorerConfig;
import com.github.gzuliyujiang.filepicker.FilePicker;
import com.github.gzuliyujiang.filepicker.annotation.ExplorerMode;
import com.github.gzuliyujiang.filepicker.contract.OnFilePickedListener;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuestionFragment extends Fragment implements OnFilePickedListener {
    private static final String ARG_PARAM1 = "title";
    private static final String ARG_PARAM2 = "img";

    private ImageView ivQuestion, ivCalculation;
    private TextView tvQuestionType, tvQuestionContent;
    private LinearLayout llAnswerInfo, llChoices, llBlank, llCalculation;
    private MultiLineEditText etBlank;
    private Button btnUploadCalculation, btnRecord, btnCommitAnswer;
    private ChoicesListView lvChoices;
    private ArrayList<String> choicesList;
    private ArrayList<Integer> availableQuestions;
    private int idx;
    private QuestionVo question;
    private QuestionChoicesAdapter questionChoicesAdapter;
    private int selectPosition;
    private Uri fileUri;
    private Context context;
    private HashMap<Integer, Answer> answerMap;

    private String title;
    private int img;

    public Button getBtnCommitAnswer() {
        return btnCommitAnswer;
    }

    public void setBtnCommitAnswer(Button btnCommitAnswer) {
        this.btnCommitAnswer = btnCommitAnswer;
    }

    public static QuestionFragment newInstance(String title, int img, Context context, int idx, HashMap<Integer, Answer> answerMap,
                                               QuestionVo question) {
        QuestionFragment fragment = new QuestionFragment(context, idx, answerMap, question);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putInt(ARG_PARAM2,img);
        fragment.setArguments(args);
        return fragment;
    }

    public QuestionFragment() {
    }

    public QuestionFragment(Context context, int idx, HashMap<Integer, Answer> answerMap, QuestionVo question) {
        this.context = context;
        this.answerMap = answerMap;
        this.question = question;
        this.idx = idx;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            img = getArguments().getInt(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);
        Bundle argument = getArguments();
        //ImageView iv = view.findViewById(R.id.iv);
        //iv.setImageResource(argument.getInt(ARG_PARAM2,R.drawable.ji1));
        initView(view);
        runOnUiThread(()->{
            initData();
        });
        initListener();
        return view;
    }

    private void initView(View view) {
        choicesList = new ArrayList<>();
        selectPosition = -1;

        ivQuestion = (ImageView) view.findViewById(R.id.iv_question);
        ivCalculation = (ImageView) view.findViewById(R.id.iv_calculation);
        tvQuestionType = (TextView) view.findViewById(R.id.tv_question_type);
        tvQuestionContent = (TextView) view.findViewById(R.id.tv_question_content);
        llAnswerInfo = (LinearLayout) view.findViewById(R.id.ll_answer_info);
        llChoices = (LinearLayout) view.findViewById(R.id.ll_choices);
        llBlank = (LinearLayout) view.findViewById(R.id.ll_blank);
        llCalculation = (LinearLayout) view.findViewById(R.id.ll_calculation);
        lvChoices = (ChoicesListView) view.findViewById(R.id.lv_choices);
        etBlank = (MultiLineEditText) view.findViewById(R.id.et_blank);
        btnUploadCalculation = (Button) view.findViewById(R.id.btn_upload_calculation);
        btnRecord = (Button) view.findViewById(R.id.btn_record);
        btnCommitAnswer = (Button) view.findViewById(R.id.btn_commit_answer);
        btnCommitAnswer.setVisibility(View.INVISIBLE);
    }

    private void initListener() {
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answerMap.put(idx, getAnswerContent());
            }
        });
    }

    private void initData() {
        // todo: 获得数据
        if (question == null) {
            return;
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
                        //new FileExplorerFragment().show(getChildFragmentManager(), getClass().getName());
                        ExplorerConfig config = new ExplorerConfig(getActivity());
                        config.setRootDir(new File("sdcard"));
                        config.setLoadAsync(false);
                        config.setExplorerMode(ExplorerMode.FILE);
                        config.setOnFilePickedListener(QuestionFragment.this);
                        FilePicker picker = new FilePicker(getActivity());
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
        if (question.getImgUrl() != null) {
            Glide.with(context)
                    .load(question.getImgUrl())
                    .error(R.mipmap.ic_launcher)
                    .into(ivQuestion);
        } else {
            ivQuestion.setVisibility(View.GONE);
        }
    }

    private Answer getAnswerContent() {
        if (question == null) {
            return null;
        }
        Answer answer = new Answer();
        answer.setQid(question.getQid());
        answer.setType(question.getQuestionType());
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
            default:
                break;
        }
        return answer;
    }

    public void setAnswerContent(Answer answer) {
        if (answer == null) {
            return;
        }
        switch (question.getQuestionType()) {
            case QuestionConstant.CHOICES:
                if (answer.getAnswer() != null && answer.getAnswer().length() > 0) {
                    char c = answer.getAnswer().charAt(0);
                    int choiceValue = (int) c - 65;
                    questionChoicesAdapter.setSelectPosition(choiceValue);
                    questionChoicesAdapter.notifyDataSetChanged();
//                    RadioButton rb = lvChoices.getChildAt(selectPosition).findViewById(R.id.rb_select);
//                    rb.setChecked(true);
                }
                break;
            case QuestionConstant.BLANK:
                etBlank.setContentText(answer.getAnswer());
                break;
            case QuestionConstant.CALCULATION:
                Glide.with(context)
                        .load(answer.getFileUri())
                        .error(R.mipmap.ic_launcher)
                        .into(ivCalculation);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFilePicked(@NonNull File file) {
        //Toast.makeText(getApplicationContext(), file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        ToastUtils.toast(file.getAbsolutePath());
        fileUri = getUriForFile(context, file);
        Glide.with(context)
                .load(file)
                .error(R.mipmap.ic_launcher)
                .into(ivCalculation);
    }

}