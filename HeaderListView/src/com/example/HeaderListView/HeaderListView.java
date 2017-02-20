package com.example.HeaderListView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by WYJ on 2017/2/20.
 */
public class HeaderListView extends ListView {
    private ImageView headerView;
    private LinearLayout.LayoutParams headerParams;

    private int initHeight = 200;

    public HeaderListView(Context context) {
        super(context);
        init();
    }

    public HeaderListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeaderListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LinearLayout linearLayout = new LinearLayout(getContext());


        initHeight = MetricUtil.getDip(getContext(), initHeight);
        headerView = new ImageView(getContext());
        headerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                initHeight);
        headerView.setLayoutParams(headerParams);
        headerView.setImageResource(R.drawable.wall);
        headerView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        headerParams.setMargins(0, -initHeight/2, 0, 0);
//        headerView.setPadding(0, -initHeight/2, 0, 0);
        linearLayout.addView(headerView);
        addHeaderView(linearLayout);


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
                    if (tempY > 0 && tempY < initHeight) {
                        headerParams.height = tempY + initHeight;
                        headerView.setLayoutParams(headerParams);
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (headerParams.height > initHeight) {
                    closeAnimator(headerParams.height);
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

        animator.setDuration(MetricUtil.getDip(getContext(), h) / 4);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

}
