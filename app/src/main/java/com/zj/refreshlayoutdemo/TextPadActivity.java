package com.zj.refreshlayoutdemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

public class TextPadActivity extends Activity {

    private TextView tvPad;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_pad);
        String text = getIntent().getStringExtra("Text");
        tvPad = findViewById(R.id.tv_pad);
        tvPad.setText(text);

    }
}
