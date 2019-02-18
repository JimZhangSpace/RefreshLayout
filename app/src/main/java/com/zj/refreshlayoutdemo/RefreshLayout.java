package com.zj.refreshlayoutdemo;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

public class RefreshLayout extends FrameLayout {

    private float startY;
    private float currentY;

    private float mPullHeight;
    private float mHeaderHeight;
    private View mChildView;
    private ProgressView mHeaderView;
    private DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator(10);
    private ValueAnimator mBackUpAnimator;
    private ValueAnimator mBackTopAnimator;
    private static final int BACK_TOP_DURA = 600;
    private static final int BACK_UP_DURA = 200;

    public RefreshLayout(@NonNull Context context) {
        super(context);
    }

    public RefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public RefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getActionMasked()) {

            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                currentY = startY;
                break;
            case MotionEvent.ACTION_MOVE:

                break;
        }

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_MOVE:
                currentY = event.getY();
                float dy = currentY - startY;
                dy = Math.min(mPullHeight * 2, dy);
                dy = Math.max(0, dy);

                if (mChildView != null) {

                    mChildView.setTranslationY(dy);

                    float offsetY = decelerateInterpolator.getInterpolation(dy / 2 / mPullHeight) * dy / 2;
                    mChildView.setTranslationY(offsetY);

                    mHeaderView.getLayoutParams().height = (int) offsetY;
                    mHeaderView.requestLayout();
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if (mChildView != null) {

                    float height = mChildView.getTranslationY();
                    if (height > mHeaderHeight) {

                    } else {
                        ValueAnimator backTopAni = ValueAnimator.ofFloat(height, 0);
                        backTopAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                float val = (float) animation.getAnimatedValue();
                                val = decelerateInterpolator.getInterpolation(val / mHeaderHeight) * val;
                                if (mChildView != null) {
                                    mChildView.setTranslationY(val);
                                }
                                mHeaderView.getLayoutParams().height = (int) val;
                                mHeaderView.requestLayout();
                            }
                        });
                        backTopAni.setDuration((long) (BACK_TOP_DURA * height / mHeaderHeight));
                        backTopAni.start();
                    }
                }

                break;
        }
        return true;
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        if (getChildCount() > 1) {
            throw new RuntimeException("You can attach only one child");
        }

        setAttrs(attrs);

        mPullHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, context.getResources().getDisplayMetrics());
        mHeaderHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());

        this.post(new Runnable() {
            @Override
            public void run() {
                mChildView = getChildAt(0);
                addHeaderView();
            }
        });

    }

    private void setAttrs(AttributeSet attrs) {

    }

    private void addHeaderView() {


        mHeaderView = new ProgressView(getContext());

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        params.gravity = Gravity.TOP;
        mHeaderView.setLayoutParams(params);

        addView(mHeaderView);
        setupChildAnimation();
    }

    private void setupChildAnimation() {
        if (mChildView == null) {
            return;
        }

        mBackUpAnimator = ValueAnimator.ofFloat(mPullHeight, mHeaderHeight);
        mBackUpAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float val = (float) animation.getAnimatedValue();
                if (mChildView != null) {
                    mChildView.setTranslationY(val);
                }
            }
        });
        mBackUpAnimator.setDuration(BACK_UP_DURA);

        mBackTopAnimator = ValueAnimator.ofFloat(mHeaderHeight, 0);
        mBackTopAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

            }
        });
        mBackUpAnimator.setDuration(BACK_TOP_DURA);

    }

}
