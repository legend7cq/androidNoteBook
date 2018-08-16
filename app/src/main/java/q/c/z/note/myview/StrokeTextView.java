package q.c.z.note.myview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.reflect.Field;

import q.c.z.note.R;

/**
 *
 * 文字描边
 *
 * 使用的时候，设置的字符串需要在两边加上空格，
 * 不然，描边显示不完整（字符串两边)，估计是描边的宽度没有计算，导致出错）
 * " mm:ss "  ,
 * 有的不加空格也木有问题。。。。 待解决
 */
@SuppressLint("AppCompatCustomView")
public class StrokeTextView extends TextView {

    private final Paint m_TextPaint;
    private boolean isStroken; // 是否描边
    private final int textColor;
    private int strokenColor;
    public StrokeTextView(Context context) {
        this(context, null);
    }

    public StrokeTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StrokeTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StrokeTextView);
        isStroken = typedArray.getBoolean(R.styleable.StrokeTextView_strokenNeed, true);
        strokenColor = typedArray.getColor(R.styleable.StrokeTextView_strokenColor,Color.WHITE);
        int strokenW=typedArray.getDimensionPixelSize(R.styleable.StrokeTextView_strokenWidth,5);
        typedArray.recycle();

        m_TextPaint = getPaint();
        m_TextPaint.setShadowLayer(2, 0, 0, strokenW); // 字体的阴影效果，可以忽略
        m_TextPaint.setStrokeWidth(strokenW); // 描边宽度
        m_TextPaint.setFakeBoldText(true);
        textColor = getCurrentTextColor();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isStroken) {
            // 描外层
            // super.setTextColor(Color.BLUE); // 不能直接这么设，如此会导致递归
            setTextColorUseReflection(strokenColor);
            m_TextPaint.setStyle(Paint.Style.STROKE);
            super.onDraw(canvas);

            // 描内层，恢复原先的画笔
            setTextColorUseReflection(textColor);
            m_TextPaint.setStyle(Paint.Style.FILL);
        }
        super.onDraw(canvas);
    }

    /**
     * 使用反射的方法进行字体颜色的设置
     *
     * @param color
     */
    private void setTextColorUseReflection(int color) {
        Field textColorField;
        try {
            textColorField = TextView.class.getDeclaredField("mCurTextColor");
            textColorField.setAccessible(true);
            textColorField.set(this, color);
            textColorField.setAccessible(false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
