package com.example.BookPageView;

import android.animation.Animator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by WYJ on 2017/2/17.
 */
public class BookPageView extends View {
    private float width;
    private float height;

    //边的坐标
    private float x;
    private float y;

    private PointF touchPoint;//

    //绘制
    private Paint paint;
    private Path path;
    private Region region;

    private Path nextPath;
    private Region nextRegion;

    private Bitmap[] bitmaps;

    private int page = 0;

    public BookPageView(Context context) {
        super(context);
        init();
    }

    public BookPageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BookPageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null);//去掉软件加速

        touchPoint = new PointF();

        paint = new Paint();
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        path = new Path();
        nextPath = new Path();


        region = computeRegion(path);
        nextRegion = computeRegion(nextPath);
    }

    /**
     * 通过路径计算区域
     *
     * @param path 路径对象
     * @return 路径的Region
     */
    private Region computeRegion(Path path) {
        Region region = new Region();
        RectF f = new RectF();
        path.computeBounds(f, true);
        region.setPath(path, new Region((int) f.left, (int) f.top, (int) f.right, (int) f.bottom));
        return region;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width == 0 || height == 0) {
            return;
        }


        canvas.drawBitmap(bitmaps[page % bitmaps.length], 0, 0, null);

        canvas.save();
        path.reset();
        paint.setColor(0XFFF1FD00);
        path.moveTo(touchPoint.x, touchPoint.y);
        path.lineTo(width, height - y);
        path.lineTo(width - x, height);
        canvas.drawPath(path, paint);
        canvas.restore();

        nextPath.reset();
        nextPath.moveTo(width, height);
        nextPath.lineTo(width, height - y);
        nextPath.lineTo(width - x, height);

        canvas.save();
        canvas.clipPath(nextPath);
        canvas.clipRect(0, 0, width, height, Region.Op.INTERSECT);
        canvas.drawColor(Color.GREEN);
        canvas.drawBitmap(bitmaps[(page + 1) % bitmaps.length], 0, 0, null);
        canvas.restore();

    }


    private boolean calculate() {
        return calculate(false);
    }

    private boolean calculate(boolean isAuto) {
        //根据三角形面积公式计算
        // 需要重复使用的参数存值避免重复计算

        if (touchPoint.y > height * 9 / 10 && !isAuto) {
            touchPoint.y = height * 9 / 10;
        }

        float mK = width - touchPoint.x;
        float mL = height - touchPoint.y;
        float temp = (float) (Math.pow(mL, 2) + Math.pow(mK, 2));

        x = temp / (2F * mK);
        y = temp / (2F * mL);

        Log.d("debug", "x " + x + " y " + y);
        if (x > width || x < -width || y > height * 2 || y < 0) {
            return false;
        }

        return true;
    }

    private boolean isMove = false;
    private boolean isAuto = false;//自动翻页

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isAuto) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                touchPoint.x = width;
                touchPoint.y = height;

                if (width - event.getX() < 100 && height - event.getY() < 100) {
                    isMove = true;

                    touchPoint.x = event.getX();
                    touchPoint.y = event.getY();

                    boolean is = calculate();
                    if (is) {
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (isMove) {

                    touchPoint.x = event.getX();
                    touchPoint.y = event.getY();

                    boolean is = calculate();
                    if (is) {
                        invalidate();
                    }

                }
                break;
            case MotionEvent.ACTION_UP:
                if (isMove) {
                    auto();
                    isMove = false;
                }
                break;
        }
        return true;
    }

    private void auto() {
        calculate();

        if (x < width / 3) {//回去
            startAnimator(width);
        } else {//打开
            startAnimator(-width);
        }
    }


    private void startAnimator(final float endX) {
        isAuto = true;

        PointF startPoint = touchPoint;
        PointF endPoint = new PointF(endX, height);
        ValueAnimator anim = ValueAnimator.ofObject(new PointEvaluator(), startPoint, endPoint);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                touchPoint = (PointF) animation.getAnimatedValue();
                calculate(true);
                postInvalidate();


            }
        });


        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                touchPoint.x = -width;
                touchPoint.y = height;
                if (endX == -width) {
                    page++;
                }
                postInvalidate();
                Log.d("debug", "动画结束");
                isAuto = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        anim.setDuration((long) Math.abs(endX - touchPoint.x));
        anim.start();

    }

    public class PointEvaluator implements TypeEvaluator {

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            PointF startPoint = (PointF) startValue;
            PointF endPoint = (PointF) endValue;
            float x = startPoint.x + fraction * (endPoint.x - startPoint.x);
            float y = startPoint.y + fraction * (endPoint.y - startPoint.y);
            PointF point = new PointF(x, y);

            return point;
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getWidth();
        height = getHeight();
        if (width > 0 && height > 0) {
            touchPoint.x = width;
            touchPoint.y = height;

            boolean is = calculate();
            invalidate();

            Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.b1);
            Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.b2);
            bitmaps = new Bitmap[]{bitmap1, bitmap2};
        }
    }


}
