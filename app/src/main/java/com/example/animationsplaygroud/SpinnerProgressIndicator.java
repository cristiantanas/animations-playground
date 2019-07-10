package com.example.animationsplaygroud;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class SpinnerProgressIndicator extends View {

    private static final float INDICATOR_START_ANGLE = 270f;
    private static final float INDICATOR_SWEEP_ANGLE = 50f;
    private static final float CIRCLE_ANGLE_MIN = 0f;
    private static final float CIRCLE_ANGLE_MAX = 360f;

    private static final long ANIMATION_DURATION_MS = 1500;

    private static final float STROKE_WIDTH = 10f;

    private Paint primaryPaint;
    private Paint secondaryPaint;
    private RectF bounds;
    private float sweepAngle;
    private float startAngle;

    public SpinnerProgressIndicator(Context context) {
        this(context, null);
    }

    public SpinnerProgressIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpinnerProgressIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.bounds = new RectF();

        this.primaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.primaryPaint.setStyle(Paint.Style.STROKE);
        this.primaryPaint.setStrokeWidth(STROKE_WIDTH);
        this.primaryPaint.setColor(ContextCompat.getColor(context, android.R.color.holo_blue_light));

        this.secondaryPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.secondaryPaint.setStyle(Paint.Style.STROKE);
        this.secondaryPaint.setStrokeWidth(STROKE_WIDTH);
        this.secondaryPaint.setColor(ContextCompat.getColor(context, android.R.color.darker_gray));

        this.sweepAngle = INDICATOR_SWEEP_ANGLE;
        this.startAngle = INDICATOR_START_ANGLE;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.startAnimation();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int half = (int) STROKE_WIDTH / 2;
        bounds.set(half, half, w - half, w - half);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(this.bounds, CIRCLE_ANGLE_MIN, CIRCLE_ANGLE_MAX, false, this.secondaryPaint);
        canvas.drawArc(this.bounds, this.startAngle, this.sweepAngle, false, this.primaryPaint);
    }

    private void startAnimation() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(CIRCLE_ANGLE_MIN, CIRCLE_ANGLE_MAX);
        valueAnimator.setDuration(ANIMATION_DURATION_MS);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startAngle = (float) animation.getAnimatedValue() + INDICATOR_START_ANGLE % CIRCLE_ANGLE_MAX;
                invalidate();
            }
        });
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }
}
