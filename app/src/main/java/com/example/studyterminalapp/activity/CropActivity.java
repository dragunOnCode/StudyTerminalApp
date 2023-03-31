package com.example.studyterminalapp.activity;

import static android.os.Environment.DIRECTORY_DCIM;
import static android.os.FileUtils.copy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.studyterminalapp.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class CropActivity extends AppCompatActivity implements View.OnClickListener {

    private int id;
    private int userType;//0学生 1教师
    private Button btnUpload;
    private String cropPath;

    private ImageView mHeader_iv, ivTemp;

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
        setContentView(R.layout.activity_crop);
        id = getIntent().getIntExtra("id", 1);
        userType = getIntent().getIntExtra("type", 0);

        initView();
    }

    private void initView() {
        mHeader_iv = (ImageView) findViewById(R.id.mHeader_iv);
        ivTemp = (ImageView) findViewById(R.id.iv_temp);
        //Button mGoCamera_btn = (Button) findViewById(R.id.mGoCamera_btn);
        Button mGoAlbm_btn = (Button) findViewById(R.id.mGoAlbm_btn);
        btnUpload = (Button) findViewById(R.id.btn_upload);
        //mGoCamera_btn.setOnClickListener(this);
        mGoAlbm_btn.setOnClickListener(this);
        btnUpload.setOnClickListener(new UploadListener());
        btnUpload.setEnabled(false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.mGoCamera_btn:
//                getPicFromCamera();
//                break;
            case R.id.mGoAlbm_btn:
                getPicFromAlbm();
                btnUpload.setEnabled(true);
                break;
            default:
                break;
        }
    }


    /**
     * 从相机获取图片
     */
    private void getPicFromCamera() {
        //用于保存调用相机拍照后所生成的文件
        tempFile = new File(this.getExternalFilesDir(DIRECTORY_DCIM), System.currentTimeMillis() + ".png");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {   //如果在Android7.0以上,使用FileProvider获取Uri
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(CropActivity.this, "com.choosecrop.fileprovider", tempFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);

        } else {    //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        }
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    /**
     * 从相册获取图片
     */
    private void getPicFromAlbm() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//        photoPickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        photoPickerIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        photoPickerIntent.setType("image/*");
        //   photoPickerIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(photoPickerIntent, DICM_REQUEST_CODE);
    }


    /**
     * 裁剪图片
     */
    private void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        // Intent intent = new Intent("android.intent.action.EDIT");
        // intent.setAction("android.intent.action.EDIT");
        //  intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");

        //  intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // 避免拉伸
        intent.putExtra("outputX", 64);
        intent.putExtra("outputY", 64);
        intent.putExtra("return-data", false);
        File cropTemp = this.getExternalFilesDir(DIRECTORY_DCIM);
        File cropTempName = new File(cropTemp, System.currentTimeMillis() + "_crop_temp.png");
        Log.e("getPath", cropTempName.getAbsolutePath());

        Uri uriForFile = FileProvider.getUriForFile(this, "com.choosecrop.fileprovider", cropTempName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);

        grantPermissionFix(intent, uriForFile);

        startActivityForResult(intent, CROP_REQUEST_CODE);

    }

    private void grantPermissionFix(Intent intent, Uri uri) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setAction(null);
            intent.setComponent(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name));
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.e("d", "--------------222222222-------requestCode--: " + requestCode);
        switch (requestCode) {

            case CAMERA_REQUEST_CODE:   //调用相机后返回
                if (resultCode == RESULT_OK) {
                    //Log.i("CAMERA_REQUEST", "调用相册");
                    //用相机返回的照片去调用剪裁也需要对Uri进行处理
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = FileProvider.getUriForFile(CropActivity.this, "com.choosecrop.fileprovider", tempFile);
                        cropPhoto(contentUri);
                    } else {
                        cropPhoto(Uri.fromFile(tempFile));
                    }
                }
                break;
            case DICM_REQUEST_CODE:    //调用相册后返回
                if (resultCode == RESULT_OK) {
                    //Log.i("DICM_REQUEST", "调用相册");
                    Uri uri = intent.getData();
                    //mHeader_iv.setImageURI(uri);
                    if (null != uri)
                        cropPhoto(uri);
                    else {
                        Log.e("e", "null");
                    }
                }
                break;
            case CROP_REQUEST_CODE:     //调用剪裁后返回
                //Log.i("CROP_REQUEST", "调用裁剪");
                Log.e("d", "--------------222222222-------");
                if (null != intent) {
                    Log.e("d", "---------------------not null");
                    Uri data = intent.getData();
                    mHeader_iv.setImageURI(data);
//                    Bundle bundle = intent.getExtras();
//                    if (bundle != null) {
//                        Log.i("d", "bundle不为空");
//                        //在这里获得了剪裁后的Bitmap对象，可以用于上传
//                        Bitmap image = bundle.getParcelable("data");
//                        //设置到ImageView上
//                        mHeader_iv.setImageBitmap(image);
//                        //也可以进行一些保存、压缩等操作后上传
//                        cropPath = save("crop", image);
//                    }
                } else {
                    Log.e("d", "---------null");
                }
                break;
        }
    }

    public String save(String name, Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory().getPath());
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = name + ".png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
            ivTemp.setImageBitmap(bitmap);
        }
    }
}