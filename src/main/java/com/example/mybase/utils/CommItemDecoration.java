package com.example.mybase.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ConvertUtils;
import com.example.mybase.base.BaseApplication;

/**
 * @author WZ
 * @date 2019/6/27.
 * 严格要求自己，对每一行代码负责
 * description：
 */
public class CommItemDecoration extends RecyclerView.ItemDecoration {
    /**
     * 使用
     * 垂直方向：CommItemDecoration.createVertical(context, Color.BLUE,30)
     * 水平方向：CommItemDecoration.createHorizontal(context, Color.BLUE,30)
     */

    private static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
    private static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

    private int mSpace = 1;     //间隔
    private Rect mRect = new Rect(0, 0, 0, 0);
    private Paint mPaint = new Paint();

    private int mOrientation;

    private CommItemDecoration(Context context, int orientation, @ColorRes int color, int space) {
        mOrientation = orientation;
        if (space > 0) {
            mSpace = dp2px(space);
        }
        mPaint.setColor(ContextCompat.getColor(context, color));
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mSpace;
            mRect.set(left, top, right, bottom);
            c.drawRect(mRect, mPaint);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin;
            final int right = left + mSpace;
            mRect.set(left, top, right, bottom);
            c.drawRect(mRect, mPaint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == VERTICAL_LIST) {
            outRect.set(0, 0, 0, mSpace);
        } else {
            outRect.set(0, 0, mSpace, 0);
        }
    }

    public static CommItemDecoration createVertical(Context context, @ColorRes int color, int height) {
        return new CommItemDecoration(context, VERTICAL_LIST, color, height);
    }

    public static CommItemDecoration createHorizontal(Context context, @ColorRes int color, int width) {
        return new CommItemDecoration(context, HORIZONTAL_LIST, color, width);
    }

    public static int dp2px(final float dpValue) {
        final float scale = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
