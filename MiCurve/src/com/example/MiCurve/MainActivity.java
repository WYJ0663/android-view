package com.example.MiCurve;

import android.app.Activity;
import android.content.Context;
import android.graphics.*;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(new BezierView(this));
    }
}

class BezierView extends View {
    /**
     * @param context
     * @author liqiongwei
     */
    public BezierView(Context context) {
        super(context);
    }

    protected void onDraw(Canvas canvas) {

        List<Float> points = new ArrayList<Float>();

        Paint paint = new Paint();
        // 添加第一个点(118.0, 294.0),
        points.add((float) 118.0);// X轴
        points.add((float) 294.0);// Y轴
        // 添加第二个点
        points.add((float) 206.0);
        points.add((float) 444.0);
        // 添加第三个点
        points.add((float) 294.0);
        points.add((float) 118.0);
//        // 添加第四个点
//        points.add((float) 382.0);
//        points.add((float) 206.0);

        points.add((float) 470.0);
        points.add((float) 294.0);

        // 通过画折线和贝塞尔曲线可以知道，点得位置是不一样的。
        // 画折线
        paint.setColor(Color.RED);
        for (int i = 0; i < points.size() - 2; i = i + 2) {
            canvas.drawLine(points.get(i), points.get(i + 1), points.get(i + 2), points.get(i + 3), paint);
            canvas.drawCircle(points.get(i), points.get(i + 1), 3, paint);
        }
        canvas.drawCircle(points.get(points.size() - 2), points.get(points.size() - 1), 3, paint);

        // 贝塞尔曲线
        paint.setColor(Color.BLUE);
        Path p = new Path();
        Point p1 = new Point();
        Point p2 = new Point();
        Point p3 = new Point();
        float xp = points.get(0);
        float yp = points.get(1);
        // 设置第一个点开始
        p.moveTo(xp, yp);
        int length = points.size();
        // 设置第一个控制点33%的距离
        float mFirstMultiplier = 0.5f;
        // 设置第二个控制点为66%的距离
        float mSecondMultiplier = 1 - mFirstMultiplier;

        for (int b = 0; b < length; b += 2) {
            int nextIndex = b + 2 < length ? b + 2 : b;
            int nextNextIndex = b + 4 < length ? b + 4 : nextIndex;
            // 设置第一个控制点
            calc(points, p1, b, nextIndex, mSecondMultiplier);
            // 设置第二个控制点
            p2.setX(points.get(nextIndex));
            p2.setY(points.get(nextIndex + 1));
            // 设置第二个控制点
            calc(points, p3, nextIndex, nextNextIndex, mFirstMultiplier);
            // 最后一个点就是赛贝尔曲线上的点
            p.cubicTo(p1.getX(), p1.getY(), p2.getX(), p2.getY(), p3.getX(), p3.getY());
            // 画点
        }
        paint.setStyle(Style.STROKE);
        canvas.drawPath(p, paint);

        PathMeasure mPathMeasure;
        mPathMeasure = new PathMeasure(p, false);
        // 设置为线

        reSetPointWithPath(mPathMeasure, points);
        for (int k = 0; k < points.size() - 1; k += 2) {
            canvas.drawCircle(points.get(k), points.get(k + 1), 5, paint);
        }


        invalidate();
    }

    /**
     * 计算控制点
     *
     * @param points
     * @param result
     * @param index1
     * @param index2
     * @param multiplier
     */
    private void calc(List<Float> points, Point result, int index1, int index2, final float multiplier) {
        float p1x = points.get(index1);
        float p1y = points.get(index1 + 1);
        float p2x = points.get(index2);
        float p2y = points.get(index2 + 1);

        float diffX = p2x - p1x;
        float diffY = p2y - p1y;
        result.setX(p1x + (diffX * multiplier));
        result.setY(p1y + (diffY * multiplier));
    }

    /**
     * 重新设置点的位置，为曲线上的位置
     *
     * @param mPathMeasure
     * @param pointsList
     */
    public void reSetPointWithPath(PathMeasure mPathMeasure, List<Float> pointsList) {
        int length = (int) mPathMeasure.getLength();
        int pointsLength = pointsList.size();
        float[] coords = new float[2];
        for (int b = 0; b < length; b++) {
            mPathMeasure.getPosTan(b, coords, null);
            double prevDiff = Double.MAX_VALUE;
            boolean ok = true;
            for (int j = 0; j < pointsLength && ok; j += 2) {
                double diff = Math.abs(pointsList.get(j) - coords[0]);
                if (diff < 1) {
                    pointsList.set(j + 1, coords[1]);
                    prevDiff = diff;
                }
                ok = prevDiff > diff;
            }
        }
    }
}