package com.example.timor.myviewdraghelper;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Flavien Laurent (flavienlaurent.com) on 23/08/13.
 */
public class DragLayout extends LinearLayout {

    private final ViewDragHelper mDragHelper;

    private View mDragView1;
    private View mDragView2;

    private boolean mDragEdge;
    private boolean mDragHorizontal;
    private boolean mDragCapture;
    private boolean mDragVertical;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDragHelper = ViewDragHelper.create(this, 1f, new DragHelperCallback());
    }

    /**
     * 布局加载完成后执行的方法
     */
    @Override
    protected void onFinishInflate() {
        mDragView1 = findViewById(R.id.drag1);
        mDragView2 = findViewById(R.id.drag2);
    }

    /**
     * 水平
     * @param dragHorizontal
     */
    public void setDragHorizontal(boolean dragHorizontal) {
        mDragHorizontal = dragHorizontal;
        mDragView2.setVisibility(View.GONE);
    }

    /**
     *垂直
     * @param dragVertical
     */
    public void setDragVertical(boolean dragVertical) {
        mDragVertical = dragVertical;
        mDragView2.setVisibility(View.GONE);
    }

    /**
     * 滑动左边缘还是右边缘
     * 假如设置着边缘，onEdgeTouched方法会在左边缘滑动的时候被调用，这种情况下一般都是没有和子view接触的情况。
     * @param dragEdge
     */
    public void setDragEdge(boolean dragEdge) {
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        mDragEdge = dragEdge;
        mDragView2.setVisibility(View.GONE);
    }

    public void setDragCapture(boolean dragCapture) {
        mDragCapture = dragCapture;
    }

    /**
     * Callback
     */
    private class DragHelperCallback extends ViewDragHelper.Callback {

        /**
         * 决定parentview中哪个子view可以拖动
         * @param child
         * @param pointerId
         * @return
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            if (mDragCapture) {
                return child == mDragView1;
            }
            return true;
        }

        /**
         * changedView 的 position 改变了
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            invalidate();
        }

        /**
         * view 被捕获
         * @param capturedChild
         * @param activePointerId
         */
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        /**
         * View 被释放
         * @param releasedChild
         * @param xvel
         * @param yvel
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
        }

        /**
         * 
         * @param edgeFlags
         * @param pointerId
         */
        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
        }

        /**
         * 根据滑动距离移动一个子view，
         * @param edgeFlags
         * @param pointerId
         */
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            if (mDragEdge) {
                mDragHelper.captureChildView(mDragView1, pointerId);
            }
        }

        /**
         * 实现纵向拖动
         * @param child
         * @param top
         * @param dy
         * @return
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (mDragVertical) {
                final int topBound = getPaddingTop();
                final int bottomBound = getHeight() - mDragView1.getHeight();

                final int newTop = Math.min(Math.max(top, topBound), bottomBound);

                return newTop;
            }
            return super.clampViewPositionVertical(child, top, dy);
        }

        /**
         * 实现横向拖动
         * @param child
         * @param left
         * @param dx
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (mDragHorizontal || mDragCapture || mDragEdge) {
                final int leftBound = getPaddingLeft();
                final int rightBound = getWidth() - mDragView1.getWidth();

                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);

                return newLeft;
            }
            return super.clampViewPositionHorizontal(child, left, dx);
        }

    }

    /**
     * 将触摸事件传递给ViewDragHelper
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel();
            return false;
        }
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);
        return true;
    }

}
