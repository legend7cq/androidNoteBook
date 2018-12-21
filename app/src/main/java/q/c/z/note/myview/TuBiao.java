package q.c.z.note.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;

/**
 *
 */
public class TuBiao extends TextView {

    private final Paint mPaintFan;
    private final Paint mPaintText;
    private final int[] colors = new int[4];
    private List<Info> data = new ArrayList<>();
    private final int colorTot = 4; //颜色总数
    private final RectF rectF; //绘制矩形

    public TuBiao(Context context) {
        this(context, null);
    }

    public TuBiao(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TuBiao(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        String[] cls = {"#fafefe00", "#fa0098fe", "#fafe9800", "#fafe3200"};
        for (int j = 0; j < colorTot; ++j) {
            colors[j] = Color.parseColor(cls[j]);
        }
        rectF = new RectF(0, 0, 200, 0);
        mPaintFan = new Paint();
        mPaintFan.setAntiAlias(true);
        mPaintFan.setStyle(Paint.Style.FILL);

        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setColor(Color.BLACK);
    }

    public void setData(List<Info> d) {
        data.clear();
        data.addAll(d);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < data.size(); i++) {
            final Info info = data.get(i);
            float lastSweep = 0;
            SparseIntArray list = info.list;
            float sweepAngle = 360f / info.times;
            rectF.top = rectF.bottom + 20;//上下20
            rectF.bottom = rectF.top + 200;//高200

            for (int j = 0; j < list.size(); j++) {
                mPaintFan.setColor(colors[j % colorTot]); //循环取颜色
                float angle = sweepAngle * list.valueAt(j);
                canvas.drawArc(rectF, lastSweep, angle, true, mPaintFan);
                lastSweep += angle;
                canvas.drawText(String.valueOf(list.keyAt(j)),
                        zuobiaoX(lastSweep-angle/2) , zuobiaoY(lastSweep-angle/2) , mPaintText);//间隔值
                if (list.keyAt(j) == info.zuijinJge) {
                    canvas.drawText("卍", zuobiaoX(lastSweep-angle/3), zuobiaoY(lastSweep-angle/3), mPaintText);//最近间隔
                }
            }
            canvas.drawText(String.valueOf(info.qiu), rectF.right / 2-5, rectF.bottom-rectF.right / 2+5, mPaintText);//球
        }


    }

//    获取圆上的坐标
// 圆心坐标：(x0,y0)
//    半径：r
//    角度：a
//    圆周率： PI
//    则圆上任一点为：（x1,y1）
//    x1   =   x0   +   r   *   cos(a   *   PI   /180   )
//    y1   =   y0   +   r   *   sin(a   *   PI  /180   )

    private float zuobiaoX(float angle) { //将半径减少20是为了将文字的显示的坐标在圆内
        return (float) (rectF.right / 2 + (rectF.right / 2-20) * Math.cos(angle * PI / 180));
    }

    private float zuobiaoY(float angle) {
        return (float) (rectF.bottom -rectF.right / 2  + (rectF.right / 2-20) * Math.sin(angle * PI / 180));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int measuredWidth;
        int measuredHeight;
        if (widthMode == MeasureSpec.EXACTLY) {
            measuredWidth = widthSize;
        } else {// warp_content，自己计算view大小
            measuredWidth = (int) rectF.right;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            measuredHeight = heightSize;
        } else {
            measuredHeight = (int) (rectF.right+20) * 4;
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    public static class Info {
        public int qiu = 0;//号
        public int times = 0;//总次数
        public SparseIntArray list = new SparseIntArray(); //<间隔，次数>
        public int zuijinJge = 0;
    }


}
