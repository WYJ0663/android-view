package com.example.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ptwyj.calendar.view.MonthView;

public class MyActivity extends Activity implements View.OnClickListener {
    TextView leftView;
    TextView rightView;
    TextView titleView;
    LinearLayout mainLayout;
    MonthView monthView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        leftView = (TextView) findViewById(R.id.left);
        rightView = (TextView) findViewById(R.id.right);
        titleView = (TextView) findViewById(R.id.title);
        mainLayout = (LinearLayout) findViewById(R.id.main_layout);

        monthView = new MonthView(this);
        mainLayout.addView(monthView);

        leftView.setOnClickListener(this);
        rightView.setOnClickListener(this);

        monthView.update(year, month);
    }

    private int year = 2016;
    private int month = 12;

    @Override
    public void onClick(View view) {
        if (view == leftView) {
            getYM(false);
            monthView.update(year, month);
        } else if (view == rightView) {
            getYM(true);
            monthView.update(year, month);
        }

        titleView.setText(year + "-" + month);
    }

    private void getYM(boolean isNext) {
        if (isNext) {
            if (month == 12) {
                month = 1;
                year++;
            } else {
                month++;
            }
        } else {
            if (month == 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
        }

    }
}
