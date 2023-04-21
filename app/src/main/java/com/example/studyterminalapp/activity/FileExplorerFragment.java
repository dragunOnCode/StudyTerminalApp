package com.example.studyterminalapp.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.studyterminalapp.R;
import com.github.gzuliyujiang.filepicker.ExplorerConfig;
import com.github.gzuliyujiang.filepicker.FileExplorer;
import com.github.gzuliyujiang.filepicker.annotation.ExplorerMode;

import java.io.File;
import java.util.Objects;

/**
 * @author :Reginer  2022/3/16 16:19.
 * 联系方式:QQ:282921012
 * 功能描述:文件浏览器弹窗
 */
public class FileExplorerFragment extends DialogFragment {

    @Override
    @SuppressLint("PrivateResource")
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStyle(STYLE_NORMAL, R.style.Theme_Design_Light_BottomSheetDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog()).getWindow().getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes(layoutParams);
        return inflater.inflate(R.layout.fragment_file_explorer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FileExplorer fileExplorer = view.findViewById(R.id.fileExplorer);
        ExplorerConfig config = new ExplorerConfig(requireContext());
        config.setRootDir(new File("sdcard"));
        config.setLoadAsync(true);
        config.setExplorerMode(ExplorerMode.FILE);
        config.setShowHomeDir(false);
        config.setShowUpDir(false);
        config.setShowHideDir(false);
        //config.setAllowExtensions(new String[]{".txt", ".jpg"});
        fileExplorer.load(config);
    }

}