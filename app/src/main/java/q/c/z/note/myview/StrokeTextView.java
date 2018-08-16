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
 * �������
 *
 * ʹ�õ�ʱ�����õ��ַ�����Ҫ�����߼��Ͽո�
 * ��Ȼ�������ʾ���������ַ�������)����������ߵĿ��û�м��㣬���³���
 * " mm:ss "  ,
 * �еĲ��ӿո�Ҳľ�����⡣������ �����
 */
@SuppressLint("AppCompatCustomView")
public class StrokeTextView extends TextView {

    private final Paint m_TextPaint;
    private boolean isStroken; // �Ƿ����
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
        m_TextPaint.setShadowLayer(2, 0, 0, strokenW); // �������ӰЧ�������Ժ���
        m_TextPaint.setStrokeWidth(strokenW); // ��߿��
        m_TextPaint.setFakeBoldText(true);
        textColor = getCurrentTextColor();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isStroken) {
            // �����
            // super.setTextColor(Color.BLUE); // ����ֱ����ô�裬��˻ᵼ�µݹ�
            setTextColorUseReflection(strokenColor);
            m_TextPaint.setStyle(Paint.Style.STROKE);
            super.onDraw(canvas);

            // ���ڲ㣬�ָ�ԭ�ȵĻ���
            setTextColorUseReflection(textColor);
            m_TextPaint.setStyle(Paint.Style.FILL);
        }
        super.onDraw(canvas);
    }

    /**
     * ʹ�÷���ķ�������������ɫ������
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
