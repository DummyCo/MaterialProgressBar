/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package me.zhanghai.android.materialprogressbar;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

class SingleCircularProgressDrawable extends BaseSingleCircularProgressDrawable
        implements ShowBackgroundDrawable {

    /**
     * Value from {@link Drawable#getLevel()}
     */
    private static final int LEVEL_MAX = 10000;

    private static final float START_ANGLE_MAX_NORMAL = 0;
    private static final float START_ANGLE_MAX_DYNAMIC = 360;
    private static final float SWEEP_ANGLE_MAX = 360;

    private final int style;
    private final float mStartAngleMax;
    private float mConstantAngleOffset;

    private boolean mShowBackground;
    private float currentRatio;

    SingleCircularProgressDrawable(int style) {
        this.style = style;
        switch (style) {
            case MaterialProgressBar.DETERMINATE_CIRCULAR_PROGRESS_STYLE_NORMAL:
                mStartAngleMax = START_ANGLE_MAX_NORMAL;
                break;
            case MaterialProgressBar.DETERMINATE_CIRCULAR_PROGRESS_STYLE_DYNAMIC:
                mStartAngleMax = START_ANGLE_MAX_DYNAMIC;
                break;
            case MaterialProgressBar.DETERMINATE_CIRCULAR_PROGRESS_STYLE_DYNAMIC_SPINNING:
                mStartAngleMax = START_ANGLE_MAX_DYNAMIC;
                break;
            default:
                throw new IllegalArgumentException("Invalid value for style");
        }
    }

    @Override
    protected boolean onLevelChange(int level) {
        invalidateSelf();
        return true;
    }

    @Override
    public boolean getShowBackground() {
        return mShowBackground;
    }

    @Override
    public void setShowBackground(boolean show) {
        if (mShowBackground != show) {
            mShowBackground = show;
            invalidateSelf();
        }
    }

    @Override
    protected void onDraw(Canvas canvas, int width, int height, Paint paint) {
        super.onDraw(canvas, width, height, paint);
        if (style == MaterialProgressBar.DETERMINATE_CIRCULAR_PROGRESS_STYLE_DYNAMIC_SPINNING) {
            invalidateSelf();
        }
    }

    @Override
    protected void onDrawRing(Canvas canvas, Paint paint) {
        int level = getLevel();

        float targetRatio = (float) level / LEVEL_MAX;
        float startAngle;

        if (style == MaterialProgressBar.DETERMINATE_CIRCULAR_PROGRESS_STYLE_DYNAMIC_SPINNING) {
            startAngle = mConstantAngleOffset;
            mConstantAngleOffset += 3;
            if (mConstantAngleOffset >= 360) {
                mConstantAngleOffset = 0;
            }
            currentRatio += (targetRatio - currentRatio) * 0.1f;
        } else {
            startAngle = currentRatio * mStartAngleMax;
            currentRatio = targetRatio;
        }

        float sweepAngle = currentRatio * SWEEP_ANGLE_MAX;

        drawRing(canvas, paint, startAngle, sweepAngle);
        if (mShowBackground) {
            // Draw twice to emulate the background for secondary progress.
            drawRing(canvas, paint, startAngle, sweepAngle);
        }
    }
}
