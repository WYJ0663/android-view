package com.example.MiCurve.other;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.example.MiCurve.MetricUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 小米录音的曲线
 * Created by WYJ on 2016/11/9.
 */
public class PoCurveView extends View {
    private Paint paint;
    private int colors[] = {0x70189DAE, 0x703EBACB, 0x70189DAE};//#189DAE

    private float amplitude = 1;
    private int fineness = 1;

    List<Path> paths = null;

    private float lineDistance = 0;//移动距离
    private float wave = 40;//波动高度

    public PoCurveView(Context context) {
        super(context);
        init();
    }

    public PoCurveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PoCurveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        wave = MetricUtil.getDip(getContext(), wave);

        paint = new Paint();
        paint.setAntiAlias(true); // 消除锯齿
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(MetricUtil.getDip(getContext(), 2f));

        paths = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            paths.add(new Path());
        }

    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);


//        changePoint(0);
        drawScrollLine(canvas);

        invalidate();
    }


    //画曲线
    private void drawScrollLine(Canvas canvas) {

        // 实例化混合模式
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));

        canvas.save();
        int moveY = getHeight() / 2;
        for (int i = 0; i < paths.size(); i++) {//20条线，初始化
            Path path = paths.get(i);
            path.reset();
            path.moveTo(0, 0);
            path.lineTo(0, getHeight() / 2);
        }
        for (float x = 0; x < getWidth(); x += fineness) {
            //y = ax^2-awx 抛物线 当 a = -4 / w^2值在0-1中间
            amplitude = -x * x * 4 / getWidth() / getWidth() + 4 * x / getWidth();
            for (int n = 1; n <= paths.size(); n++) {
                float tx = (float) Math.PI * 3 * n / 4;
                //值0-1，且只有一个周期
                float sin =  (float) (Math.sin((x - lineDistance) * 2 * Math.PI / getWidth() + tx));
                paths.get(n - 1).lineTo(x, amplitude * wave *sin + moveY);
            }
        }

        for (int i = 0; i < paths.size(); i++) {
            Path path = paths.get(i);
            path.lineTo(getWidth(), 0);
            path.lineTo(0, 0);
            paint.setColor(colors[i]);
            canvas.drawPath(path, paint);
        }

        canvas.restore();

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);


        if (getWidth() != 0 && getHeight() != 0) {

            startAnimator();
        }
    }


    private ValueAnimator animator;

    private void startAnimator() {
        animator = ValueAnimator.ofInt(0, getWidth());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lineDistance = (int) animation.getAnimatedValue();

                postInvalidate();
            }
        });

        animator.setDuration(1200);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(0);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
    }

}
