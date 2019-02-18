package com.zj.refreshlayoutdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TestRefreshLayoutActivity extends AppCompatActivity {

    private ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.lv_test);

        String[] strs = {
                "The",
                "Canvas",
                "class",
                "holds",
                "the",
                "draw",
                "calls",
                ".",
                "To",
                "draw",
                "something,",
                "you",
                "need",
                "4 basic",
                "components",
                "Bitmap",
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs);
        mListView.setAdapter(adapter);


    }

}
