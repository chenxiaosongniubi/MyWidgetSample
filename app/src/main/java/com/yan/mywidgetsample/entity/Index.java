package com.yan.mywidgetsample.entity;

/**
 * Created by yanweiqiang on 2017/11/15.
 */

public class Index implements ViewType {
    private int viewType;
    private String text;

    @Override
    public int getViewType() {
        return viewType;
    }

    @Override
    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
