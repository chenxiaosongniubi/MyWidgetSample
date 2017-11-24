package com.yan.mywidgetsample.activity;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yan.mywidget.RecyclerIndex;
import com.yan.mywidget.RecyclerIndexBar;
import com.yan.mywidgetsample.R;
import com.yan.mywidgetsample.entity.Data;
import com.yan.mywidgetsample.entity.Index;
import com.yan.mywidgetsample.entity.ViewType;

import java.util.List;

/**
 * Created by yanweiqiang on 2017/11/15.
 */

public class IndexViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerIndex.IAdapter<ViewType> {
    private final String tag = IndexViewAdapter.class.getSimpleName();
    private List<ViewType> viewTypeList;

    public IndexViewAdapter(List<ViewType> viewTypeList) {
        super();
        this.viewTypeList = viewTypeList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new IndexHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index, parent, false));
        }

        return new DataHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewType viewType = viewTypeList.get(position);

        if (holder instanceof IndexHolder) {
            IndexHolder indexHolder = (IndexHolder) holder;
            indexHolder.display((Index) viewType);
        }

        if (holder instanceof DataHolder) {
            DataHolder dataHolder = (DataHolder) holder;
            Data data = (Data) viewType;
            dataHolder.text.setText(data.getText());
        }
    }

    @Override
    public int getItemCount() {
        return viewTypeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return viewTypeList.get(position).getViewType();
    }

    @Override
    public List<ViewType> getDataList() {
        return viewTypeList;
    }

    class IndexHolder extends RecyclerView.ViewHolder implements RecyclerIndex.IViewHolder<Index> {
        TextView text;

        Index data;

        public IndexHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }

        @Override
        public View getView() {
            return itemView;
        }

        @Override
        public Index getData() {
            return data;
        }

        @Override
        public void display(Index data) {
            this.data = data;
            text.setText(data.getText());
        }
    }

    class DataHolder extends RecyclerView.ViewHolder {
        TextView text;

        public DataHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }
}
