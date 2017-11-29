package com.yan.mywidget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * yanweiqiang
 * 2017/11/29.
 */

public class PTRLLayout extends FrameLayout {
    private final String tag = PTRLLayout.class.getSimpleName();

    public PTRLLayout(@NonNull Context context) {
        super(context);
    }

    public PTRLLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 1) {
            throw new IllegalStateException("Support only one child!");
        }
        processView(getChildAt(0));
    }

    private void processView(View view) {
        if (view instanceof RecyclerView) {
            processRecycleView((RecyclerView) view);
        }
    }

    private void processRecycleView(RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.i(tag, "dx:" + dx + " dy:" + dy);
            }
        });
    }
}
