package com.yan.mywidgetsample.activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.view.Window;

import com.yan.mywidget.RecyclerIndex;
import com.yan.mywidgetsample.DividerDecoration;
import com.yan.mywidgetsample.R;
import com.yan.mywidgetsample.entity.Data;
import com.yan.mywidgetsample.entity.Index;
import com.yan.mywidgetsample.entity.ViewType;

import java.util.ArrayList;
import java.util.List;

public class IndexViewActivity extends AppCompatActivity {
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Fade());
        }
        setContentView(R.layout.activity_index_view);
        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerDecoration());
        IndexViewAdapter adapter = new IndexViewAdapter(getViewTypeList());
        rv.setAdapter(adapter);

        new RecyclerIndex().attachIndex(rv);
    }

    private List<ViewType> getViewTypeList() {
        List<ViewType> viewTypeList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            if (i % 10 == 0) {
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