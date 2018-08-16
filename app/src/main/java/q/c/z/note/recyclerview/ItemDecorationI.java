package q.c.z.note.recyclerview;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.ColorInt;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import q.c.z.note.utils.CommUitls;


/**
 * Created by zcq on 2018/4/23.
 *装饰器
 */

public class ItemDecorationI extends RecyclerView.ItemDecoration {

    private int vv = 0;
    private int hh = 0;
    private Paint divPaint;

    public ItemDecorationI() {
        super();
    }

    /**
     * 默认透明分割
     * @param right
     * @param button
     */
    public ItemDecorationI(int right, int button) {
        this(right, button, Color.TRANSPARENT);
    }

    public ItemDecorationI(int right, int button, @ColorInt int diverColor) {
        super();
        hh = right;
        vv = button;
        divPaint = new Paint();
        divPaint.setColor(diverColor);
        divPaint.setAntiAlias(true);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.right = CommUitls.dip2px(view.getContext(), hh);
        outRect.bottom = CommUitls.dip2px(view.getContext(), vv);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getRight() - parent.getPaddingRight();
        int count = parent.getChildCount();

        for (int i = 0; i < count; i++) {
            View view = parent.getChildAt(i);
            //h
            int top = view.getBottom();
            int button = top+CommUitls.dip2px(view.getContext(), vv);
            c.drawRect(left,top,right,button,divPaint);
            //v
            int rightDiff =right- CommUitls.dip2px(view.getContext(), hh);
            c.drawRect(rightDiff,view.getTop(),right,top,divPaint);
        }
    }
}
