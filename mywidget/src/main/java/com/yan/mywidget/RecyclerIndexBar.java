package com.yan.mywidget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * yanweiqiang
 * 2017/11/24.
 */

public class RecyclerIndexBar<T> extends RecyclerView {
    private final String tag = RecyclerIndexBar.class.getSimpleName();

    private List<T> indexList;
    private SparseIntArray posMap;
    private SparseIntArray reversePosMap;
    private RecyclerView recyclerView;
    private RecyclerIndex recyclerIndex;

    public RecyclerIndexBar(Context context) {
        super(context);
    }

    public RecyclerIndexBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        indexList = new ArrayList<>();
        posMap = new SparseIntArray();
        reversePosMap = new SparseIntArray();
    }

    public void transformData(List<T> list) {
        indexList.clear();
        posMap.clear();
        reversePosMap.clear();

        if (list == null) {
            return;
        }

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

    public SparseIntArray getPosMap() {
        return posMap;
    }

    public SparseIntArray getReversePosMap() {
        return reversePosMap;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public RecyclerIndex getRecyclerIndex() {
        return recyclerIndex;
    }

    public void attach(RecyclerView recyclerView, RecyclerIndex<T> recyclerIndex) {
        this.recyclerView = recyclerView;
        this.recyclerIndex = recyclerIndex;
        recyclerIndex.setOnIndexChangeListener(new RecyclerIndex.OnIndexChangeListener<T>() {
            @Override
            public void onChange(int position, T data) {
                int barPos = getPosMap().get(position);
                selectPosition(barPos);
            }
        });
    }

    /**
     * Combine with {@link RecyclerIndex} for auto select in {@link RecyclerIndexBar}
     *
     * @param position
     */
    public void selectPosition(int position) {
        ((IAdapter) getAdapter()).selectPosition(position);
    }

    public interface IAdapter {
        void selectPosition(int position);
    }

    public interface IIndex {
    }
}
