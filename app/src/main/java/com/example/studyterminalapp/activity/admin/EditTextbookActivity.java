package com.example.studyterminalapp.activity.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.student.EditStudentProfileActivity;
import com.example.studyterminalapp.bean.Textbook;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.RequestManager;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.edittext.ValidatorEditText;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class EditTextbookActivity extends AppCompatActivity {
    private Textbook textbook;
    private MaterialEditText etTextbookName, etTextbookAuthor, etPressName, etBookSn;
    private MaterialSpinner msCourseName, msGrade;
    private ValidatorEditText etTextbookPrice;
    private MultiLineEditText etTextbookDescription;
    private Button btnCancelCommit, btnCommitEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_textbook);

        textbook = (Textbook) getIntent().getSerializableExtra("textbook");

        initView();
    }

    private void initView() {
        etTextbookName = (MaterialEditText) findViewById(R.id.et_textbook_name);
        etTextbookAuthor = (MaterialEditText) findViewById(R.id.et_textbook_author);
        etPressName = (MaterialEditText) findViewById(R.id.et_press_name);
        etBookSn = (MaterialEditText) findViewById(R.id.et_book_sn);
        msCourseName = (MaterialSpinner) findViewById(R.id.ms_course_name);
        msGrade = (MaterialSpinner) findViewById(R.id.ms_grade);
        etTextbookPrice = (ValidatorEditText) findViewById(R.id.et_textbook_price);
        etTextbookDescription = (MultiLineEditText) findViewById(R.id.et_textbook_description);

        btnCancelCommit = (Button) findViewById(R.id.btn_cancel_commit);
        btnCommitEdit = (Button) findViewById(R.id.btn_commit_edit);

        etTextbookName.setText(textbook.getTextbookName());
        etTextbookAuthor.setText(textbook.getTextbookAuthor());
        etPressName.setText(textbook.getPressName());
        etBookSn.setText(textbook.getBookSn());
        msCourseName.setText(textbook.getCourseName());
        msGrade.setText(textbook.getGrade());
        etTextbookPrice.setText(String.valueOf(textbook.getTextbookPrice()));
        etTextbookDescription.setContentText(textbook.getTextbookDescription());

        btnCancelCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCommitEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etTextbookPrice.isEmpty() || !etTextbookPrice.validate()) {
                    return;
                }
                HashMap<String, Object> paramsMap = new HashMap<>();
                paramsMap.put("tid", textbook.getTid());
                if (!etTextbookName.isEmpty()) {
                    paramsMap.put("textbookName", etTextbookName.getText().toString());
                }
                paramsMap.put("courseName", msCourseName.getText().toString());
                paramsMap.put("grade", msGrade.getText().toString());
                if (!etTextbookAuthor.isEmpty()) {
                    paramsMap.put("textbookAuthor", etTextbookAuthor.getText().toString());
                }
                if (!etPressName.isEmpty()) {
                    paramsMap.put("pressName", etPressName.getText().toString());
                }
                paramsMap.put("textbookPrice", etTextbookPrice.getText().toString());
                if (!etTextbookDescription.isEmpty()) {
                    paramsMap.put("textbookDescription", etTextbookDescription.getContentText());
                }
                if (!etBookSn.isEmpty()) {
                    paramsMap.put("bookSn", etBookSn.getText().toString());
                }
                try {
                    RequestManager.getInstance().PutRequest(paramsMap, Constants.TEXTBOOK, new RequestManager.ResultCallback() {
                        @Override
                        public void onResponse(String responseCode, String response) {
                            int code = Integer.parseInt(responseCode);
                            switch (code) {
                                case 200:
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(EditTextbookActivity.this,
                                                    "更新教辅信息成功", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    break;
                                default:
                                    break;
                            }
                        }

                        @Override
                        public void onError(String msg) {
                            Log.i("Update Student Profile", msg);
                        }
                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });
    }
}