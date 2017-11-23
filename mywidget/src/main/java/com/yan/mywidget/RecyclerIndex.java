package com.yan.mywidget;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.Stack;

/**
 * yanweiqiang
 * 2017/11/22.
 */

public class RecyclerIndex<T> {
    private final String tag = RecyclerIndex.class.getSimpleName();
    private ViewAttachment viewAttachment;
    private Index<T> index;

    public RecyclerIndex() {
        super();
    }

    public void attachIndex(RecyclerView recyclerView) {
        viewAttachment = new ViewAttachment.Builder()
                .attachTo(recyclerView)
                .build();

        final LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private Stack<Integer> posStack = new Stack<>();
            private Stack<T> dataStack = new Stack<>();

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int fp = llm.findFirstVisibleItemPosition();
                if (fp == RecyclerView.NO_POSITION) {
                    return;
                }
                View fv = llm.findViewByPosition(fp);
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(fv);

                //Log.i(tag, "fp:" + fp + " preP:" + (posStack.empty() ? "" : posStack.peek()));

                if (!posStack.empty() && fp <= posStack.peek()) {
                    Log.i(tag, "pop");
                    dataStack.pop();
                    T data = dataStack.peek();
                    posStack.pop();
                    index.display(data);
                    for (Integer integer : posStack) {
                        Log.i(tag, "pop::" + integer);
                    }
                } else {
                    if (viewHolder instanceof Index) {
                        Log.i(tag, "fp:" + fp);
                        Index<T> cIndex = (Index<T>) viewHolder;
                        if (index == null) {
                            index = initIndex(recyclerView, cIndex);
                        }
                        T data = (T) cIndex.getData();
                        index.display(data);
                        posStack.push(fp);
                        dataStack.push(data);
                    }
                }
            }
        });
    }

    private Index<T> initIndex(RecyclerView recyclerView, Index<T> index) {
        View view = index.getView();
        recyclerView.removeView(view);
        viewAttachment.changeChild(view);
        return index;
    }

    public interface Index<T> {
        View getView();

        T getData();

        void display(T data);
    }

    public interface DataCallback<T> {
        void onDataChanged(RecyclerView.ViewHolder holder, T data);
    }
}
