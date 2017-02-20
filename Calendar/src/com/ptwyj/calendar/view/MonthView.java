package com.ptwyj.calendar.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WYJ on 2016/12/15.
 */
public class MonthView extends LinearLayout {

    private static final String week[] = {"日", "一", "二", "三", "四", "五", "六"};

    private Month month;

    List<View> lineViewList;
    List<TextView> itemViewList;

    public MonthView(Context context) {
        super(context);

        lineViewList = new ArrayList<>();
        itemViewList = new ArrayList<>();
        initView();
    }

    private void initView() {
        int lineMargins = MetricUtil.getDip(getContext(), 0.5f);

        //根布局
        setOrientation(VERTICAL);
        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, MetricUtil.getDip(getContext(), 300));
        setPadding(lineMargins, lineMargins, 0, 0);
        setLayoutParams(params);
        setBackgroundColor(Color.WHITE);

        //行
        LayoutParams lineParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);

        //item
        LayoutParams itemParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
        itemParams.setMargins(0, 0, lineMargins, lineMargins);

        //头部星期数
        LinearLayout headLayout = new LinearLayout(getContext());
        LayoutParams headParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        headLayout.setLayoutParams(headParams);
        headLayout.setOrientation(HORIZONTAL);
        addView(headLayout);

        for (int i = 0; i < 7; i++) {
            TextView itemView = new TextView(getContext());
            itemView.setTextColor(Color.RED);
            itemView.setTextSize(20);
            itemView.setGravity(Gravity.CENTER);
            itemView.setLayoutParams(itemParams);
            headLayout.addView(itemView);

            itemView.setText(week[i]);
        }

        //日历主体
        for (int i = 0; i < 6; i++) {
            LinearLayout lineLayout = new LinearLayout(getContext());
            lineLayout.setLayoutParams(lineParams);
            lineLayout.setOrientation(HORIZONTAL);
            addView(lineLayout);
            lineLayout.setVisibility(GONE);
            lineViewList.add(lineLayout);

            for (int j = 0; j < 7; j++) {
                TextView itemView = new TextView(getContext());
                itemView.setTextSize(20);
                itemView.setGravity(Gravity.CENTER);
                itemView.setLayoutParams(itemParams);
                lineLayout.addView(itemView);
                itemViewList.add(itemView);
//                itemView.setOnClickListener(this);
//                itemView.setOnLongClickListener(this);
            }
        }
    }

    public void update(int year, int m) {
        this.month = new Month(year, m);

        for (int i = 0; i < 6; i++) {
            View lineLayout = lineViewList.get(i);
            lineLayout.setVisibility(GONE);

            if (i < month.getLineSize()) {
                lineLayout.setVisibility(VISIBLE);
            }
        }
        for (int i = 0; i < month.getAllDdays().size(); i++) {
            TextView itemView = itemViewList.get(i);
            int day = month.getAllDdays().get(i);

            itemView.setText(day + "");
            if (month.isThisMonth(i)) {
                itemView.setTextColor(Color.BLACK);
            } else {
                itemView.setTextColor(Color.GRAY);
            }
        }
    }
}
