package com.yan.mywidgetsample.activity;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yan.mywidgetsample.R;
import com.yan.mywidgetsample.entity.Data;
import com.yan.mywidgetsample.entity.Index;
import com.yan.mywidgetsample.entity.ViewType;

import java.util.List;

/**
 * Created by yanweiqiang on 2017/11/15.
 */

public class IndexViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final String tag = IndexViewAdapter.class.getSimpleName();
    private List<ViewType> viewTypeList;

    public IndexViewAdapter(List<ViewType> viewTypeList) {
        super();
        this.viewTypeList = viewTypeList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(tag, "onCreateViewHolder");
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
            Index index = (Index) viewType;
            indexHolder.text.setText(index.getText());
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

    class IndexHolder extends RecyclerView.ViewHolder {
        TextView text;

        public IndexHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
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
