package com.yan.mywidgetsample;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by yanweiqiang on 2017/11/15.
 */

public class DividerDecoration extends RecyclerView.ItemDecoration {
    private final String tag = DividerDecoration.class.getSimpleName();
    Paint paint;

    public DividerDecoration() {
        super();
        paint = new Paint();
        paint.setTextSize(50);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //super.getItemOffsets(outRect, view, parent, state);
        Log.i(tag, "" + outRect.left + " " + outRect.top + " " + outRect.right + " " + outRect.bottom + " " + state);
        outRect.bottom = 10;
    }
}
