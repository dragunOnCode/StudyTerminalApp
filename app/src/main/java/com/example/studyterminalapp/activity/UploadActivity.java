package com.example.studyterminalapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.studyterminalapp.R;
import com.example.studyterminalapp.activity.teacher.TeacherClassActivity;
import com.example.studyterminalapp.bean.Class;
import com.example.studyterminalapp.bean.Result;
import com.example.studyterminalapp.bean.vo.PageInfo;
import com.example.studyterminalapp.utils.Constants;
import com.example.studyterminalapp.utils.JsonParse;
import com.example.studyterminalapp.utils.MyMultipartBody;
import com.example.studyterminalapp.utils.UploadProgressListener;
import com.github.gzuliyujiang.filepicker.ExplorerConfig;
import com.github.gzuliyujiang.filepicker.FileExplorer;
import com.github.gzuliyujiang.filepicker.FilePicker;
import com.github.gzuliyujiang.filepicker.annotation.ExplorerMode;
import com.github.gzuliyujiang.filepicker.contract.OnFilePickedListener;
import com.google.gson.reflect.TypeToken;
import com.xuexiang.xui.widget.progress.HorizontalProgressView;
import com.xuexiang.xutil.tip.ToastUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.URLConnection;

import io.reactivex.rxjava3.functions.Consumer;
import lombok.val;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import rxhttp.wrapper.entity.Progress;
import rxhttp.wrapper.param.RxHttp;

public class UploadActivity extends AppCompatActivity implements OnFilePickedListener {
    private Button btnUpload;
    private ImageView ivSelect;
    private File uploadFile;
    private Uri fileUri;
    private HorizontalProgressView hpvUpload;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
//        FileExplorer fileExplorer = findViewById(R.id.file_picker_explorer);
//        ExplorerConfig config = new ExplorerConfig(this);
//        config.setRootDir(Environment.getExternalStorageDirectory());
//        config.setLoadAsync(true);
//        config.setExplorerMode(ExplorerMode.FILE);
//        config.setShowHomeDir(true);
//        config.setShowUpDir(true);
//        config.setShowHideDir(true);
//        config.setAllowExtensions(new String[]{".txt", ".jpg", ".png"});
//        fileExplorer.load(config);

        ivSelect = (ImageView) findViewById(R.id.iv_select);
        btnUpload = (Button) findViewById(R.id.btn_upload);
        hpvUpload = (HorizontalProgressView) findViewById(R.id.hpv_upload);
        hpvUpload.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFilePicked(@NonNull File file) {
        Toast.makeText(getApplicationContext(), file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        Glide.with(getApplicationContext())
                .load(file)
                .error(R.mipmap.ic_launcher)
                .into(ivSelect);
        uploadFile = file;
        //fileUri = getImageContentUri(this, file);
        fileUri = getUriForFile(this, uploadFile);
    }

    public void onPermission(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                Toast.makeText(getApplicationContext(), "isExternalStorageManager==true", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivity(intent);
            }
            return;
        }
        Toast.makeText(getApplicationContext(), "当前系统版本不支持文件管理权限", Toast.LENGTH_SHORT).show();
    }

    public void onFilePick(View view) {
        ExplorerConfig config = new ExplorerConfig(this);
        config.setRootDir(getExternalFilesDir(null));
        config.setLoadAsync(false);
        config.setExplorerMode(ExplorerMode.FILE);
        config.setOnFilePickedListener(UploadActivity.this);
        FilePicker picker = new FilePicker(this);
        picker.setExplorerConfig(config);
        picker.show();
    }

    public void onDirPick(View view) {
        ExplorerConfig config = new ExplorerConfig(this);
        config.setRootDir(getFilesDir());
        config.setLoadAsync(false);
        config.setExplorerMode(ExplorerMode.DIRECTORY);
        config.setOnFilePickedListener(this);
        FilePicker picker = new FilePicker(this);
        picker.setExplorerConfig(config);
        picker.show();
    }

    public void onDialogPick(View view) {
        new FileExplorerFragment().show(getSupportFragmentManager(), getClass().getName());
    }

    public void onUploadClick(View view) {
        uploadFile();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * Gets the content:// URI from the given corresponding path to a file
     *
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(MediaStore.MediaColumns._ID);
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


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

    private void uploadFile2() {
        //File file = new File(Environment.getExternalStorageDirectory(), "test.png");
        if (uploadFile == null) {
            ToastUtils.toast("没有选择文件");
            return;
        }

        //1.1创建okHttpClient
        OkHttpClient httpClient = new OkHttpClient();

        //1.2创建RequestBody对象(MultipartBody是继承RequestBody)
        MultipartBody.Builder builder = new MultipartBody
                .Builder()
                .setType(MultipartBody.FORM);

        builder.addFormDataPart("platform", "android");
//        builder.addFormDataPart("file", uploadFile.getName(),
//                RequestBody.create(MediaType.parse(guessMimeType(uploadFile.getAbsolutePath())), uploadFile));

        builder.addFormDataPart("file", uploadFile.getName());

        //MediaType mediaType = MediaType.parse("multipart/form-data; charset=utf-8");

        //Log.i("Upload Type", mediaType.toString());


        //1.3包装MultipartBody成MyMultipartBody
        MyMultipartBody myMultipartBody = new MyMultipartBody(builder.build(), new UploadProgressListener() {
            @Override
            public void onProgress(long total, long current) {
                //回调接口打印总进度和当前进度
                Log.e("Upload Progress", total + " : " + current);
            }
        });

        //1.4创建Request对象
        Request request = new Request.Builder()
                .url(Constants.WEB_SITE + Constants.UPLOAD_TEST)
                .post(myMultipartBody)
                .build();

        //2.把Request对象封装成call对象
        Call call = httpClient.newCall(request);

        //3.发起异步请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response r) throws IOException {
                String json = r.body().string();
                //Log.e("TAG", json);
                Type dataType = new TypeToken<Result>(){}.getType();
                Result response = JsonParse.getInstance().getResult(json, dataType);
                int code = response.getStatus();
                switch (code) {
                    case 200:
                        //Log.d("TEST", "JSON: " + json);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("Upload Success", response.getMsg());
                            }
                        });
                        break;
                    default:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("Upload Fail", response.getMsg());
                            }
                        });
                        break;
                }
            }
        });
    }

    private void uploadFile() {
        if (uploadFile == null || fileUri == null) {
            ToastUtils.toast("没有选择文件");
            return;
        }

        hpvUpload.setVisibility(View.VISIBLE);
        hpvUpload.setProgress(0);
        RxHttp.postForm(Constants.WEB_SITE + Constants.UPLOAD_TEST)
                .add("name", "lucas")
                .addPart(this, "file", fileUri)
                .toObservableString()
                .onMainProgress(new Consumer<Progress>() {
                    @Override
                    public void accept(Progress progress) throws Throwable {
                        int currentProgress = progress.getProgress();
                        long currentSize = progress.getCurrentSize();
                        long totalSize = progress.getTotalSize();
                        Log.e("Upload Progress", currentProgress + " | " + currentSize + " : " + totalSize);
                        hpvUpload.setProgress(currentProgress);
                    }
                }).subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Throwable {
                        ToastUtils.toast("上传成功");
                        Log.i("Upload Progress", "上传成功");
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