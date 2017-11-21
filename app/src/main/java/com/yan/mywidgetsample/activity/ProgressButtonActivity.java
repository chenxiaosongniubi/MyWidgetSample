package com.yan.mywidgetsample.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yan.mywidget.ProgressButtonLayout;
import com.yan.mywidgetsample.R;

import java.text.MessageFormat;

public class ProgressButtonActivity extends AppCompatActivity {
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_button);
        count = getIntent().getIntExtra("count", 0) + 1;

        final ProgressButtonLayout pbl = findViewById(R.id.pbl);
        pbl.getButton().setText(MessageFormat.format("Enter ProgressButtonActivity << {0} >>", count));
        pbl.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbl.showProgress(500);
                /*pbl.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pbl.hideProgress();
                    }
                }, 1000);*/
                pbl.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pbl.showCover(getWindow().peekDecorView(), new ProgressButtonLayout.CoverCallback() {
                            @Override
                            public void onCovered() {
                                Intent intent = new Intent(ProgressButtonActivity.this, ProgressButtonActivity.class);
                                intent.putExtra("count", count);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(ProgressButtonActivity.this);
                                    startActivity(intent, option.toBundle());
                                } else {
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                                }
                                pbl.hideProgressImmediately();
                            }
                        });
                    }
                }, 2000);
            }
        });
    }
}