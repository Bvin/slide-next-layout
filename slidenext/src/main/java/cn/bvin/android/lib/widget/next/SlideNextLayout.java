package cn.bvin.android.lib.widget.next;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by bvin on 2017/3/15.
 */

public class SlideNextLayout extends LinearLayout {

    private boolean mEnable = true;

    private View mFirstPage;
    private View mNextPage;

    private int mTouchSlop = 0;

    private int mLastY;
    private int mLastInterceptY;

    private OnSlideCallback mOnSlideCallback;

    private int mCurrentPage = 0;// 默认在第一页
    private Scroller mScroller;

    public SlideNextLayout(Context context) {
        super(context);
    }

    public SlideNextLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);
        // 最小有效移动距离
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
      super.onFinishInflate();
        final int childCount = getChildCount();
        if (2 < childCount) {
            throw new RuntimeException("child count more than 2!");
        }
        mFirstPage = getChildAt(0);
        if (childCount>1){
            mNextPage = getChildAt(1);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (hasWindowFocus) {
            ViewGroup.LayoutParams lps = mNextPage.getLayoutParams();
            if (lps.height != mFirstPage.getMeasuredHeight()) {
                lps.height = mFirstPage.getMeasuredHeight();
                mNextPage.setLayoutParams(lps);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mEnable || mFirstPage == null || mNextPage == null) return super.onInterceptTouchEvent(ev);
        final int y = (int) ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastInterceptY = mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int movedDistance = y - mLastInterceptY;
                if (movedDistance > mTouchSlop && mCurrentPage == 1) {
                    if (mOnSlideCallback != null) {
                        return mOnSlideCallback.onSlideToPrevious(ev);
                    }else {
                        if (mNextPage instanceof ScrollView){
                            return mNextPage.getScrollY() == 0;
                        }
                    }
                } else if (movedDistance < -mTouchSlop && mCurrentPage == 0) {
                    if (mOnSlideCallback != null) {
                        return mOnSlideCallback.onSlideToNext(ev);
                    }else {
                        if (mFirstPage instanceof ScrollView){
                            return mFirstPage.getScrollY() + mFirstPage.getHeight() >= ((ScrollView) mFirstPage).getChildAt(0)
                                    .getHeight();
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastInterceptY = 0;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = y - mLastY;
                if (mCurrentPage == 0){
                    int sy = -dy;
                    if (sy < 0) {
                        sy = 0;
                    } else if (sy > getHeight()) {
                        sy = getHeight();
                    }
                    scrollTo(0, sy);
                }else if(mCurrentPage == 1){
                    if (dy > 0) {
                        scrollTo(0, mFirstPage.getMeasuredHeight() - dy);
                    } else { // dy < 0
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                int mHeightOfFirstPage = mFirstPage.getMeasuredHeight();
                int t = 0;
                switch (mCurrentPage) {
                    case 0:
                        t = mHeightOfFirstPage / 4;
                        break;
                    case 1:
                        t = mHeightOfFirstPage * 3 / 4;
                        break;
                }
                int sy = getScrollY();
                if (sy > t) { // scroll to footer
                    mScroller.startScroll(0, sy, 0, mHeightOfFirstPage - sy, 150);
                    mCurrentPage = 1;
                    invalidate();
                } else { // scroll to header
                    mScroller.startScroll(0, sy, 0, -sy, 150);
                    mCurrentPage = 0;
                    invalidate();
                }
                mLastY = 0;
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void setOnSlideCallback(OnSlideCallback onSlideCallback) {
        mOnSlideCallback = onSlideCallback;
    }

    public interface OnSlideCallback{
        boolean onSlideToNext(MotionEvent ev);
        boolean onSlideToPrevious(MotionEvent ev);
    }
}
