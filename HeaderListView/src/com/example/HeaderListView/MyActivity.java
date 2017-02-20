package com.example.HeaderListView;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends Activity {


    private HeaderListView lv;
    private List<String> list;
    private LvAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        lv = (HeaderListView) findViewById(R.id.list_view);
        list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add("我们都是开发者" + i);
        }
        adapter = new LvAdapter(list, this);
        lv.setAdapter(adapter);

    }
}
