package com.util;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberUtil {

    static double[] num = {3.4567, 3, 0.4567, 2343.333, 133, 3.1415};

    //保留两位小数
    public static void func() {
        int i = 1;
        for (double d : num) {
            System.out.println("测试" + (i++) + " = " + d);
            String dstr = String.valueOf(d);

            // 第一种：
            java.text.DecimalFormat df = new java.text.DecimalFormat("#.##");
            System.out.println("第一种=" + df.format(d));

            // 第二种：通过下面的结果可以看出，一二两种都可以，第一种如果小数部分是0的话就只显示整数，第二种始终显示两位小数
            BigDecimal bd = new BigDecimal(dstr);
            bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
            System.out.println("第二种=" + bd);

            // 第三种：
            long l = Math.round(d * 100); // 四舍五入
            double ret = l / 100.0; // 注意：使用 100.0 而不是 100
            System.out.println("第三种=" + ret);

            // 第四种：
            d = ((int) (d * 100)) / 100;
            System.out.println("第四种=" + d);

            //第五种
            DecimalFormat df2 = new DecimalFormat("#.00");
            //df2.setRoundingMode(RoundingMode.HALF_UP);
            System.out.println("第五种=" + df2.format(d));
            System.out.println("-------------------------风骚的java分割线---------------------------");
        }
    }

    /**
     * 分转元
     * @param fen 金额分
     */
    public static String f2y(String fen) {
        String moneyPenny = StringUtils.leftPad(fen, 3, '0');
        int len = moneyPenny.length();
        StringBuilder moneyYuanSb = new StringBuilder(moneyPenny.substring(0, len-2));
        return moneyYuanSb.append(".").append(moneyPenny.substring(len-2)).toString();
    }

    /**
     * 分转元
     * @param fen 金额分
     */
    public static String f2y(long fen) {
        String oriFen = String.valueOf(fen);
        return f2y(oriFen);
    }

    public static void main(String[] args) {
        long fen = 1L;
        System.out.println(f2y(fen));

        func();

    }
}
