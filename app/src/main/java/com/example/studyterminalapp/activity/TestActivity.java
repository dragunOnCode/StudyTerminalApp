package com.example.studyterminalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import com.example.studyterminalapp.R;

public class TestActivity extends AppCompatActivity {

    private RelativeLayout rlBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        rlBottomBar = findViewById(R.id.bottom_bar);
        Log.d("TAG", rlBottomBar.toString());
    }


}