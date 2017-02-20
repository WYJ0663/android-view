package com.example.MiCurve;

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
 * 小米录音的曲线
 * Created by WYJ on 2016/11/9.
 */
public class CurveView extends View {
    private int color = 0x70189DAE;//#189DAE
    private int color2 = 0x703EBACB;//#3EBACB
    private int color3 = 0x70189DAE;//#78EFFF

    private int width;
    private int height;

    private Paint paint;
    private Path path = new Path();

    private List<Point> points1 = new ArrayList<>();
    private List<Point> points2 = new ArrayList<>();
    private List<Point> points3 = new ArrayList<>();

    private Handler handler = new Handler();
    private int time = 1000;


    private int maxHeight = 60;//曲线的最大高度
    private Point startPoint;
    private Point endPoint;

    public CurveView(Context context) {
        super(context);
        init();
    }

    public CurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        maxHeight = (int) MetricUtil.getDip(getContext(), maxHeight);

        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true); // 消除锯齿
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(MetricUtil.getDip(getContext(), 2f));


    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

//        changePoint(0);
        paint.setColor(color);
        drawScrollLine(canvas, points1);
        paint.setColor(color2);
        drawScrollLine(canvas, points2);
        paint.setColor(color3);
        drawScrollLine(canvas, points3);
    }

    //画曲线
    private void drawScrollLine(Canvas canvas, List<Point> points) {


        path.reset();
        path.moveTo(0, 0);
        path.lineTo(points.get(0).x, points.get(0).y);

        float mFirstMultiplier = 0.5f;
        float mSecondMultiplier = 1 - mFirstMultiplier;


        for (int i = 0; i < points.size() - 1; i += 1) {
            int nextIndex = i + 1 < points.size() ? i + 1 : i;
            int nextNextIndex = i + 2 < points.size() ? i + 2 : nextIndex;

            // 设置第一个控制点
            Point p1 = calc(points.get(i), points.get(nextIndex), mSecondMultiplier);
            // 设置第二个控制点
            Point p2 = points.get(nextIndex);
            // 设置终点
            Point p3 = calc(points.get(nextIndex), points.get(nextNextIndex), mFirstMultiplier);

            if (i == 0) {
                path.cubicTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
            } else if (i == 2) {
                path.quadTo(points.get(2).x, points.get(2).y, points.get(3).x, points.get(3).y);
            }

            Log.i("debug", "i " + i);
        }
        path.lineTo(width, 0);
        path.lineTo(0, 0);
        canvas.drawPath(path, paint);

    }

    /**
     * 计算控制点
     */
    private Point calc(Point p1, Point p2, float multiplier) {
        Point point = new Point();

        float diffX = p2.x - p1.x;
        float diffY = p2.y - p1.y;
        point.setX(p1.x + (diffX * multiplier));
        point.setY(p1.y + (diffY * multiplier));
        return point;
    }

    //3分
    private void changePoint(int valueX, List<Point> points) {
        points.clear();

        int x1 = valueX;
        int x2 = valueX + width / 2;

        x1 = x1 % width;
        x2 = x2 % width;


        Point point1 = new Point(x1, height / 2 - getY(x1));
        Point point2 = new Point(x2, height / 2 + getY(x2));

        Point p = point1;

        if (x2 < x1) {
            point1 = point2;
            point2 = p;
        }

        points.add(point1);
        points.add(point2);

        points.add(0, startPoint);
        points.add(endPoint);


        for (Point point : points) {
            Log.i("debug", point.toString());
        }

    }

    private int getY(int x1) {
        int y1 = 0;
        if (x1 <= width / 4) {
            y1 = x1;

        } else if (x1 <= width / 2) {
            y1 = width / 4;

        } else if (x1 <= width * 3 / 4) {
            y1 = width / 4;

        } else if (x1 <= width) {
            y1 = (width - x1);
        }

        y1 = y1 * 4 * maxHeight / width;
        return y1;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = getWidth();
        height = getHeight();

        if (width != 0 && height != 0) {
            startPoint = new Point(0, height / 2);
            endPoint = new Point(width, height / 2);

            startAnimator();
        }
    }



    private void startAnimator() {
        ValueAnimator   animator = ValueAnimator.ofInt(0, width);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                changePoint(value, points1);
                changePoint(value + width / 3, points2);
                changePoint(value + width * 2 / 3, points3);

                postInvalidate();
            }
        });

        animator.setDuration(1200);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
    }


}
