package com.example.studyterminalapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class QuestionListView extends ListView {
    public QuestionListView(Context context) {
        super(context);
    }
    public QuestionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public QuestionListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
