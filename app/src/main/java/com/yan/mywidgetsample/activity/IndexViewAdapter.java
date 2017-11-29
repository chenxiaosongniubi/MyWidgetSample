package com.yan.mywidgetsample.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yan.mywidget.RecyclerIndex;
import com.yan.mywidgetsample.R;
import com.yan.mywidgetsample.entity.Data;
import com.yan.mywidgetsample.entity.Index;
import com.yan.mywidgetsample.entity.ItemData;

import java.util.List;

/**
 * Created by yanweiqiang on 2017/11/15.
 */

public class IndexViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RecyclerIndex.IAdapter<ItemData> {
    private final String tag = IndexViewAdapter.class.getSimpleName();
    private List<ItemData> itemDataList;

    public IndexViewAdapter(List<ItemData> itemDataList) {
        super();
        this.itemDataList = itemDataList;
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
        ItemData itemData = itemDataList.get(position);

        if (holder instanceof IndexHolder) {
            IndexHolder indexHolder = (IndexHolder) holder;
            indexHolder.display((Index) itemData);
        }

        if (holder instanceof DataHolder) {
            DataHolder dataHolder = (DataHolder) holder;
            Data data = (Data) itemData;
            dataHolder.text.setText(data.getText());
        }
    }

    @Override
    public int getItemCount() {
        return itemDataList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return itemDataList.get(position).getViewType();
    }

    @Override
    public List<ItemData> getDataList() {
        return itemDataList;
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
