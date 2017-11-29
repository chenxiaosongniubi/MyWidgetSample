package com.yan.mywidgetsample.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.yan.mywidget.RecyclerIndex;
import com.yan.mywidget.RecyclerIndexBar;
import com.yan.mywidgetsample.DividerDecoration;
import com.yan.mywidgetsample.R;
import com.yan.mywidgetsample.entity.Index;
import com.yan.mywidgetsample.entity.ItemData;
import com.yan.mywidgetsample.util.DataUtil;

import java.util.List;

public class IndexBarTest extends AppCompatActivity {
    private final String tag = IndexBarTest.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index_bar_test);
        final List<ItemData> data = DataUtil.getItemDataList(555);

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerDecoration());
        IndexViewAdapter adapter = new IndexViewAdapter(data);
        recyclerView.setAdapter(adapter);

        final RecyclerIndex<ItemData> index = new RecyclerIndex<>(recyclerView);
        index.attachIndex();
        index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), ((Index) index.getData()).getText(), Toast.LENGTH_SHORT).show();
            }
        });

        final RecyclerIndexBar<ItemData> indexBar = findViewById(R.id.index_bar);
        indexBar.attach(recyclerView, index);
        indexBar.setLayoutManager(new LinearLayoutManager(this));
        IndexBarAdapter indexBarAdapter = new IndexBarAdapter(indexBar);
        indexBar.setAdapter(indexBarAdapter);
        indexBarAdapter.refresh(data);
    }
}
