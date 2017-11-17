package com.yan.mywidget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
    private int coverResId;

    private String buttonText;
    private int buttonWidth;
    private int progressWidth;
    private boolean isProgressShowing;

    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public ProgressButtonLayout(@NonNull Context context) {
        super(context);
    }

    public ProgressButtonLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressButtonLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < getChildCount(); i++) {
                    View view = getChildAt(i);
                    if (view instanceof TextView) {
                        button = (TextView) view;
                        buttonText = button.getText().toString();
                        buttonWidth = button.getMeasuredWidth();
                        button.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (onClickListener != null) {
                                    onClickListener.onClick(v);
                                }
                            }
                        });
                    } else {
                        progress = view;
                        progressWidth = progress.getMeasuredWidth();
                        progress.setVisibility(INVISIBLE);
                    }
                }
            }
        });
    }

    public void setCoverResId(int coverResId) {
        this.coverResId = coverResId;
    }

    public void showCover(final CoverCallback coverCallback) {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        if (windowManager == null) {
            return;
        }
        float density = getResources().getDisplayMetrics().density;
        float sh = getResources().getDisplayMetrics().heightPixels;
        int size = progressWidth;

        int[] location = new int[2];
        progress.getLocationInWindow(location);
        if (cover == null) {
            cover = new ImageView(getContext());
        }
        FrameLayout frameLayout = new FrameLayout(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size, size);
        cover.setLayoutParams(layoutParams);
        cover.setImageResource(coverResId);
        cover.setX(location[0]);
        cover.setY(location[1]);
        frameLayout.addView(cover);
        windowManager.addView(frameLayout, getWindowLayoutParams());
        cover.animate().setDuration(1000).scaleX(sh / size * 2).scaleY(sh / size * 2).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                coverCallback.onCovered();
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cover.animate().setDuration(1000).scaleX(0).scaleY(0).setListener(null).start();
                    }
                }, 2000);
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
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.START | Gravity.TOP;
        wmParams.x = 0;
        wmParams.y = 0;
        wmParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wmParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        return wmParams;
    }

    public void hideProgress() {
        if (!isProgressShowing) {
            return;
        }
        isProgressShowing = false;
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(500);
        valueAnimator.setIntValues(progressWidth, buttonWidth);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                button.getLayoutParams().width = value;
                button.requestLayout();
                if (value == buttonWidth) {
                    button.setText(buttonText);
                    button.setEnabled(true);
                }
            }
        });
        progress.setVisibility(GONE);
        valueAnimator.start();
    }

    public void showProgress() {
        if (isProgressShowing) {
            return;
        }
        isProgressShowing = true;
        button.setEnabled(false);
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(500);
        valueAnimator.setIntValues(buttonWidth, progressWidth);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                button.getLayoutParams().width = value;
                button.requestLayout();
                if (value == progressWidth) {
                    progress.setVisibility(VISIBLE);
                }
            }
        });
        button.setText(null);
        valueAnimator.start();
    }

    private int getAlphaColor(float percent, int color) {
        return ((int) (0xff * percent) << 24) & color;
    }
}