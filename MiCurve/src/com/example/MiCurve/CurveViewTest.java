package com.example.MiCurve;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * 小米录音的曲线
 * Created by WYJ on 2016/11/9.
 */
public class CurveViewTest extends View {
    private int color = 0x55189DAE;

    private int width;
    private int height;

    private Paint paint;
    private Path path;

    private List<Point> points = new ArrayList<>();

    private Handler handler = new Handler();
    private int time = 1000;


    private int maxHeight = 50;//曲线的最大高度
    private Point startPoint;
    private Point endPoint;

    public CurveViewTest(Context context) {
        super(context);
        init();
    }

    public CurveViewTest(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurveViewTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        maxHeight = (int) MetricUtil.getDip(getContext(), maxHeight);

        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true); // 消除锯齿
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(MetricUtil.getDip(getContext(), 2f));
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        changePoint(0);
        drawScrollLine(canvas);

    }

    //画曲线
    private void drawScrollLine(Canvas canvas) {


        // 通过画折线和贝塞尔曲线可以知道，点得位置是不一样的。
        // 画折线
        paint.setColor(Color.RED);
        for (int i = 0; i < points.size() - 1; i++) {
            canvas.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y, paint);
            canvas.drawCircle(points.get(i).x, points.get(i).y, 3, paint);
        }


        Path p = new Path();

        // 设置第一个控制点33%的距离
        float mFirstMultiplier = 0.5f;
        // 设置第二个控制点为66%的距离
        float mSecondMultiplier = 1 - mFirstMultiplier;

        p.moveTo(points.get(0).x, points.get(0).y);
        for (int i = 0; i < points.size(); i += 1) {
            int nextIndex = i + 1 < points.size() ? i + 1 : i;
            int nextNextIndex = i + 2 < points.size() ? i + 2 : nextIndex;

            // 设置第一个控制点
            Point p1 = calc(points.get(i), points.get(nextIndex), mSecondMultiplier);
            // 设置第二个控制点
            Point p2 = points.get(nextIndex);
            // 设置第二个控制点
            Point p3 = calc(points.get(nextIndex), points.get(nextNextIndex), mFirstMultiplier);
            // 最后一个点就是赛贝尔曲线上的点
            p.cubicTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
            // 画点


            if (i == 0) {
                paint.setColor(Color.BLUE);
                canvas.drawCircle(p1.x, p1.y, 6, paint);
                paint.setColor(Color.GREEN);
                canvas.drawCircle(p2.x, p2.y, 6, paint);
                paint.setColor(Color.RED);
                canvas.drawCircle(p3.x, p3.y, 6, paint);
            }
        }
        paint.setColor(Color.BLACK);
        canvas.drawPath(p, paint);
    }

    /**
     * 计算控制点
     */
    private Point calc(Point p1, Point p2, float multiplier) {
//
//        float diffX = p2.x - p1.x;
//        float diffY = p2.y - p1.y;
//
//        float x = (p1.x + (diffX * multiplier));
//        float y = (p1.y + (diffY * multiplier));
        Point point = new Point();

        float diffX = p2.x - p1.x;
        float diffY = p2.y - p1.y;
        point.setX(p1.x + (diffX * multiplier));
        point.setY(p1.y + (diffY * multiplier));
        return point;
    }

    //3分
    private void changePoint(int valueX) {
        points.clear();

        int x1 = width / 3;
        int x2 = width * 2 / 3;

//        int x = x1;
//        if (x2 < x1) {
//            x1 = x2;
//            x2 = x;
//        }

//        int y1 = 0;
//        if (valueX < width / 4) {
//            y1 = x1;
//
//        } else if (valueX < width / 2) {
//            y1 = width / 2 - x1;
//
//        } else if (valueX < width * 3 / 4) {
//            y1 = width / 2 - x1;
//
//        } else if (valueX < width) {
//            y1 = x1 - width;
//        }
//
//        if (valueX < width / 2) {
//            y1 = x1;
//
//        } else if (valueX < width) {
//            y1 = width - x1;
//        }
//
//
//        y1 = y1 / 4;

        Point point1 = new Point(x1, height / 2 - maxHeight);
        Point point2 = new Point(x2, height / 2 + maxHeight);
        points.add(point1);
        points.add(point2);
//        if (x2 <= width)
//            points.add(point2);


//        if (value == 0 || value == width) {
//
//        } else {
//
//            for (int i = 0; i < 3; i++) {
//                int x = value + width / 4 * i;
//                int y = 0;
//
//                int valueY = value * width / height;
//
//                if (x < width) {
//                    y = width % value;
//                } else {
//
//                }
//
//                Point point = new Point(x, y);
//
//                points.add(point);
//            }
//        }
//
//
//

        points.add(0, startPoint);
        points.add(endPoint);


        for (Point point : points) {
            Log.i("debug", point.toString());
        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = getWidth();
        height = getHeight();

        startPoint = new Point(0, height / 2);
        endPoint = new Point(width, height / 2);

    }


    private ValueAnimator animator;

    private void startAnimator() {
        animator = ValueAnimator.ofInt(0 + 60, width - 60);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                changePoint(value);
                postInvalidate();
                Log.i("debug", "value " + value);
            }
        });

        animator.setDuration(4000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(0);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
    }


}
