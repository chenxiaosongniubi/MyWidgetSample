package com.yan.mywidget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yanweiqiang on 2017/11/17.
 */

public class ProgressButtonLayout extends FrameLayout {
    private final String tag = ProgressButtonLayout.class.getSimpleName();
    private TextView button;
    private View progress;
    private ImageView cover;
    private int bgResId;
    private int coverResId;

    private Drawable buttonBg;
    private String buttonText;
    //    private int buttonWidth;
//    private int progressWidth;
    private boolean isProgressShowing;

    public ProgressButtonLayout(@NonNull Context context) {
        super(context);
    }

    public ProgressButtonLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressButtonLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressButtonLayout);
        bgResId = ta.getResourceId(R.styleable.ProgressButtonLayout_background, R.drawable.sc_btn);
        coverResId = ta.getResourceId(R.styleable.ProgressButtonLayout_cover, R.drawable.ic_cover);
        ta.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                button = (TextView) view;
                buttonBg = button.getBackground();
                buttonText = button.getText().toString();
            } else {
                progress = view;
                progress.setVisibility(INVISIBLE);
            }
        }
        /*post(new Runnable() {
            @Override
            public void run() {
                buttonWidth = button.getMeasuredWidth();
                progressWidth = progress.getMeasuredWidth();
            }
        });*/
    }

    public TextView getButton() {
        return button;
    }

    public View getProgress() {
        return progress;
    }

    public void setBgResId(int bgResId) {
        this.bgResId = bgResId;
    }

    public void setCoverResId(int coverResId) {
        this.coverResId = coverResId;
    }

    public void showCover(final CoverCallback coverCallback) {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return;
        }
        float sh = getResources().getDisplayMetrics().heightPixels;
        int pw = progress.getMeasuredWidth();
        int ph = progress.getMeasuredHeight();
        int[] location = new int[2];
        progress.getLocationInWindow(location);
        if (cover == null) {
            cover = new ImageView(getContext());
        }
        Log.i(tag, location[0] + ":" + location[1]);
        FrameLayout frameLayout = new FrameLayout(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(pw / 2, ph / 2);
        frameLayout.setBackgroundColor(0x44000000);
        cover.setLayoutParams(layoutParams);
        cover.setImageResource(coverResId);
        cover.setX(location[0] + (progress.getMeasuredWidth() - cover.getMeasuredWidth()) / 4);
        cover.setY(location[1] - (progress.getMeasuredHeight() - cover.getMeasuredHeight()) / 4);
        frameLayout.addView(cover);
        windowManager.addView(frameLayout, getWindowLayoutParams());
        cover.animate().setDuration(1000).scaleX(sh / pw * 4).scaleY(sh / ph * 4).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                coverCallback.onCovered();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    public interface CoverCallback {
        void onCovered();
    }

    private static WindowManager.LayoutParams getWindowLayoutParams() {
        WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.START | Gravity.TOP;
        wmParams.x = 0;
        wmParams.y = 0;
        wmParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wmParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        return wmParams;
    }

    public void showProgress() {
        if (isProgressShowing) {
            return;
        }
        isProgressShowing = true;
        button.setEnabled(false);
        button.setBackgroundResource(bgResId);
        final int bw = button.getMeasuredWidth();
        final int pw = progress.getMeasuredWidth();
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(500);
        valueAnimator.setIntValues(bw, pw);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                button.getLayoutParams().width = value;
                button.requestLayout();
                if (value == pw) {
                    progress.setVisibility(VISIBLE);
                }
            }
        });
        button.setText(null);
        valueAnimator.start();
    }

    public void hideProgress() {
        if (!isProgressShowing) {
            return;
        }
        isProgressShowing = false;
        final int bw = button.getMeasuredWidth();
        final int pw = progress.getMeasuredWidth();
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(500);
        valueAnimator.setIntValues(pw, bw);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                button.getLayoutParams().width = value;
                button.requestLayout();
                if (value == bw) {
                    button.setBackgroundDrawable(buttonBg);
                    button.setText(buttonText);
                    button.setEnabled(true);
                }
            }
        });
        progress.setVisibility(GONE);
        valueAnimator.start();
    }

    private int getAlphaColor(float percent, int color) {
        return ((int) (0xff * percent) << 24) & color;
    }
}
