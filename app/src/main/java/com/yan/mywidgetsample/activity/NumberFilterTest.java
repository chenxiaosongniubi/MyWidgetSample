package com.yan.mywidgetsample.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.yan.mywidget.NumberFilter;
import com.yan.mywidgetsample.R;

import java.math.BigDecimal;

public class NumberFilterTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_filter);
        EditText et = findViewById(R.id.et);
        final NumberFilter filter = new NumberFilter();
        et.setFilters(new InputFilter[]{filter});

        EditText max = findViewById(R.id.max);
        NumberFilter maxFilter = new NumberFilter();
        maxFilter.setMaxValue(Double.MAX_VALUE);
        maxFilter.setPointLen(0);
        max.setFilters(new InputFilter[]{maxFilter});
        max.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    filter.setMaxValue(0);
                    return;
                }
                filter.setMaxValue(Double.parseDouble(s.toString()));
            }
        });

        EditText pointLen = findViewById(R.id.point_len);
        NumberFilter plFilter = new NumberFilter();
        plFilter.setMaxValue(Double.MAX_VALUE);
        plFilter.setPointLen(0);
        pointLen.setFilters(new InputFilter[]{plFilter});
        pointLen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    filter.setPointLen(0);
                    return;
                }
                filter.setPointLen(Integer.parseInt(s.toString()));
            }
        });
    }
}
