package com.dinghong.studyviewdemo1.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

import com.dinghong.studyviewdemo1.R;
import com.facebook.drawee.view.SimpleDraweeView;


/**
 * Created by Jason Chen on 2017/12/7.
 */

public class ToTopBehavior extends CoordinatorLayout.Behavior<SimpleDraweeView> {
    private static boolean isAnimatingOut = false;
    public static final int ANIM_STATE_NONE = 0;
    public static final int ANIM_STATE_HIDING = 1;
    public static final int ANIM_STATE_SHOWING = 2;
    public static final int SHOW_HIDE_ANIM_DURATION = 200;
    int mAnimState = ANIM_STATE_NONE;

    NestedScrollView nScrollView;

    public ToTopBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull SimpleDraweeView child, @NonNull View directTargetChild, @NonNull View target, int axes) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull SimpleDraweeView child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {

        if (dyConsumed > 0 && dyUnconsumed == 0) {
            //上滑中。。。
        }
        if (dyConsumed == 0 && dyUnconsumed > 0) {
            //到边界了还在上滑。。。
        }
        if (dyConsumed < 0 && dyUnconsumed == 0) {
            //下滑中。。。
        }
        if (dyConsumed == 0 && dyUnconsumed < 0) {
            //到边界了，还在下滑。。。
        }

        NestedScrollView nScrollView = coordinatorLayout.findViewById(R.id.nscroll);
        if (this.nScrollView == null) {
            this.nScrollView = nScrollView;
            this.nScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (oldScrollY < scrollY && child.getVisibility() != View.VISIBLE) {

                    scaleShow(child);
                } else if (oldScrollY > scrollY && child.getVisibility() == View.VISIBLE) {
                    scaleHide(child);
                }
            });
        }
    }

    public static void scaleShow(View view) {
        if (isOrWillBeShown(view)) {
            // We either are or will soon be visible, skip the call
            return;
        }

        view.animate().cancel();

        if (shouldAnimateVisibilityChange(view)) {
//            mAnimState = ANIM_STATE_SHOWING;
            view.setTag(ANIM_STATE_SHOWING);

            if (view.getVisibility() != View.VISIBLE) {
                // If the view isn't visible currently, we'll animate it from a single pixel
                view.setAlpha(0f);
                view.setScaleY(0f);
                view.setScaleX(0f);
            }

            view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .alpha(1f)
                    .setDuration(SHOW_HIDE_ANIM_DURATION)
                    .setInterpolator(new FastOutLinearInInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            view.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
//                            mAnimState = ANIM_STATE_NONE;
                            view.setTag(ANIM_STATE_NONE);

                        }
                    });
        } else {
            view.setVisibility(View.VISIBLE);
            view.setAlpha(1f);
            view.setScaleY(1f);
            view.setScaleX(1f);
        }
    }

    public static void scaleHide(final View view) {
        if (isOrWillBeHidden(view)) {
            // We either are or will soon be hidden, skip the call
            return;
        }

        view.animate().cancel();

        if (shouldAnimateVisibilityChange(view)) {
//            mAnimState = ANIM_STATE_HIDING;
            view.setTag(ANIM_STATE_HIDING);
            view.animate()
                    .scaleX(0f)
                    .scaleY(0f)
                    .alpha(0f)
                    .setDuration(SHOW_HIDE_ANIM_DURATION)
                    .setInterpolator(new FastOutLinearInInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        private boolean mCancelled;

                        @Override
                        public void onAnimationStart(Animator animation) {
                            view.setVisibility(View.VISIBLE);
                            mCancelled = false;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            mCancelled = true;
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
//                            mAnimState = ANIM_STATE_NONE;
                            view.setTag(ANIM_STATE_NONE);
                            if (!mCancelled) {
                                view.setVisibility(View.INVISIBLE);

                            }
                        }
                    });
        } else {
            // If the view isn't laid out, or we're in the editor, don't run the animation
            view.setVisibility(View.INVISIBLE);
        }
    }

    static boolean isOrWillBeHidden(View viwe) {
        if (viwe.getTag() == null)
            return false;

        if (viwe.getVisibility() == View.VISIBLE) {
            // If we currently visible, return true if we're animating to be hidden

            return ((int) viwe.getTag()) == ANIM_STATE_HIDING;
//            return mAnimState == ANIM_STATE_HIDING;
        } else {
            // Otherwise if we're not visible, return true if we're not animating to be shown
            return ((int) viwe.getTag()) == ANIM_STATE_SHOWING;
//            return mAnimState != ANIM_STATE_SHOWING;
        }
    }

    static boolean isOrWillBeShown(View viwe) {
        if (viwe.getTag() == null)
            return false;

        if (viwe.getVisibility() != View.VISIBLE) {
            // If we not currently visible, return true if we're animating to be shown
            return ((int) viwe.getTag()) == ANIM_STATE_SHOWING;
//            return mAnimState == ANIM_STATE_SHOWING;
        } else {
            // Otherwise if we're visible, return true if we're not animating to be hidden
            return ((int) viwe.getTag()) == ANIM_STATE_HIDING;
//            return mAnimState != ANIM_STATE_HIDING;
        }
    }

    private static boolean shouldAnimateVisibilityChange(View view) {
        return ViewCompat.isLaidOut(view) && !view.isInEditMode();
    }

}
