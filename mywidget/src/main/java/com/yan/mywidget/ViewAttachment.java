package com.yan.mywidget;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;

/**
 * Created by yanweiqiang on 2017/11/14.
 */
public class ViewAttachment {
    private View child;

    private ViewAttachment(View child) {
        super();
        this.child = child;
    }

    private ViewAttachment(Context context, int layoutId) {
        super();
        this.child = LayoutInflater.from(context).inflate(layoutId, null);
    }

    private void attachTo(View view) {
        ViewParent viewParent = view.getParent();
        ViewGroup parent = (ViewGroup) viewParent;
        int index = parent.indexOfChild(view);

        parent.removeView(view);


        FrameLayout frameLayout = new FrameLayout(view.getContext());

        if (view.getLayoutParams() instanceof ConstraintLayout.LayoutParams) {
            frameLayout.setLayoutParams(new ConstraintLayout.LayoutParams((ConstraintLayout.LayoutParams) view.getLayoutParams()));
        } else if (view.getLayoutParams() instanceof TableLayout.LayoutParams) {
            frameLayout.setLayoutParams(new TableLayout.LayoutParams((TableLayout.LayoutParams) view.getLayoutParams()));
        } else {
            frameLayout.setLayoutParams(new ViewGroup.LayoutParams(view.getLayoutParams()));
        }

        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        frameLayout.addView(view);
        frameLayout.addView(child);

        parent.addView(frameLayout, index);
    }

    public View getChild() {
        return child;
    }

    public View findViewById(int resId) {
        return child.findViewById(resId);
    }

    public void hide() {
        child.setVisibility(View.GONE);
    }

    public void show() {
        child.setVisibility(View.VISIBLE);
    }

    public boolean isShowing() {
        return child.getVisibility() == View.VISIBLE;
    }

    public static class Builder {
        private View view;
        private View child;
        private View.OnClickListener onClickListener;
        private View.OnTouchListener onTouchListener;

        public Builder attachTo(View view) {
            this.view = view;
            return this;
        }

        public Builder child(View child) {
            this.child = child;
            return this;
        }

        public Builder child(Context context, int layoutId) {
            this.child = LayoutInflater.from(context).inflate(layoutId, null);
            return this;
        }

        public Builder onClickListener(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public Builder onTouchListener(View.OnTouchListener onTouchListener) {
            this.onTouchListener = onTouchListener;
            return this;
        }

        public ViewAttachment build() {
            if (child == null) {
                throw new IllegalStateException("Child can't be null!");
            }

            ViewAttachment viewAttachment = new ViewAttachment(child);
            viewAttachment.child.setOnClickListener(onClickListener);
            viewAttachment.child.setOnTouchListener(onTouchListener);

            if (view != null) {
                viewAttachment.attachTo(view);
            }

            return viewAttachment;
        }
    }
}