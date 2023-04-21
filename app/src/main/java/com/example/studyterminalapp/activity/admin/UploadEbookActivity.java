package com.example.studyterminalapp.activity.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.teacher.AddResourceActivity;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.FileSizeUtil;
import com.example.studyterminalapp.utils.ResourceConstant;
import com.github.gzuliyujiang.filepicker.ExplorerConfig;
import com.github.gzuliyujiang.filepicker.FilePicker;
import com.github.gzuliyujiang.filepicker.annotation.ExplorerMode;
import com.github.gzuliyujiang.filepicker.contract.OnFilePickedListener;
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.edittext.materialedittext.MaterialEditText;
import com.xuexiang.xui.widget.progress.HorizontalProgressView;
import com.xuexiang.xutil.common.StringUtils;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.File;

import io.reactivex.rxjava3.functions.Consumer;
import rxhttp.wrapper.entity.Progress;
import rxhttp.wrapper.param.RxHttp;

public class UploadEbookActivity extends AppCompatActivity implements OnFilePickedListener {
    private int textbookId;
    private RelativeLayout rlTitleBar;
    private TextView tvTitle;
    private ImageView ivBack;
    private File selectedFile;
    private Uri fileUri;
    private Button btnFilePick, btnUpload;
    private TextView tvFileName, tvFileSize, tvUploadMsg, tvProgress;
    private HorizontalProgressView hpvUpload;
    private LinearLayout llUploadProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_ebook);

        textbookId = getIntent().getIntExtra("textbookId", 0);

        initView();
        initListener();
    }

    private void initView() {
        rlTitleBar = findViewById(R.id.title_bar);
        rlTitleBar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tvTitle = findViewById(R.id.tv_title);
        ivBack = findViewById(R.id.iv_back);

        tvTitle.setText("上传资源");

        btnFilePick = (Button) findViewById(R.id.btn_file_pick);
        btnUpload = (Button) findViewById(R.id.btn_upload);
        tvFileName = (TextView) findViewById(R.id.tv_file_name);
        tvFileSize = (TextView) findViewById(R.id.tv_file_size);
        tvProgress = (TextView) findViewById(R.id.tv_progress);
        tvUploadMsg = (TextView) findViewById(R.id.tv_upload_msg);
        hpvUpload = (HorizontalProgressView) findViewById(R.id.hpv_upload);
        llUploadProgress = (LinearLayout) findViewById(R.id.ll_upload_progress);

        //hpvUpload.setVisibility(View.INVISIBLE);
        btnUpload.setVisibility(View.INVISIBLE);
        btnUpload.setEnabled(false);
        llUploadProgress.setVisibility(View.INVISIBLE);
        tvUploadMsg.setVisibility(View.INVISIBLE);
    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnFilePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExplorerConfig config = new ExplorerConfig(UploadEbookActivity.this);
                config.setRootDir(getExternalFilesDir(null));
                config.setLoadAsync(false);
                config.setExplorerMode(ExplorerMode.FILE);
                config.setOnFilePickedListener(UploadEbookActivity.this);
                FilePicker picker = new FilePicker(UploadEbookActivity.this);
                picker.setExplorerConfig(config);
                picker.show();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(UploadEbookActivity.this)
                        .content("是否要添加此资源？")
                        .positiveText("是")
                        .negativeText("否")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                uploadFile();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void onFilePicked(@NonNull File file) {
        //Toast.makeText(getApplicationContext(), file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        selectedFile = file;
        fileUri = getUriForFile(this, selectedFile);
        tvFileName.setText(file.getName());
        btnUpload.setVisibility(View.VISIBLE);
        btnUpload.setEnabled(true);

        llUploadProgress.setVisibility(View.INVISIBLE);
        tvUploadMsg.setVisibility(View.INVISIBLE);
    }

    // 获取真实Uri
    private static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.choosecrop.fileprovider", file);
            //uri = FileProvider.getUriForFile(context.getApplicationContext(), context.getPackageName()+".fileProvider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    // 上传文件
    private void uploadFile() {
        if (selectedFile == null || fileUri == null ) {
            ToastUtils.toast("没有选择文件");
            return;
        }

        //hpvUpload.setVisibility(View.VISIBLE);
        llUploadProgress.setVisibility(View.VISIBLE);
        hpvUpload.setProgress(0);
        String fileSizeMsg = getResources().getString(R.string.file_size) + FileSizeUtil.getAutoFileSize(selectedFile);
        tvFileSize.setText(fileSizeMsg);
        tvUploadMsg.setVisibility(View.VISIBLE);
        tvUploadMsg.setText(R.string.uploading_msg);
        RxHttp.postForm(Constants.WEB_SITE + Constants.UPLOAD_PDF)
                .add("tid", textbookId)
                .addPart(this, "file", fileUri)
                .toObservableString()
                .onMainProgress(new Consumer<Progress>() {
                    @Override
                    public void accept(Progress progress) throws Throwable {
                        int currentProgress = progress.getProgress();
                        long currentSize = progress.getCurrentSize();
                        long totalSize = progress.getTotalSize();
                        Log.e("Upload Progress", currentProgress + " | " + currentSize + " : " + totalSize);
                        tvProgress.setText(currentProgress + "%");
                        hpvUpload.setProgress(currentProgress);
                    }
                }).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        //ToastUtils.toast("上传成功");
                        Log.i("Upload Progress", "上传成功");
                        tvUploadMsg.setText(R.string.uploaded_msg);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        ToastUtils.toast("上传异常");
                        Log.e("Upload Progress", "上传异常");
                    }
                });
    }
}