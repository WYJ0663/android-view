package com.example.Lock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LockView extends View {
    Paint errorPaint;
    float heigth;
    boolean isFinish;
    boolean isInit;
    boolean isSelect;
    boolean movingNoPoint;
    float movingX;
    float movingY;
    int n = 10;//n*n
    Paint normalPaint;
    float offsetX;
    float offsetY;
    private OnPatterChangeListener onPatterChangeListener;
    List<Point> pointList;
    float pointR;
    float pointTR;
    private Point[][] points;
    Paint pressedPaint;
    float width;

    public interface OnPatterChangeListener {
        void onPatterChange(String str);
    }

    public static class Point {
        public static int STATE_ERROR;
        public static int STATE_NORMAL;
        public static int STATE_PRESSED;
        public int i;
        public int j;
        public int state;
        public float x;
        public float y;

        static {
            STATE_NORMAL = 0;
            STATE_PRESSED = 1;
            STATE_ERROR = 2;
        }

        public void setIJ(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
            this.state = STATE_NORMAL;
        }

        public static double distance(Point a, Point b) {
            return Math.sqrt((double) ((Math.abs(a.x - b.x) * Math.abs(a.x - b.x)) + (Math.abs(a.y - b.y) * Math.abs(a.y - b.y))));
        }

        public static boolean with(float pointX, float pointY, float r, float movingX, float movingY) {
            return Math.sqrt((double) (((pointX - movingX) * (pointX - movingX)) + ((pointY - movingY) * (pointY - movingY)))) < ((double) r);
        }
    }

    public LockView(Context context, int n) {
        super(context);
        this.n = n;
        init();
    }


    public LockView(Context context) {
        super(context);
        init();
    }

    public LockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.pointList = new ArrayList();
        this.points = new Point[this.n][this.n];
        this.normalPaint = new Paint();
        this.normalPaint.setColor(Color.WHITE);
        this.pressedPaint = new Paint();
        this.pressedPaint.setColor(Color.WHITE);
        this.errorPaint = new Paint();
        this.errorPaint.setColor(Color.RED);
    }

    private void initPoint() {
        this.width = (float) getWidth();
        this.heigth = (float) getHeight();
        if (this.width > this.heigth) {
            this.offsetX = (this.width - this.heigth) / 2.0f;
            this.width = this.heigth;
        } else {
            this.offsetY = (this.heigth - this.width) / 2.0f;
            this.heigth = this.width;
        }
        float w = this.width / ((float) this.n);
        for (int i = 0; i < this.points.length; i++) {
            for (int j = 0; j < this.points[i].length; j++) {
                this.points[i][j] = new Point(((((float) (i + 1)) * w) - (w / 2.0f)) + this.offsetX, ((((float) (j + 1)) * w) - (w / 2.0f)) + this.offsetY);
                this.points[i][j].setIJ(i, j);
            }
        }
        this.pointR = (this.width / ((float) this.n)) / 10.0f;
        this.pointTR = (this.width / ((float) this.n)) / 2.0f;
        this.isInit = true;
    }

    protected void onDraw(Canvas canvas) {
        if (!this.isInit) {
            initPoint();
        }
        points2Canvas(canvas);
        if (this.pointList.size() > 0) {
            Point a = (Point) this.pointList.get(0);
            for (int i = 0; i < this.pointList.size(); i++) {
                Point b = (Point) this.pointList.get(i);
                line2Canvas(canvas, a, b);
                a = b;
            }
            if (this.movingNoPoint) {
                line2Canvas(canvas, a, new Point(this.movingX, this.movingY));
            }
        }
    }

    private void points2Canvas(Canvas canvas) {
        for (int i = 0; i < this.points.length; i++) {
            for (Point point : this.points[i]) {
                if (point.state == Point.STATE_PRESSED) {
                    canvas.drawCircle(point.x, point.y, this.pointR, this.pressedPaint);
                } else if (point.state == Point.STATE_ERROR) {
                    canvas.drawCircle(point.x, point.y, this.pointR, this.errorPaint);
                } else {
                    canvas.drawCircle(point.x, point.y, this.pointR, this.normalPaint);
                }
            }
        }
    }

    private void line2Canvas(Canvas canvas, Point a, Point b) {
        float lineLength = (float) Point.distance(a, b);
        float degrees = (float) getDegrees(a, b);
        canvas.rotate(degrees, a.x, a.y);
        RectF rectf = new RectF(a.x - (this.pointR / 3.0f), a.y, a.x + (this.pointR / 3.0f), a.y + lineLength);
        if (a.state == Point.STATE_PRESSED) {
            canvas.drawRect(rectf, this.pressedPaint);
        } else {
            canvas.drawRect(rectf, this.errorPaint);
        }
        canvas.rotate(-degrees, a.x, a.y);
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.movingNoPoint = false;
        this.isFinish = false;
        this.movingX = event.getX();
        this.movingY = event.getY();
        Point point = null;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN /*0*/:
                resetPoint();
                point = checkSelectPoint();
                if (point != null) {
                    this.isSelect = true;
                    break;
                }
                break;
            case MotionEvent.ACTION_UP /*1*/:
                this.isFinish = true;
                this.isSelect = false;
                break;
            case MotionEvent.ACTION_MOVE /*2*/:
                if (this.isSelect) {
                    point = checkSelectPoint();
                    if (point == null) {
                        this.movingNoPoint = true;
                        break;
                    }
                }
                break;
        }
        if (!(this.isFinish || !this.isSelect || point == null)) {
            if (crossPoint(point).booleanValue()) {
                this.movingNoPoint = true;
            } else {
                point.state = Point.STATE_PRESSED;
                this.pointList.add(point);
            }
        }
        if (this.isFinish) {
            if (this.pointList.size() == 1) {
                resetPoint();
            } else if (this.pointList.size() < 5 && this.pointList.size() > 1) {
                errorPoint();
                if (this.onPatterChangeListener != null) {
                    this.onPatterChangeListener.onPatterChange(null);
                }
            } else if (this.onPatterChangeListener != null) {
                String passwordStr = "";
                for (Point p : this.pointList) {
                    passwordStr = passwordStr + "[" + p.i + "," + p.j + "]";
                }
                this.onPatterChangeListener.onPatterChange(passwordStr);
            }
        }
        Log.i("yijun", "pointList.size() = " + this.pointList.size());
        postInvalidate();
        return true;
    }

    private Boolean crossPoint(Point point) {
        if (this.pointList.contains(point)) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(false);
    }

    private void resetPoint() {
        for (Point point : this.pointList) {
            point.state = Point.STATE_NORMAL;
        }
        this.pointList.clear();
    }

    private void errorPoint() {
        for (Point point : this.pointList) {
            point.state = Point.STATE_ERROR;
        }
    }

    private Point checkSelectPoint() {
        for (int i = 0; i < this.points.length; i++) {
            for (Point point : this.points[i]) {
                if (Point.with(point.x, point.y, this.pointTR, this.movingX, this.movingY)) {
                    return point;
                }
            }
        }
        return null;
    }

    public double getDegrees(Point a, Point b) {
        float ax = a.x;
        float ay = a.y;
        float bx = b.x;
        float by = b.y;
        double degrees = 0.0d;
        if (bx == ax && by > ay) {
            degrees = 90.0d;
        } else if (bx < ax && by == ay) {
            degrees = 180.0d;
        } else if (bx == ax && by < ay) {
            degrees = 270.0d;
        } else if (bx > ax && by == ay) {
            degrees = 0.0d;
        } else if (bx > ax) {
            degrees = Math.toDegrees(Math.atan((double) ((by - ay) / (bx - ax))));
        } else if (bx < ax) {
            degrees = Math.toDegrees(Math.atan((double) ((ay - by) / (ax - bx)))) + 180.0d;
        }
        return degrees - 90.0d;
    }

    public void setOnPatterChangeListener(OnPatterChangeListener onPatterChangeListener) {
        if (onPatterChangeListener != null) {
            this.onPatterChangeListener = onPatterChangeListener;
        }
    }
}