package com.yan.mywidget;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * yanweiqiang
 * 2017/11/22.
 */

/**
 * @param <T> you data type in adapter.
 */
public class RecyclerIndex<T> {
    private final String tag = RecyclerIndex.class.getSimpleName();
    private ViewAttachment viewAttachment;
    private IViewHolder<T> indexHolder;
    private RecyclerView recyclerView;
    private View preTitleView;
    private View.OnClickListener onClickListener;
    private T data;
    private OnIndexChangeListener<T> onIndexChangeListener;
    private int preIndexPos;

    public RecyclerIndex(RecyclerView recyclerView) {
        super();
        this.recyclerView = recyclerView;
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
            if (manager.getSpanCount() > 1) {
                throw new IllegalStateException("RecyclerIndex only support LinearLayoutManager!");
            }
        }
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public T getData() {
        return data;
    }

    public void setOnIndexChangeListener(OnIndexChangeListener<T> onIndexChangeListener) {
        this.onIndexChangeListener = onIndexChangeListener;
    }

    public List<T> getDataList() {
        return ((IAdapter<T>) recyclerView.getAdapter()).getDataList();
    }

    public void attachIndex() {
        viewAttachment = new ViewAttachment.Builder()
                .attachTo(recyclerView)
                .build();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                syncProcessScroll(false);
            }
        });
    }

    public void forceChangeIndex() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                syncProcessScroll(true);
            }
        });
    }

    private void syncProcessScroll(boolean isForce) {
        final LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
        final int position = llm.findFirstVisibleItemPosition();

        if (position == RecyclerView.NO_POSITION) {
            return;
        }

        View indexView = viewAttachment.getChild();
        View view = llm.findViewByPosition(position);
        RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
        View nextView = llm.findViewByPosition(position + 1);
        RecyclerView.ViewHolder nextViewHolder = recyclerView.getChildViewHolder(nextView);

        /**
         * A bux not fix, if app killed background by system, the indexHolder view cannot recreate in time,
         * you must swipe the RecyclerView until an title view appear on the top of it.
         */
        if (indexView == null) {
            if (viewHolder instanceof IViewHolder) {
                Log.i(tag, "init index view!");
                IViewHolder<T> temp = (IViewHolder<T>) viewHolder;
                indexHolder = initIndex(recyclerView, temp);
                indexHolder.getView().setOnClickListener(onClickListener);
            }
            return;
        }

        T data = getDataList().get(position);
        T nextData = getDataList().get(position + 1);

        //Add limit of indexHolder view refresh for performance.
        if (isForce || data instanceof IIndex || nextData instanceof IIndex) {
            for (int i = position; i >= 0; i--) {
                final T temp = getDataList().get(i);
                if (temp instanceof IIndex) {
                    if (preIndexPos == i) {
                        break;
                    }
                    preIndexPos = i;
                    RecyclerIndex.this.data = temp;
                    indexHolder.display(temp);
                    if (onIndexChangeListener != null) {
                        onIndexChangeListener.onChange(i, temp);
                    }
                    break;
                }
            }
        }

        if (nextViewHolder instanceof IViewHolder) {
            if (indexView.getBottom() >= nextView.getTop()) {
                indexView.setTop(nextView.getTop() - indexView.getBottom());
            }

            if (nextView.getVisibility() != View.VISIBLE) {
                nextView.setVisibility(View.VISIBLE);
            }
        } else if (viewHolder instanceof IViewHolder) {
            if (indexView.getTop() != 0) {
                indexView.setTop(0);
            }

            if (preTitleView != null) {
                preTitleView.setVisibility(View.VISIBLE);
            }
            preTitleView = view;
            preTitleView.setVisibility(View.INVISIBLE);
        } else {
            if (indexView.getTop() != 0) {
                indexView.setTop(0);
            }

            if (preTitleView == null || preTitleView.getVisibility() == View.VISIBLE) {
                return;
            }

            preTitleView.setVisibility(View.VISIBLE);
        }
    }


    private IViewHolder<T> initIndex(RecyclerView recyclerView, IViewHolder<T> indexHolder) {
        View view = indexHolder.getView();
        recyclerView.removeView(view);
        viewAttachment.changeChild(view);
        return indexHolder;
    }

    /**
     * The Adapter implement this interface.
     *
     * @param <T> data type same with {@link RecyclerIndex}.
     */
    public interface IAdapter<T> {
        //Get the data of your list that in adapter.
        List<T> getDataList();
    }

    /**
     * Index title viewHolder implement this interface.
     *
     * @param <T> data type same with {@link RecyclerIndex}.
     */
    public interface IViewHolder<T> {
        View getView();

        //The data of list item.
        T getData();

        /**
         * Change your view in this function in the {@link android.support.v7.widget.RecyclerView.ViewHolder}
         *
         * @param data data type same with {@link RecyclerIndex}.
         */
        void display(T data);
    }

    /**
     * Index title entity implement this interface.
     */
    public interface IIndex {

    }

    public interface OnIndexChangeListener<T> {
        void onChange(int position, T data);
    }
}
