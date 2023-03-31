package com.example.studyterminalapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class StudentListView extends ListView {
    public StudentListView(Context context) {
        super(context);
    }
    public StudentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public StudentListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
