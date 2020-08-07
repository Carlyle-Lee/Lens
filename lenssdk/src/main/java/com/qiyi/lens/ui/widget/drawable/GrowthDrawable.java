package com.qiyi.lens.ui.widget.drawable;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;

import androidx.annotation.ColorInt;

/**
 * 用于支持textView 文案效果
 * 渐变动画；
 */
public class GrowthDrawable extends GradientDrawable {


    private ValueAnimator mAnimator;
    private int mDuration = 300;
    private Bounds mBounds;

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
    }


    public void setDuration(int duration) {
        mDuration = duration;
    }

    /**
     * 启动动画
     */
    public void startGrow(int width, int height, boolean isLarger) {

        if (mAnimator != null || height == 0 || width == 0) return;
        mBounds = new Bounds(width, height, isLarger);

        mAnimator = ValueAnimator.ofFloat(0, 2);
        mAnimator.setDuration(mDuration);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                updateDrawable(animation.getAnimatedFraction());
            }
        });
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                Log.d("TAGBBBB", " value height : " + GrowthDrawable.this.getBounds().height());
                reset();
                updateDrawable(0);

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mAnimator = null;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mAnimator.start();


    }

    private void reset() {
        setColors(getGradientColors());
        if (mBounds != null) {
            setGradientCenter(mBounds.right >> 1, mBounds.height * 0.75f);
        }

        setAlpha(0xFF);
    }

    private @ColorInt
    int[] getGradientColors() {
        if (mBounds.mIsLarger) {
            return new int[]{0, 0x56ff0000};
        } else {
            return new int[]{0x5600FF00, 0};
        }
    }

    private void updateDrawable(float value) {
//        Log.d("TAGBBBB"," value " + value);
        if (value < 1) {
            mBounds.figure(value);
            setBounds(mBounds.left, mBounds.top, mBounds.right, mBounds.bottom);
        } else {
            setAlpha((int) (256 * (2 - value)));
        }

    }

    class Bounds {
        int left;
        int top;
        int right;
        int bottom;
        int height;
        boolean mIsLarger;

        public Bounds(int wd, int ht, boolean larger) {
            mIsLarger = larger;
            if (larger) {
                up(wd, ht);
            } else {
                down(wd, ht);
            }
        }


        private void up(int wd, int ht) {
            left = 0;
            right = wd;
            top = ht;
            bottom = ht;
            height = ht;
        }


        void figure(float value) {
            if (mIsLarger) {
                figureUp(value);
            } else {
                figureDown(value);
            }
        }

        private void figureUp(float value) {
            top = (int) (height - value * height);
        }

        private void down(int wd, int ht) {
            left = 0;
            right = wd;
            top = 0;
            bottom = 0;
            height = ht;
        }

        private void figureDown(float value) {
            bottom = (int) (height * value);
        }

    }

}
