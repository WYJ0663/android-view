package com.example.CircleWaitView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MyActivity extends Activity implements CircleWaitView.OnCircleWaitListener {

    CircleWaitView circleWaitView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        circleWaitView = (CircleWaitView) findViewById(R.id.circle);
        circleWaitView.setOnCircleWaitListener(this);
        circleWaitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                circleWaitView.start(3);
            }
        });
    }

    @Override
    public void onFinish() {
        Toast.makeText(this, "结束", Toast.LENGTH_SHORT).show();
    }

}
