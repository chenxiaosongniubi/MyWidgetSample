package com.yan.mywidgetsample.activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.yan.mywidget.RecyclerIndex;
import com.yan.mywidgetsample.DividerDecoration;
import com.yan.mywidgetsample.R;
import com.yan.mywidgetsample.entity.Data;
import com.yan.mywidgetsample.entity.Index;
import com.yan.mywidgetsample.entity.ViewType;

import java.util.ArrayList;
import java.util.List;

public class IndexViewTest extends AppCompatActivity {
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Fade());
        }
        setContentView(R.layout.activity_index_view);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerDecoration());
        IndexViewAdapter adapter = new IndexViewAdapter(getViewTypeList());
        recyclerView.setAdapter(adapter);

        final RecyclerIndex<Index> index = new RecyclerIndex<>();
        index.attachIndex(recyclerView);
        index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), index.getData().getText(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private List<ViewType> getViewTypeList() {
        List<ViewType> viewTypeList = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            if (i % 20 == 0) {
                Index index = new Index();
                index.setText("index " + i / 10);
                viewTypeList.add(index);
                continue;
            }

            Data data = new Data();
            data.setViewType(1);
            data.setText("data " + i);
            viewTypeList.add(data);
        }

        return viewTypeList;
    }
}