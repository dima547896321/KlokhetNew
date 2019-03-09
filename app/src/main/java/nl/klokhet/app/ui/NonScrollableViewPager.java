package nl.klokhet.app.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class NonScrollableViewPager extends ViewPager {

    private boolean mScrollingPagesEnabled;

    public NonScrollableViewPager(Context context) {
        super(context);
    }

    public NonScrollableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mScrollingPagesEnabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.mScrollingPagesEnabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    /**
     * Enable or disable the swipe navigation
     *
     * @param enabled
     */
    public void setPagesScrollingEnabled(boolean enabled) {
        this.mScrollingPagesEnabled = enabled;
    }
}

