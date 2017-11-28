package com.yan.mywidget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by yanweiqiang on 2017/11/14.
 */
public class ViewAttachment {
    private FrameLayout attachmentRoot;
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
        attachmentRoot = new FrameLayout(view.getContext());
        attachmentRoot.setLayoutParams(view.getLayoutParams());
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        attachmentRoot.addView(view);
        if (child != null) {
            attachmentRoot.addView(child);
        }
        parent.addView(attachmentRoot, index);
    }

    public void changeChild(View newChildView) {
        if (child != null) {
            attachmentRoot.removeView(child);
        }
        attachmentRoot.addView(newChildView);
        child = newChildView;
        child.requestLayout();
    }

    public View getChild() {
        return child;
    }

    public void removeChild() {
        attachmentRoot.removeView(child);
        child = null;
    }

    public View findViewById(int resId) {
        return child.findViewById(resId);
    }

    public void hide() {
        if (child == null) {
            return;
        }

        child.setVisibility(View.GONE);
    }

    public void show() {
        if (child == null) {
            return;
        }

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
            ViewAttachment viewAttachment = new ViewAttachment(child);
            if (child != null) {
                viewAttachment.child.setOnClickListener(onClickListener);
                viewAttachment.child.setOnTouchListener(onTouchListener);
            }
            if (view != null) {
                viewAttachment.attachTo(view);
            }

            return viewAttachment;
        }
    }
}