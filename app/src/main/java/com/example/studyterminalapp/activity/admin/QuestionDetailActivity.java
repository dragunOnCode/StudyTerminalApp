package com.example.studyterminalapp.activity.admin;

import static android.os.Environment.DIRECTORY_DCIM;
import static android.os.FileUtils.copy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.CropActivity;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.vo.QuestionVo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.QuestionConstant;
import com.example.studyterminalapp.utils.RequestManager;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.button.RippleView;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuestionDetailActivity extends AppCompatActivity {
    private RelativeLayout rlTitleBar;
    private TextView tvTitle, tvChoicesSelection;
    private ImageView ivBack;
    private MaterialSpinner msCourseName, msGrade, msQuestionType, msQuestionDifficulty;
    private MultiLineEditText etQuestionContent, etChoicesSelection, etQuestionSolution, etQuestionAnalysis;
    private RippleView btnConfirm;
    private ImageView ivImgUrl;
    private Button btnSelectImg, btnUploadImg;
    private QuestionVo question;
    private String cropPath;

    //private ImageView mHeader_iv, ivTemp;

    //相册请求码
    private static final int DICM_REQUEST_CODE = 1;
    //相机请求码
    private static final int CAMERA_REQUEST_CODE = 2;
    //剪裁请求码
    private static final int CROP_REQUEST_CODE = 3;

    //调用照相机返回图片文件
    private File tempFile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);

        question = (QuestionVo) getIntent().getSerializableExtra("question");

        initView();
        initListener();
    }

    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);

        tvChoicesSelection = (TextView) findViewById(R.id.tv_choices_selection);
        msCourseName = (MaterialSpinner) findViewById(R.id.ms_course_name);
        msGrade = (MaterialSpinner) findViewById(R.id.ms_grade);
        msQuestionType = (MaterialSpinner) findViewById(R.id.ms_question_type);
        msQuestionDifficulty = (MaterialSpinner) findViewById(R.id.ms_question_difficulty);
        etQuestionContent = (MultiLineEditText) findViewById(R.id.et_question_content);
        etChoicesSelection = (MultiLineEditText) findViewById(R.id.et_choices_selection);
        etQuestionSolution = (MultiLineEditText) findViewById(R.id.et_question_solution);
        etQuestionAnalysis = (MultiLineEditText) findViewById(R.id.et_question_analysis);
        ivImgUrl = (ImageView) findViewById(R.id.iv_img_url);

        btnSelectImg = (Button) findViewById(R.id.btn_select_img);
        btnUploadImg = (Button) findViewById(R.id.btn_upload_img);
        btnConfirm = (RippleView) findViewById(R.id.btn_confirm);

        tvTitle.setText("题目管理");

        msCourseName.setText(question.getCourseName());
        msGrade.setText(question.getGrade());
        msQuestionType.setText(question.getQuestionType());
        msQuestionDifficulty.setText(question.getQuestionDifficulty());
        etQuestionContent.setContentText(question.getQuestionContent());
        etQuestionSolution.setContentText(question.getQuestionSolution());
        etQuestionAnalysis.setContentText(question.getQuestionAnalysis());
        if (QuestionConstant.CHOICES.equals(question.getQuestionType())) {
            etChoicesSelection.setContentText(String.join("\n", question.getChoiceSelectionList()));
        }
        Glide.with(QuestionDetailActivity.this)
                .load(question.getImgUrl())
                .error(R.mipmap.ic_launcher)
                .into(ivImgUrl);
    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        ivImgUrl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bitmap bitmap = ((BitmapDrawable) ivImgUrl.getDrawable()).getBitmap();
//                bigImageLoader(bitmap);
//            }
//        });

        //btnUploadImg.setOnClickListener(this);
        btnSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPicFromAlbm();
                btnUploadImg.setEnabled(true);
            }
        });

        msQuestionType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                String i = view.getSelectedItem().toString();
                if (QuestionConstant.CHOICES.equals(i)) {
                    tvChoicesSelection.setVisibility(View.VISIBLE);
                    etChoicesSelection.setVisibility(View.VISIBLE);
                } else {
                    tvChoicesSelection.setVisibility(View.INVISIBLE);
                    etChoicesSelection.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etQuestionContent.isEmpty() || etQuestionSolution.isEmpty()) {
                    return;
                }
                HashMap<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("qid", question.getQid());
                paramsMap.put("questionContent", etQuestionContent.getContentText().toString());
                paramsMap.put("questionSolution", etQuestionSolution.getContentText().toString());
                paramsMap.put("courseName", msCourseName.getText().toString());
                paramsMap.put("grade", msGrade.getText().toString());
                paramsMap.put("questionDifficulty", msQuestionDifficulty.getText().toString());
                paramsMap.put("questionType", msQuestionType.getText().toString());
                if (!etQuestionAnalysis.isEmpty()) {
                    paramsMap.put("questionAnalysis", etQuestionAnalysis.getContentText().toString());
                }
                if (QuestionConstant.CHOICES.equals(msQuestionType.getSelectedItem().toString())) {
                    String[] c = etChoicesSelection.getContentText().toString().split("\n");
                    List<String> choices= Stream.of(c).collect(Collectors.toList());
                    paramsMap.put("choiceSelectionList", choices);
                }

                try {
                    RequestManager.getInstance().PutRequest(paramsMap, Constants.QUESTION,
                            new RequestManager.ResultCallback() {
                                @Override
                                public void onResponse(String responseCode, String response) {
                                    Type dataType = new TypeToken<Result>(){}.getType();
                                    Result result = JsonParse.getInstance().getResult(response, dataType);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(QuestionDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
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

    private void bigImageLoader(Bitmap bitmap){
        final Dialog dialog = new Dialog(QuestionDetailActivity.this);
        ImageView image = new ImageView(MyApp.getApplication());
        image.setScaleType(ImageView.ScaleType.FIT_CENTER);
        image.setImageBitmap(bitmap);
        dialog.setContentView(image);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dialog.cancel();
            }
        });
    }

    /**
     * 从相册获取图片
     */
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, DICM_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case DICM_REQUEST_CODE:    //调用相册后返回
                if (resultCode == RESULT_OK) {
                    //Log.i("DICM_REQUEST", "调用相册");
                    Uri uri = intent.getData();
                    ivImgUrl.setImageURI(uri);
                }
                break;
            default:
                break;
        }
    }

    public Bitmap getPicFromPath(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try
        {
            in = new BufferedInputStream(new URL(url).openStream(), 2*1024);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, 2*1024);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                copy(in, out);
            }
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    class UploadListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // 根据mHeader_iv获取图片
//            mHeader_iv.setDrawingCacheEnabled(true);
//            Bitmap drawingCache = mHeader_iv.getDrawingCache();
//            ivTemp.setImageBitmap(drawingCache);
//            mHeader_iv.setDrawingCacheEnabled(false);
            // 根据save()的cropPath获取图片
            Bitmap bitmap = getPicFromPath(cropPath);
            ivImgUrl.setImageBitmap(bitmap);
        }
    }
}