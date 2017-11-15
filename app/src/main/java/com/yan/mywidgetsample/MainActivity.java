package com.yan.mywidgetsample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yan.mywidgetsample.activity.ViewAttachmentTest;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView mainRv = findViewById(R.id.main_rv);
        mainRv.setLayoutManager(new LinearLayoutManager(this));
        mainRv.setAdapter(new MyAdapter(getClassList()));
    }

    private List<Class> getClassList() {
        List<Class> classList = new ArrayList<>();
        classList.add(ViewAttachmentTest.class);
        return classList;
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        List<Class> classList;

        MyAdapter(List<Class> classList) {
            super();
            this.classList = classList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.display(classList.get(position));
        }

        @Override
        public int getItemCount() {
            return classList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv;

            ViewHolder(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.text);
            }

            void display(final Class cls) {
                tv.setText(cls.getSimpleName());
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(itemView.getContext(), cls);
                        itemView.getContext().startActivity(intent);
                    }
                });
            }
        }
    }
}
