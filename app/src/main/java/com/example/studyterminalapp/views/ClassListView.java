package com.example.studyterminalapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ClassListView extends ListView {
    public ClassListView(Context context) {
        super(context);
    }
    public ClassListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ClassListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
