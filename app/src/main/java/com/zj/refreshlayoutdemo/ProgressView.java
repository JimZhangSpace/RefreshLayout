package com.zj.refreshlayoutdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class ProgressView extends View {

    private int mHeight;
    private int mWidth;

    private int PULL_HEIGHT;
    private int PULL_DELTA;
    private Paint mBackPaint;
    private Paint mBallPaint;
    private Paint mOutPaint;
    private Path mPath;
    private ProgressStatus mProgressStatus;


    public ProgressView(Context context) {
        this(context, null, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public enum ProgressStatus {
        PULL_DOWN,
        DRAG_DOWN,
        REL_DRAG
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mHeight = h;
        mWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (mProgressStatus) {
            case PULL_DOWN:
                canvas.drawRect(new Rect(0, 0, mWidth, mHeight), mBackPaint);
                break;
            case DRAG_DOWN:
            case REL_DRAG:
                drawDrag(canvas);
                break;

        }

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (changed) {
            mWidth = getWidth();
            mHeight = getHeight();

            if (mHeight < PULL_HEIGHT) {
                mProgressStatus = ProgressStatus.PULL_DOWN;
            } else if (mHeight >= PULL_HEIGHT) {
                mProgressStatus = ProgressStatus.DRAG_DOWN;
            }
        }
    }

    public void releaseDrag() {
        mProgressStatus = ProgressStatus.REL_DRAG;
        requestLayout();

    }
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        PULL_HEIGHT = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, context.getResources().getDisplayMetrics());
        PULL_DELTA = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, context.getResources().getDisplayMetrics());

        mBackPaint = new Paint();
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setColor(0xff8b90af);

        mPath = new Path();

    }

    private void drawDrag(Canvas canvas) {
        canvas.drawRect(new Rect(0, 0, mWidth, PULL_HEIGHT), mBackPaint);

        mPath.reset();
        mPath.moveTo(0, PULL_HEIGHT);
        mPath.quadTo(mWidth / 2, PULL_HEIGHT + (mHeight - PULL_HEIGHT) * 2, mWidth, PULL_HEIGHT);
        canvas.drawPath(mPath, mBackPaint);
    }

}
