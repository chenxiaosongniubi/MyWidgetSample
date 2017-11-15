package com.yan.mywidgetsample.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.yan.mywidget.ViewAttachment;
import com.yan.mywidgetsample.R;

public class ViewAttachmentTest extends AppCompatActivity {
    ViewAttachment viewAttachment;
    View vaLyt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attachment_test);
        vaLyt = findViewById(R.id.va_lyt);
        Button vaSwitch = findViewById(R.id.va_switch);

        viewAttachment = initViewAttachment();

        vaSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewAttachment.isShowing()) {
                    viewAttachment.hide();
                } else {
                    viewAttachment.show();
                }
            }
        });
    }

    private ViewAttachment initViewAttachment() {
        return new ViewAttachment.Builder()
                .attachTo(vaLyt)
                .child(this, R.layout.layout_attachment)
                .onClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .build();
    }
}
