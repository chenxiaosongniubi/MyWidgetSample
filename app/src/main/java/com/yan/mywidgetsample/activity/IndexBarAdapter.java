package com.yan.mywidgetsample.activity;

import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yan.mywidget.RecyclerIndexBar;
import com.yan.mywidgetsample.R;
import com.yan.mywidgetsample.entity.Index;
import com.yan.mywidgetsample.entity.ViewType;

import java.util.ArrayList;
import java.util.List;

/**
 * yanweiqiang
 * 2017/11/24.
 */

public class IndexBarAdapter extends RecyclerView.Adapter<IndexBarAdapter.ViewHolder> implements RecyclerIndexBar.IAdapter {
    private List<ViewType> indexList;
    private int selectPosition;
    private int prePosition;
    private RecyclerIndexBar<ViewType> recyclerIndexBar;
    private LinearLayoutManager linearLayoutManager;

    public IndexBarAdapter(RecyclerIndexBar<ViewType> recyclerIndexBar) {
        super();
        indexList = new ArrayList<>();
        this.recyclerIndexBar = recyclerIndexBar;
        linearLayoutManager = (LinearLayoutManager) recyclerIndexBar.getLayoutManager();
    }

    public void refresh(List<ViewType> indexList) {
        recyclerIndexBar.transformData(indexList);

        this.indexList.clear();
        if (indexList != null) {
            this.indexList.addAll(recyclerIndexBar.getIndexList());
        }
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_index_bar, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Index index = (Index) indexList.get(position);
        final int finalPos = position;
        holder.text.setText(index.getText());
        if (selectPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#44000000"));
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

        //Scroll to selected position, When index clicked.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int willSelPos = recyclerIndexBar.getReversePosMap().get(finalPos);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerIndexBar.getRecyclerView().getLayoutManager();
                linearLayoutManager.scrollToPositionWithOffset(willSelPos, 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return indexList.size();
    }

    @Override
    public void selectPosition(int position) {
        linearLayoutManager.scrollToPosition(position);
        selectPosition = position;
        notifyItemChanged(selectPosition);
        notifyItemChanged(prePosition);
        prePosition = position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }
}
