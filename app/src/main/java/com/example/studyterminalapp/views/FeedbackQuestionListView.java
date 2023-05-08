package com.example.studyterminalapp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class FeedbackQuestionListView extends ListView {

    public FeedbackQuestionListView(Context context) {
        super(context);
    }
    public FeedbackQuestionListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public FeedbackQuestionListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
