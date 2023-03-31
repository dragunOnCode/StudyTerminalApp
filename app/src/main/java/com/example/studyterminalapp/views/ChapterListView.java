package com.example.studyterminalapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ChapterListView extends ListView {
    public ChapterListView(Context context) {
        super(context);
    }
    public ChapterListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ChapterListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
