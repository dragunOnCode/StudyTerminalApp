package com.example.studyterminalapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.studyterminalapp.R;

public class ClassDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_detail);

        Toast.makeText(this, "Welcome to detail page", Toast.LENGTH_SHORT).show();
    }
}