package com.example.CircleBethel;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 1.先计算2个圆的中点
 * 2.计算2圆心的斜率
 * 3.垂直的斜率，相乘等于-1
 * 4.根据三角函数计算出偏移量
 * 5.计算出切点
 * 6.画图，圆和贝塞尔曲线
 * Created by WYJ on 2017/2/17.
 */
public class CircleBethelView1 extends View {

    private Circle firstCircle;
    private Circle secondCircle;

    private PointF midpoint;//中点

    private PointF[] firstPoints;//切点1
    private PointF[] SecondPoints;//切点2

    //绘制
    private Paint paint;
    private Path path;

    private boolean isMove = false;
    private float startX;
    private float startY;
    private Circle startCircle;//记录起始的圆

    public CircleBethelView1(Context context) {
        super(context);
        init();
    }

    public CircleBethelView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleBethelView1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        path = new Path();

        firstCircle = new Circle(300, 500, 20);
        secondCircle = new Circle(200, 300, 40);
        startCircle = new Circle();

        midpoint = new PointF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();

        calculate();
        paint.setColor(Color.RED);
        canvas.drawCircle(firstCircle.x, firstCircle.y, firstCircle.r, paint);
        canvas.drawCircle(secondCircle.x, secondCircle.y, secondCircle.r, paint);

        path.reset();
        path.moveTo(firstPoints[0].x, firstPoints[0].y);
        path.quadTo(midpoint.x, midpoint.y,
                SecondPoints[0].x, SecondPoints[0].y);
        path.lineTo(SecondPoints[1].x, SecondPoints[1].y);
        path.quadTo(midpoint.x, midpoint.y,
                firstPoints[1].x, firstPoints[1].y);
        path.close();
        canvas.drawPath(path, paint);

//        paint.setColor(Color.BLUE);
//        paint.setStrokeWidth(10);
//        canvas.drawPoint(firstPoints[0].x, firstPoints[0].y,paint);
//        paint.setColor(Color.GREEN);
//        canvas.drawPoint(firstPoints[1].x, firstPoints[1].y,paint);
//        paint.setColor(Color.YELLOW);
//        canvas.drawPoint(SecondPoints[0].x, SecondPoints[0].y,paint);
//        paint.setColor(Color.WHITE);
//        canvas.drawPoint(SecondPoints[1].x, SecondPoints[1].y,paint);
//        paint.setColor(Color.GRAY);
//        canvas.drawPoint(midpoint.x, midpoint.y,paint);
        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                isMove = secondCircle.isInside(startX, startY);
                startCircle.x = secondCircle.x;
                startCircle.y = secondCircle.y;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isMove) {
                    float endX = event.getX();
                    float endY = event.getY();

                    float dx = endX - startX;
                    float dy = endY - startY;

                    secondCircle.x = startCircle.x + dx;
                    secondCircle.y = startCircle.y + dy;

                    Log.d("debug", secondCircle.toString());
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                isMove = false;
                break;
        }
        return true;
    }

    /**
     * 计算点的位置
     */
    private void calculate() {
        midpoint.x = (firstCircle.x + secondCircle.x) / 2;
        midpoint.y = (firstCircle.y + secondCircle.y) / 2;

        float dx = firstCircle.x - secondCircle.x;
        float dy = firstCircle.y - secondCircle.y;

        float k1 = dy / dx;
        float k2 = -1 / k1;

        firstPoints = getIntersectionPoints(firstCircle, (double) k2);

        SecondPoints = getIntersectionPoints(secondCircle, (double) k2);

        Log.d("debug", midpoint.toString());
        Log.d("debug", firstPoints[0].toString() + " " + firstPoints[1].toString());
        Log.d("debug", SecondPoints[0].toString() + " " + SecondPoints[1].toString());
    }


    /**
     * 获取 通过指定圆心，斜率为lineK的直线与圆的交点。
     *
     * @param circle 圆
     * @param lineK  斜率
     * @return
     */
    public static PointF[] getIntersectionPoints(Circle circle, Double lineK) {
        PointF[] points = new PointF[2];

        float radian, xOffset = 0, yOffset = 0;
        if (lineK != null) {

            //根据三角函数计算
            radian = (float) Math.atan(lineK);
            xOffset = (float) (Math.cos(radian) * circle.r);
            yOffset = (float) (Math.sin(radian) * circle.r);
        } else {
            xOffset = circle.r;
            yOffset = 0;
        }
        points[0] = new PointF(circle.x + xOffset, circle.y + yOffset);
        points[1] = new PointF(circle.x - xOffset, circle.y - yOffset);

        return points;
    }


    //圆对象
    class Circle {
        public float x;
        public float y;
        public int r;//半径

        public Circle() {
        }

        public Circle(int x, int y, int r) {
            this.x = x;
            this.y = y;
            this.r = r;
        }

        //判断点是否在圆里面
        public boolean isInside(float pX, float pY) {
            float distance = (pX - x) * (pX - x) + (pY - y) * (pY - y);
            Log.d("debug", "distance " + distance);
            Log.d("debug", " r * r " + (r * r));
            if (distance < (r * r)) {
                return true;
            } else {
                return false;
            }
        }
    }

}
