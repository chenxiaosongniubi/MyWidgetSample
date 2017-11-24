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
    private IViewHolder<T> index;
    private RecyclerView recyclerView;
    private ExecutorService es;
    private View preTitleView;
    private View.OnClickListener onClickListener;
    private T data;
    private OnIndexChangeListener<T> onIndexChangeListener;
    private int preIndexPos;

    public RecyclerIndex(RecyclerView recyclerView) {
        super();
        this.recyclerView = recyclerView;
        es = Executors.newSingleThreadExecutor();

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
                syncProcessScroll();
            }
        });

        recyclerView.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                syncProcessScroll();
                Log.i(tag, "onChanged");
            }

            @Override
            public void onChanged() {
                super.onChanged();
                Log.i(tag, "onChanged");
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                Log.i(tag, "onChanged");
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                Log.i(tag, "onChanged");
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                Log.i(tag, "onChanged");
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                Log.i(tag, "onChanged");
            }
        });
    }

    public void forceChangeIndex(T data) {
        index.display(data);
        preTitleView.setVisibility(View.VISIBLE);
    }

    private void syncProcessScroll() {
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
         * A bux not fix, if app killed background by system, the index view cannot recreate in time,
         * you must swipe the RecyclerView until an title view appear on the top of it.
         */
        if (indexView == null) {
            if (viewHolder instanceof IViewHolder) {
                IViewHolder<T> temp = (IViewHolder<T>) viewHolder;
                index = initIndex(recyclerView, temp);
                index.getView().setOnClickListener(onClickListener);
            }
            return;
        }

        T data = getDataList().get(position);
        T nextData = getDataList().get(position + 1);

        //Log.i(tag, "position:" + position + " listSize:" + getDataList().size() + " data:" + data + " nextData:" + nextData);

        //Add limit of index view refresh for performance.
        if (data instanceof IIndex || nextData instanceof IIndex) {
            for (int i = position; i >= 0; i--) {
                final T temp = getDataList().get(i);
                if (temp instanceof IIndex) {
                    if (preIndexPos == i) {
                        break;
                    }
                    preIndexPos = i;
                    RecyclerIndex.this.data = temp;
                    index.display(temp);
                    if (onIndexChangeListener != null) {
                        onIndexChangeListener.onChange(i, temp);
                    }
                    break;
                }
            }
        }

        if (nextViewHolder instanceof IViewHolder) {
            //Log.i(tag, "indexViewBottom:" + indexView.getBottom() + " nextViewTop:" + nextView.getTop());

            if (nextView.getVisibility() != View.VISIBLE) {
                nextView.setVisibility(View.VISIBLE);
            }

            if (indexView.getBottom() > nextView.getTop()) {
                indexView.setTop(nextView.getTop() - indexView.getBottom());
            }
        } else if (viewHolder instanceof IViewHolder) {
            if (indexView.getTop() != 0) {
                indexView.setTop(0);
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


    private IViewHolder<T> initIndex(RecyclerView recyclerView, IViewHolder<T> index) {
        View view = index.getView();
        recyclerView.removeView(view);
        viewAttachment.changeChild(view);
        return index;
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
