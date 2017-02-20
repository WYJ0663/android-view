package com.example.chart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * 曲线图
 * Created by WYJ on 2016/6/5.
 */
public class GraphView extends View {

    private int width;
    private int height;

    private int radius = 4;

    private List<Integer> values = new ArrayList<>();

    private List<Point> points = new ArrayList<>();


    int maxCount = 35;//最大点数
    int count = 6;
    private int paddingX = 40;//X轴边距
    private int paddingY = 0;
    private int margin = 10;

    private Paint paint;
    private Paint pointPaint;
    private Path path;
    private CornerPathEffect effect;

    private Handler handler = new Handler();
    private int time = 1000;
    private boolean startScroll = false;


    public GraphView(Context context) {
        super(context);
        init();
    }

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {


        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true); // 消除锯齿
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(MetricUtil.getDip(getContext(), 0.5f));


        pointPaint = new Paint();
        pointPaint.setColor(Color.WHITE);
        pointPaint.setAntiAlias(true); // 消除锯齿
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setStrokeWidth(MetricUtil.getDip(getContext(), 0.5f));


        path = new Path();
        effect = new CornerPathEffect(paddingX / 4);

        radius = MetricUtil.getDip(getContext(), radius);
        paddingX = MetricUtil.getDip(getContext(), paddingX);
        margin = MetricUtil.getDip(getContext(), margin);

        for (int i = 0; i < maxCount; i++) {
            int value = (int) (1 + Math.random() * 6);
            values.add(value);
            Log.i("debug", "value " + value);
        }


        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                int value = (int) (1 + Math.random() * 6);
                values.add(value);

                if (values.size() > maxCount) {
                    values.remove(0);
                }

                invalidate();
                startAnimator();
                handler.postDelayed(this, time);
            }
        };
        handler.postDelayed(runnable, time);

    }


    private void initPoint() {
        path.reset();
        points.clear();

        Point point = new Point(margin, (values.get(0) - 1) * paddingY + margin);
        points.add(point);
        path.moveTo(point.x, point.y);

        for (int i = 1; i < values.size(); i++) {
            Point point2 = new Point(i * paddingX + margin, (count - values.get(i)) * paddingY + margin);
            points.add(point2);
            path.lineTo(point2.x, point2.y);
        }
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        initPoint();
        paint.setPathEffect(effect);
        drawScrollLine(canvas);

        for (Point point : points) {
            canvas.drawCircle(point.x, point.y, radius * 2, pointPaint);//背景
            canvas.drawCircle(point.x, point.y, radius, paint);
        }


//        scrollTo(paddingX * (values.size() - 2) - width, 0);
    }

    //画曲线
    private void drawScrollLine(Canvas canvas) {
        for (int i = 0; i < points.size() - 1; i++) {
            Point startP = points.get(i);
            Point endP = points.get(i + 1);
            int wt = (startP.x + endP.x) / 2;

            Point p3 = new Point();//中间调整点
            Point p4 = new Point();
            p3.y = startP.y;
            p3.x = wt;
            p4.y = endP.y;
            p4.x = wt;


            Path path = new Path();
            path.moveTo(startP.x, startP.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endP.x, endP.y);
            canvas.drawPath(path, paint);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = getWidth() - margin * 2;
        height = getHeight() - margin * 2;
        paddingY = height / (count - 1);
        maxCount = width / paddingX + 5;
    }


    private ValueAnimator animator;

    private void startAnimator() {
        animator = ValueAnimator.ofInt(0, paddingX);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                scrollTo(paddingX * (values.size() - 2) - width + value, 0);
            }
        });

        animator.setDuration(time);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(0);
        animator.start();
    }


}
