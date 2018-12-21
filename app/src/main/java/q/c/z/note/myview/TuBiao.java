package q.c.z.note.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.PI;

/**
 *扇形图
 */
public class TuBiao extends View {

    public static final int MIN = 20;
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
        rectF = new RectF(0, 0, 600, 0);
        mPaintFan = new Paint();
        mPaintFan.setAntiAlias(true);
        mPaintFan.setStyle(Paint.Style.FILL);

        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setColor(Color.BLACK);
        mPaintText.setTextSize(24);
    }

    public void setData(List<Info> d) {
        data.clear();
        data.addAll(d);
        rectF.top = 0;
        rectF.bottom = 0;
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {

        for (int i = 0; i < data.size(); i++) {
            final Info info = data.get(i);
            float lastSweep = 0;
            SparseIntArray list = info.list;
            float sweepAngle = 360f / info.times;
            rectF.top = rectF.bottom + MIN;//上下20
            rectF.bottom = rectF.top + rectF.right;

            for (int j = 0; j < list.size(); j++) {
                mPaintFan.setColor(colors[j % colorTot]); //循环取颜色
                float angle = sweepAngle * list.valueAt(j);
                canvas.drawArc(rectF, lastSweep, angle, true, mPaintFan);
                lastSweep += angle;
                int less = MIN + j % 2 * MIN*2;//减小半径
                float angle1 = lastSweep - angle / 2;//显示位置
                canvas.drawText(String.valueOf(list.keyAt(j)),
                        zuobiaoX(angle1, less), zuobiaoY(angle1, less), mPaintText);//间隔值

                if (list.keyAt(j) == info.zuijinJge) {
                    mPaintText.setColor(Color.WHITE);
                    canvas.drawText("■", zuobiaoX(angle1, 60),
                            zuobiaoY(angle1, 60), mPaintText);//最近间隔
                    mPaintText.setColor(Color.BLACK);
                }
            }
            canvas.drawText(String.valueOf(info.qiu < 34 ? info.qiu : info.qiu - 33),
                    rectF.right / 2 - 5, rectF.bottom - rectF.right / 2 + 5, mPaintText);//球
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

    private float zuobiaoX(float angle, int less) { //将半径减少less是为了将文字的显示的坐标在圆内
        return (float) (rectF.right / 2 + (rectF.right / 2 - less) * Math.cos(angle * PI / 180));
    }

    private float zuobiaoY(float angle, int less) {
        return (float) (rectF.bottom - rectF.right / 2 + (rectF.right / 2 - less) * Math.sin(angle * PI / 180));
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
            measuredHeight = (int) (rectF.right + MIN) * 49;
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    public static class Info {
        public int qiu = 0;//号
        public int times = 0;//总次数
        public SparseIntArray list = new SparseIntArray(); //<间隔，次数>
        public int zuijinJge = 0;

        public Info(int q) {
            qiu = q;
        }
    }


}
