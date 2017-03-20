package cn.bvin.android.lib.widget.next;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

/**
 * Created by bvin on 2017/3/15.
 */

public class SlideNextLayout extends LinearLayout {

    private boolean mEnable;

    private View mFirstPage;
    private View mNextPage;

    private int mTouchSlop = 0;

    private int mLastY;
    private int mLastInterceptY;

    public SlideNextLayout(Context context) {
        super(context);
    }

    public SlideNextLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mEnable) return super.onInterceptTouchEvent(ev);
        final int y = (int) ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastInterceptY = mLastY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int movedDistance = y - mLastInterceptY;
                if (movedDistance > mTouchSlop/*&&第一页*/) {// 往下移动

                } else if (movedDistance < -mTouchSlop/*&&第二页*/) {// 往上移动

                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastInterceptY = 0;
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
