package com.example.HeaderListView;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

/**
 * Created by WYJ on 2017/2/20.
 */
public class WXHeaderListView extends ListView {
    private RelativeLayout headerView;
    private LayoutParams headerParams;
    private ImageView loadView;
    private RelativeLayout.LayoutParams loadParams;

    private int initHeight = 330;
    private int loadHeight = 20;

    public WXHeaderListView(Context context) {
        super(context);
        init();
    }

    public WXHeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WXHeaderListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        initHeight = MetricUtil.getDip(getContext(), initHeight);
        loadHeight = MetricUtil.getDip(getContext(), loadHeight);

        headerView = (RelativeLayout) View.inflate(getContext(), R.layout.item_friend_circle_header, null);
        headerParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                initHeight);
        headerView.setLayoutParams(headerParams);
        addHeaderView(headerView);

        loadView = (ImageView) headerView.findViewById(R.id.loading);
        loadParams = (RelativeLayout.LayoutParams) loadView.getLayoutParams();
        loadParams.setMargins(loadHeight, -loadParams.height, 0, 0);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);


    }

    private int startY;
    boolean isChange = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (getFirstVisiblePosition() == 0) {
                    final View topChildView = getChildAt(0);
                    isChange = topChildView.getTop() == 0;
                }
                startY = (int) event.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                if (isChange) {
                    int tempY = (int) (event.getY() - startY);
                    if (tempY > 0) {
                        if (tempY < initHeight) {
                            //整体
                            headerParams.height = tempY + initHeight;
                            headerView.setLayoutParams(headerParams);

                            //彩虹圈加载
                            int loadY = tempY;
                            if (tempY > loadHeight + loadParams.height) {
                                loadY = loadHeight + loadParams.height;
                            }
                            loadParams.setMargins(loadHeight, -loadParams.height + loadY, 0, 0);
                            loadView.setRotation(-MetricUtil.getDip(getContext(), tempY) * 5);
                            loadView.setLayoutParams(loadParams);
                            return true;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (headerParams.height > initHeight) {
                    closeAnimator(headerParams.height);
                }

                if (loadParams.topMargin >= loadHeight) {
                    loadAnimator();
                } else {
                    closeLoadAnimator(loadParams.topMargin);
                }

                isChange = false;
                break;
        }
        return super.onTouchEvent(event);
    }


    private void closeAnimator(int h) {
        ValueAnimator animator = ValueAnimator.ofInt(h, initHeight);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                headerParams.height = value;
                headerView.setLayoutParams(headerParams);

                postInvalidate();
            }
        });

        animator.setDuration(MetricUtil.getDip(getContext(), h) / 6);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    private void closeLoadAnimator(int h) {
        ValueAnimator animator = ValueAnimator.ofInt(h, -loadParams.height);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                loadParams.setMargins(loadHeight, value, 0, 0);
                loadView.setLayoutParams(loadParams);
                Log.d("debug", "closeLoadAnimator value " + value);
                Log.d("debug", "-loadParams.height " + -loadParams.height);
                postInvalidate();
            }
        });

        animator.setDuration(Math.abs(MetricUtil.getDip(getContext(), h)) * 4);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }


    private void loadAnimator() {
        ValueAnimator animator = ValueAnimator.ofInt(0, 360);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                loadView.setRotation(MetricUtil.getDip(getContext(), value));
                postInvalidate();
            }
        });

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                closeLoadAnimator(loadParams.topMargin);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.setDuration(1500);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(1);
        animator.start();
    }

}
