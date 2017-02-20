package com.example.Lock;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class MyActivity extends Activity implements
        LockView.OnPatterChangeListener, View.OnClickListener {
    LinearLayout layout;
    EditText editText;
    Button updateView;
    LockView lockView;
    TextView textView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        layout = (LinearLayout) findViewById(R.id.layout);
        editText = (EditText) findViewById(R.id.count);
        updateView = (Button) findViewById(R.id.update);
        textView = (TextView) findViewById(R.id.text);

        updateView.setOnClickListener(this);
    }


    public void onPatterChange(String passwordStr) {
        this.textView.setText(passwordStr);
        Toast.makeText(this, passwordStr, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        if (view == updateView) {
            int count = 10;
            String countStr = editText.getText().toString();
            if (countStr != "") {
                count = Integer.valueOf(countStr);
            }

            if (lockView != null) {
                layout.removeView(lockView);
            }

            lockView = new LockView(this, count);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            lockView.setLayoutParams(params);
            layout.addView(lockView);
            lockView.setOnPatterChangeListener(this);
        }
    }
}
