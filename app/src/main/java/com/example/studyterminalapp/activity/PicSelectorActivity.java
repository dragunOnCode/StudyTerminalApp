package com.example.studyterminalapp.activity;



import static com.xuexiang.xaop.consts.PermissionConsts.STORAGE;
import static com.xuexiang.xutil.app.IntentUtils.DocumentType.ANY;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.studyterminalapp.R;

import com.xuexiang.xaop.annotation.Permission;
import com.xuexiang.xui.widget.imageview.crop.CropImageType;
import com.xuexiang.xui.widget.imageview.crop.CropImageView;
import com.xuexiang.xutil.app.IntentUtils;
import com.xuexiang.xutil.app.PathUtils;
import com.xuexiang.xutil.common.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class PicSelectorActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * 选择系统图片Request Code
     */
    public static final int REQUEST_IMAGE = 112;

    CropImageView mCropImageView;
    Button btnSelect, btnRotate, btnCrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_selector);

        initView();
    }

    private void initView() {
        mCropImageView = (CropImageView) findViewById(R.id.crop_image_view);
        btnSelect = (Button) findViewById(R.id.btn_select);
        btnRotate = (Button) findViewById(R.id.btn_rotate);
        btnCrop = (Button) findViewById(R.id.btn_crop);

        mCropImageView.setGuidelines(CropImageType.CROPIMAGE_GRID_ON);
        mCropImageView.setFixedAspectRatio(true);
        mCropImageView.setAspectRatio(40,30);

        btnRotate.setEnabled(false);
        btnCrop.setEnabled(false);

        btnSelect.setOnClickListener(this);
        btnRotate.setOnClickListener(this);
        btnCrop.setOnClickListener(this);
    }


//    @OnClick({R.id.btn_select, R.id.btn_crop, R.id.btn_rotate})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btn_select:
//                Toast.makeText(this, "点击选择", Toast.LENGTH_SHORT).show();
//                selectImage();
//                break;
//            case R.id.btn_rotate:
//                Toast.makeText(this, "点击旋转", Toast.LENGTH_SHORT).show();
//                mCropImageView.rotateImage(90);
//                break;
//            case R.id.btn_crop:
//                Toast.makeText(this, "点击裁剪", Toast.LENGTH_SHORT).show();
//                mCropImageView.cropImage();
//                //使用getCroppedImage获取裁剪的图片
//
//                btnRotate.setEnabled(false);
//                btnCrop.setEnabled(false);
//                break;
//            default:
//                break;
//        }
//    }


    @Permission(STORAGE)
    private void selectImage() {
        Intent intent;
        if (Build.VERSION.SDK_INT <19) {
            System.out.println("输出ACTION_GET_CONTENT");
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }else {
            System.out.println("输出ACTION_OPEN_DOCUMENT");
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        }
        Intent i1;
        if (!StringUtils.isEmpty(IntentUtils.DocumentType.IMAGE)) {
            i1 = intent.setType(IntentUtils.DocumentType.IMAGE);
        } else {
            i1 = intent.setType(ANY);
        }
        startActivityForResult(i1, REQUEST_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("PicSelector", "进入回调");
        //选择系统图片并解析
        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK) {
            Log.i("PicSelector", "返回图片");
            if (data != null) {
                Log.i("PicSelector", "取得数据");
                Uri uri = data.getData();
                if (uri != null) {
                    String path = PathUtils.getFilePathByUri(uri);
                    System.out.println(path);
                    mCropImageView.setImagePath(PathUtils.getFilePathByUri(uri));
                    btnRotate.setEnabled(true);
                    btnCrop.setEnabled(true);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_select:
                Toast.makeText(this, "点击选择", Toast.LENGTH_SHORT).show();
                selectImage();
                break;
            case R.id.btn_rotate:
                Toast.makeText(this, "点击旋转", Toast.LENGTH_SHORT).show();
                mCropImageView.rotateImage(90);
                break;
            case R.id.btn_crop:
                Toast.makeText(this, "点击裁剪", Toast.LENGTH_SHORT).show();
                mCropImageView.cropImage();
                //使用getCroppedImage获取裁剪的图片

                btnRotate.setEnabled(false);
                btnCrop.setEnabled(false);
                break;
            default:
                break;
        }
    }
}