package q.c.z.note.myview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import q.c.z.note.R;
import q.c.z.note.utils.CommUitls;

/**
 *
 * 实现逻辑就是，
 * 先画底图（就是最终显示的形状）
 * 然后画颜色矩形和底图叠加，画笔属性SRC_IN
 * 使用属性动画逐渐修改，矩形颜色占比
 */
public class LoadingView1 extends View {

    private final RectF rectF; //绘制矩形
    private final ValueAnimator animator;
    private final Paint mPaintFan;
    private Bitmap bg;
    private int bgH = 0;//高度
    private final int bgW;
    private final int[] colors = new int[4];
    private int cp = 3; //画笔颜色指针，第一个矩形颜色,从最后一个开始取颜色

    private final int addWidth; //view增加宽度,相对于bg的高宽，为了旋转canvas后绘制的矩形能够全部覆盖bg

    private final int colorTot = 4; //颜色总数
    private final float scale = 0.25f; //一种颜色占比

    private Rotate3dAnimation mRotate3dAnimation;//视图3d翻转

    public LoadingView1(Context context) {
        this(context, null);
    }

    public LoadingView1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingView);
        Boolean b = typedArray.getBoolean(R.styleable.LoadingView_start, true);
        typedArray.recycle();

        String[] cls = {"#fafefe00", "#fa0098fe", "#fafe9800", "#fafe3200"};
        for (int j = 0; j < colorTot; ++j) {
            colors[j] = Color.parseColor(cls[j]);
        }

        addWidth = CommUitls.dip2px(getContext(), 30);

        mPaintFan = new Paint();
        mPaintFan.setAntiAlias(true);
        mPaintFan.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        bg = BitmapFactory.decodeResource(getResources(), R.mipmap.loading);
        bgH = bg.getHeight();
        bgW = bg.getWidth();
        rectF = new RectF(0, 0, bgW + 2 * addWidth, 0);

        animator = ValueAnimator.ofFloat(0, scale).setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                refresh((Float) animation.getAnimatedValue());
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                //onAnimationUpdate返回值，不准确，有时不能返最后值
                --cp;
                if (cp < 0) {
                    cp = colorTot - 1;
                }
            }
        });


        //3d翻转，没有使用
        mRotate3dAnimation = new Rotate3dAnimation(0, 360,
                bgW / 2 + addWidth, 0, 0, false);
        mRotate3dAnimation.setDuration(3000);
        mRotate3dAnimation.setFillAfter(true);
        mRotate3dAnimation.setRepeatCount(Animation.INFINITE);
        mRotate3dAnimation.setInterpolator(new AccelerateInterpolator());

        if (b) {
            animator.start();
//            startAnimation(mRotate3dAnimation);
        }

    }

    public void start() {
        if (!animator.isStarted()) {
            animator.start();
            startAnimation(mRotate3dAnimation);
        }
    }

    private void refresh(float sca) {
        rectF.top = 0;
        rectF.bottom = sca * bgH;
        invalidate();
    }

    public void destroy() {
        if (bg == null) {
            return;
        }

        animator.cancel();
        bg.recycle();
        bg = null;
        animate().alpha(0).scaleY(0).scaleX(0).setDuration(200).start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bg == null) {
            return;
        }

        canvas.drawBitmap(bg, addWidth, CommUitls.dip2px(getContext(), 10), null);
        canvas.rotate(30, bgW / 2, bgH / 2);

        //第一个矩形,总共五个
        mPaintFan.setColor(colors[cp]);
        canvas.drawRect(rectF, mPaintFan);

        for (int i = 1; i < colorTot + 1; ++i) { //剩下4个
            rectF.top = rectF.bottom;
            rectF.bottom += bgH * scale; //总共四种颜色每样25%。第一个矩形和最后一个矩形为同一种颜色共占25%
            mPaintFan.setColor(colors[(cp + i) % colorTot]); //循环取颜色
            canvas.drawRect(rectF, mPaintFan);
        }

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
            measuredWidth = bgW + 2 * addWidth; //bg两边各addWidth
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            measuredHeight = heightSize;
        } else {
            measuredHeight = bgH;
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }


}
