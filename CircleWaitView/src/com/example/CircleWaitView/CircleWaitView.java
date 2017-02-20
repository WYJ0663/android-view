package com.example.CircleWaitView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

/**
 * 圆环等待框
 * Created by WYJ on 2016-05-17.
 */
public class CircleWaitView extends TextView {
    private int time = 3000;
    //第一圈的颜色
    private int firstColor = 0XFFAAAAAA;
    //第二圈的颜色
    private int secondColor = 0XFFF57D28;
    //view 的宽度
    private int width;
    //圆环宽度
    private int annulusWidth = 2;
    //画笔
    private Paint paint1;
    //画笔
    private Paint paint2;
    //圆心，xy
    private int centre;
    //半径1
    private int annulusRadius;
    //扇形
    private RectF ovalRectF;

    //当前进度
    private int progress;
    //最大限度
    private int max;

    private boolean isStart = false;

    public CircleWaitView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleWaitView(Context context) {
        this(context, null);
    }

    public CircleWaitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        annulusWidth = MetricUtil.getDip(getContext(), annulusWidth);

        progress = 0;
        max = 1000;

        paint1 = new Paint();
        paint1.setStrokeWidth(annulusWidth); // 设置圆环的宽度
        paint1.setColor(firstColor); // 设置圆环的颜色
        paint1.setAntiAlias(true); // 消除锯齿
        paint1.setStyle(Paint.Style.STROKE); // 设置空心

        paint2 = new Paint();
        paint2.setStrokeWidth(annulusWidth); // 设置圆环的宽度
        paint2.setColor(secondColor); // 设置圆环的颜色
        paint2.setAntiAlias(true); // 消除锯齿
        paint2.setStyle(Paint.Style.STROKE); // 设置空心
    }


    @Override
    protected void onDraw(Canvas canvas) {

        if (ovalRectF != null) {
            canvas.drawCircle(centre, centre, annulusRadius, paint1); // 画出圆环
            canvas.drawArc(ovalRectF, -90, 360 * progress / max, false, paint2); // 根据进度画圆弧
        }

        super.onDraw(canvas);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        width = getWidth();

        centre = getWidth() / 2;

        annulusRadius = centre - annulusWidth / 2 - getWidth() / 25;//内半径

        ovalRectF = new RectF(centre - annulusRadius, centre - annulusRadius, centre + annulusRadius, centre + annulusRadius);
    }

    //秒
    public void start(int time) {
        this.time = time * 1000;
        isStart = true;
        startAnimator();
    }

    public void stop() {
        isStart = false;
        if (animator != null) {
            animator.cancel();
        }
    }


    private ValueAnimator animator;

    private void startAnimator() {
        animator = ValueAnimator.ofInt(0, max);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();

                progress = value;

                postInvalidate();

//                if (value == max / 2) {
//                    stop();
//                }

                if (onCircleWaitListener != null && value >= max && isStart) {
                    isStart = false;
                    onCircleWaitListener.onFinish();
                }
            }
        });

        animator.setDuration(time);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(0);
        animator.start();
    }


    private OnCircleWaitListener onCircleWaitListener;

    public void setOnCircleWaitListener(OnCircleWaitListener onCircleWaitListener) {
        this.onCircleWaitListener = onCircleWaitListener;
    }

    public interface OnCircleWaitListener {
        void onFinish();
    }

}
