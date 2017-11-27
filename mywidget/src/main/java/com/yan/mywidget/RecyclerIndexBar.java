package com.yan.mywidget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * yanweiqiang
 * 2017/11/24.
 */

public class RecyclerIndexBar<T> extends RecyclerView {
    private List<T> indexList;
    private Map<Integer, Integer> posMap;
    private Map<Integer, Integer> reversePosMap;
    private RecyclerView recyclerView;
    private RecyclerIndex recyclerIndex;

    public RecyclerIndexBar(Context context) {
        super(context);
    }

    public RecyclerIndexBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        indexList = new ArrayList<>();
        posMap = new HashMap<>();
        reversePosMap = new HashMap<>();
    }

    public void transformData(List<T> list) {
        for (int i = 0; i < list.size(); i++) {
            T data = list.get(i);
            if (data instanceof IIndex) {
                indexList.add(data);
                posMap.put(i, indexList.size() - 1);
                reversePosMap.put(indexList.size() - 1, i);
            }
        }
    }

    public List<T> getIndexList() {
        return indexList;
    }

    public Map<Integer, Integer> getPosMap() {
        return posMap;
    }

    public Map<Integer, Integer> getReversePosMap() {
        return reversePosMap;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public RecyclerIndex getRecyclerIndex() {
        return recyclerIndex;
    }

    public void attach(RecyclerView recyclerView, RecyclerIndex recyclerIndex) {
        this.recyclerView = recyclerView;
        this.recyclerIndex = recyclerIndex;
    }

    public void selectPosition(int position) {
        ((IAdapter) getAdapter()).selectPosition(position);
    }

    public interface IAdapter {
        void selectPosition(int position);
    }

    public interface IIndex {
    }
}
