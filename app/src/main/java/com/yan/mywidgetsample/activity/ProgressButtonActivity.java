package com.yan.mywidgetsample.activity;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.yan.mywidget.ProgressButtonLayout;
import com.yan.mywidgetsample.R;

public class ProgressButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        }

        setContentView(R.layout.activity_progress_button);

        final ProgressButtonLayout pbl = findViewById(R.id.pbl);
        final ImageView cover = findViewById(R.id.cover);

        pbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pbl.showProgress();
                pbl.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cover.setVisibility(View.VISIBLE);
                        int sh = getResources().getDisplayMetrics().heightPixels;
                        pbl.setCoverResId(R.drawable.ic_cover);
                        pbl.showCover(new ProgressButtonLayout.CoverCallback() {
                            @Override
                            public void onCovered() {
                                Intent intent = new Intent(ProgressButtonActivity.this, IndexViewActivity.class);
                                startActivity(intent);
                            }
                        });

                        /*cover.animate().setDuration(1000).scaleX(sh / cover.getMeasuredWidth() * 2).scaleY(sh / cover.getMeasuredHeight() * 2).setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                Intent intent = new Intent(ProgressButtonActivity.this, IndexViewActivity.class);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation(ProgressButtonActivity.this);
                                    startActivity(intent, option.toBundle());
                                } else {
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        }).start();*/
                    }
                }, 2000);
            }
        });
    }
}
