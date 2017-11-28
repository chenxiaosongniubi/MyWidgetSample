package com.yan.mywidget;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.List;

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
    private View preTitleView;
    private int preIndexPos;
    private T data;

    private RecyclerView recyclerView;
    private View.OnClickListener onClickListener;
    private OnIndexChangeListener<T> onIndexChangeListener;

    public RecyclerIndex(RecyclerView recyclerView) {
        super();
        this.recyclerView = recyclerView;
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
        if (recyclerView == null) {
            return null;
        }
        return ((IAdapter<T>) recyclerView.getAdapter()).getDataList();
    }

    public void attachIndex() {
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
            if (manager.getSpanCount() > 1) {
                throw new IllegalStateException("RecyclerIndex only support LinearLayoutManager!");
            }
        }

        viewAttachment = new ViewAttachment.Builder()
                .attachTo(recyclerView)
                .build();

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                syncProcessScroll();
            }
        });
    }

    public void reset() {
        indexHolder = null;
        if (viewAttachment != null) {
            viewAttachment.removeChild();
        }
        if (preTitleView != null) {
            preTitleView.setVisibility(View.VISIBLE);
        }
        preIndexPos = 0;
        data = null;
    }

    private void syncProcessScroll() {
        final LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();
        final int position = llm.findFirstVisibleItemPosition();
        final int lastPosition = llm.findLastVisibleItemPosition();

        if (position == RecyclerView.NO_POSITION) {
            return;
        }

        View indexView = viewAttachment.getChild();
        View view = llm.findViewByPosition(position);
        RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
        View nextView = llm.findViewByPosition(position + 1);
        RecyclerView.ViewHolder nextViewHolder = recyclerView.getChildViewHolder(nextView);

        /*
         * A bux not fix, if app killed background by system, the indexHolder view cannot recreate in time,
         * you must swipe the RecyclerView until an title view appear on the top of it.
         */
        if (indexView == null) {
            if (viewHolder instanceof IViewHolder) {
                IViewHolder<T> temp = (IViewHolder<T>) viewHolder;
                indexHolder = createIndexHolder(recyclerView, temp);
                indexHolder.getView().setOnClickListener(onClickListener);
            }
            return;
        }

        boolean flag = false;
        for (int i = position; i < lastPosition; i++) {
            T data = getDataList().get(i);
            if (data instanceof IIndex) {
                flag = true;
                break;
            }
        }

        if (flag) {
            for (int i = position; i >= 0; i--) {
                final T temp = getDataList().get(i);
                if (temp instanceof IIndex) {
                    if (preIndexPos == i) {
                        break;
                    }
                    preIndexPos = i;
                    RecyclerIndex.this.data = temp;
                    indexHolder.display(temp);

                    if (preTitleView != null) {
                        preTitleView.setVisibility(View.VISIBLE);
                    }

                    if (onIndexChangeListener != null) {
                        onIndexChangeListener.onChange(i, temp);
                    }
                    break;
                }
            }
        }

        if (viewHolder instanceof IViewHolder) {
            if (indexView.getTranslationY() < 0) {
                indexView.setTranslationY(0);
            }

            view.setVisibility(View.INVISIBLE);
            preTitleView = view;
        } else if (nextViewHolder instanceof IViewHolder) {
            if (preTitleView != null) {
                preTitleView.setVisibility(View.VISIBLE);
            }

            if (nextView.getTop() > 0) {
                int ty = nextView.getTop() - indexView.getMeasuredHeight();
                ty = Math.min(ty, 0);
                indexView.setTranslationY(ty);
            }
        } else {
            if (preTitleView != null) {
                preTitleView.setVisibility(View.VISIBLE);
            }

            if (indexView.getTranslationY() < 0) {
                indexView.setTranslationY(0);
            }
        }
    }

    private IViewHolder<T> createIndexHolder(RecyclerView recyclerView, IViewHolder<T> indexHolder) {
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
