package q.c.z.note.utils;


import android.content.Context;

import java.math.BigDecimal;
import java.util.Calendar;


public class CommUitls {

    /**
     * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //������Сת��
    public static String getPrintFileSize(long size) {
        // ����ֽ�������1024����ֱ����BΪ��λ�������ȳ���1024����3λ��̫��������
        double value = (double) size;
        if (value < 1024) {
            return String.valueOf(value) + "B";
        } else {
            value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        }
        // ���ԭ�ֽ�������1024֮������1024�������ֱ����KB��Ϊ��λ
        // ��Ϊ��û�е���Ҫʹ����һ����λ��ʱ��
        // ����ȥ�Դ�����
        if (value < 1024) {
            return String.valueOf(value) + "KB";
        } else {
            value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
        }
        if (value < 1024) {
            return String.valueOf(value) + "MB";
        } else {
            // �������Ҫ��GBΪ��λ�ģ��ȳ���1024����ͬ���Ĵ���
            value = new BigDecimal(value / 1024).setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            return String.valueOf(value) + "GB";
        }
    }


    private static final int MIN_CLICK_DELAY_TIME = 5000;
    private static long lastClickTime = 0;

    public static boolean isFastClick() {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            return false;
        }
        return true;

//        long currentTime = Calendar.getInstance().getTimeInMillis();
//        long diff = currentTime - lastClickTime;
//        lastClickTime = currentTime;
//        return diff < MIN_CLICK_DELAY_TIME;
    }

}
