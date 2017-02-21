package com.example.HeaderListView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.qq:
                startActivity(new Intent(this, QQActivity.class));
                break;
            case R.id.wx:
                startActivity(new Intent(this, WXActivity.class));
                break;
        }
    }
}
