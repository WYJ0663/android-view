package com.example.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Y轴
 * Created by WYJ on 2016/6/19.
 */
public class ScaleView extends View {

    private int textSize = 14;

    private Paint paint;

    private String[] valueY = {"0", "1", "2", "3", "4", "5"};//Y轴的值

    private int width;
    private int height;


    private List<Integer> value = new ArrayList<>();

    private int paddingX = 20;//X轴边距
    int paddingY;

    public ScaleView(Context context) {
        super(context);
        init();
    }

    public ScaleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true); // 消除锯齿
        paint.setStyle(Paint.Style.STROKE);

        textSize = MetricUtil.getDip(getContext(), textSize);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        drawYAxis(canvas);
    }

    private void drawYAxis(Canvas canvas) {
        paint.setTextSize(textSize);

        for (int i = 0; i < valueY.length; i++) {
            canvas.drawText(valueY[valueY.length - 1 - i], 0, i * paddingY + textSize, paint);
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);


        width = getWidth();
        height = getHeight();
        paddingY = (height - textSize) / (valueY.length - 1);
    }
}
